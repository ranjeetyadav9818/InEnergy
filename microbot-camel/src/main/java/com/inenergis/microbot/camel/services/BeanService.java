package com.inenergis.microbot.camel.services;

import com.inenergis.entity.Account;
import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.AgreementPointMapPK;
import com.inenergis.entity.DataMapping;
import com.inenergis.entity.DataMappingType;
import com.inenergis.entity.History;
import com.inenergis.entity.Meter;
import com.inenergis.entity.Person;
import com.inenergis.entity.Premise;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.ServicePoint;
import com.inenergis.entity.VModelEntity;
import com.inenergis.entity.genericEnum.EnrolmentStatus;
import com.inenergis.entity.log.FileProcessorLog;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramEligibilitySnapshot;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.microbot.camel.csv.Customer;
import com.inenergis.microbot.camel.csv.CustomerInterface;
import com.inenergis.microbot.camel.dao.AccountDao;
import com.inenergis.microbot.camel.dao.AgreementPointMapDao;
import com.inenergis.microbot.camel.dao.DataMappingDao;
import com.inenergis.microbot.camel.dao.FileProcessorLogDao;
import com.inenergis.microbot.camel.dao.HistoryDao;
import com.inenergis.microbot.camel.dao.MeterDao;
import com.inenergis.microbot.camel.dao.PersonDao;
import com.inenergis.microbot.camel.dao.PremiseDao;
import com.inenergis.microbot.camel.dao.ProgramDao;
import com.inenergis.microbot.camel.dao.ProgramServiceAgreementEnrollmentDao;
import com.inenergis.microbot.camel.dao.ServiceAgreementDao;
import com.inenergis.microbot.camel.dao.ServicePointDao;
import com.inenergis.microbot.camel.exception.ProcessingException;
import com.inenergis.model.EligibilityResult;
import com.inenergis.util.EligibilityChecker;
import lombok.Getter;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.inenergis.entity.DRCCBeanUtils.generateHistoryFromDifferences;

@Getter
@Setter
@Service
public class BeanService {

    private static final Logger logger = LoggerFactory.getLogger(BeanService.class);

    @Autowired
    ServiceAgreementDao serviceAgreementDao;

    @Autowired
    ProgramServiceAgreementEnrollmentDao programServiceAgreementEnrollmentDao;

    @Autowired
    DataMappingDao dataMappingDao;

    @Autowired
    FileProcessorLogDao fileProcessorLogDao;

    @Autowired
    PersonDao personDao;

    @Autowired
    AccountDao accountDao;

    @Autowired
    PremiseDao premiseDao;

    @Autowired
    MeterDao meterDao;

    @Autowired
    ServicePointDao servicePointDao;

    @Autowired
    AgreementPointMapDao agreementPointMapDao;

    @Autowired
    ProgramDao programDao;

    @Autowired
    HistoryDao historyDao;

    @Autowired
    LocalContainerEntityManagerFactoryBean managerFactoryBean;

    public void saveVModel(Exchange exchange) throws Exception {
        final CustomerInterface customer = (CustomerInterface) exchange.getIn().getBody();
        for (Pair<VModelEntity, VModelEntity> entityPair : generateVModelEntities(customer)) {
            createHistoryChanges(entityPair.getKey(), entityPair.getValue());
        }
        for (Pair<VModelEntity, VModelEntity> entityPair : generateVModelEntities(customer)) {
            saveOrUpdate(entityPair.getValue());
        }
        if (customer instanceof Customer) {
            saveEnrollments((Customer) customer);
        }
    }

    public void saveInHeaderFileLog(Exchange exchange) {
        String fileName = (String) exchange.getIn().getHeader("fileName");
        exchange.getIn().setHeader("fileProcessorLog", getFileProcessorLogByName(fileName));
    }

    public void updateFileLogFinished(Exchange exchange) {
        String fileName = (String) exchange.getIn().getHeader("fileName");
        FileProcessorLog fileProcessorLog = getFileProcessorLogByName(fileName);
        fileProcessorLog.setTimeCompleted(new Date());
        exchange.getIn().setBody(fileProcessorLog);
    }

    @Transactional
    @Modifying
    public void createFileLog(Exchange exchange) {
        FileProcessorLog fileLog = new FileProcessorLog();
        fileLog.setFilename(((String) exchange.getIn().getHeader("fileName")));
        fileLog.setProcessDate(new Date());
        String fileType = (String) exchange.getIn().getHeader("fileType");
        if (fileType != null) {
            fileLog.setFileType(fileType);
        }
        fileProcessorLogDao.save(fileLog);
    }

    @Transactional
    public void checkEnrollmentsEligibility(Exchange exchange) {
        List<Customer> customers = (List<Customer>) exchange.getIn().getBody();
        List<ProgramServiceAgreementEnrollment> enrollmentsToUnenroll = new ArrayList<>();
        for (Customer customer : customers) {
            ServiceAgreement sa = serviceAgreementDao.getByServiceAgreementId(customer.getSA_ID());
            for (ProgramServiceAgreementEnrollment enrollment : sa.getEnrollments()) {
                checkEnrollmentHasToBeUnenrolled(customers, enrollmentsToUnenroll, customer, sa, enrollment);
            }
        }
        enrollmentsToUnenroll = enrollmentsToUnenroll.stream().distinct().collect(Collectors.toList());
        exchange.getIn().setBody(enrollmentsToUnenroll);
    }

    @Transactional
    @Modifying
    private void checkEnrollmentHasToBeUnenrolled(List<Customer> customers, List<ProgramServiceAgreementEnrollment> enrollmentsToUnenroll,
                                                  Customer customer, ServiceAgreement sa, ProgramServiceAgreementEnrollment enrollment) {
        if (enrollment.getEnrollmentStatus().equals(EnrolmentStatus.IN_PROGRESS.getName()) || enrollment.getEnrollmentStatus().equals(EnrolmentStatus.ENROLLED.getName())) {
            ProgramProfile programProfile = enrollment.getProgram().getActiveProfile();
            if (customer.getSA_END_DATE() != null) {
                Customer stitchedCustomer = isUniqueSAIDInFile(customer.getSA_UUID(), customer.getSA_ID(), customers);
                if (stitchedCustomer == null) {
                    enrollmentsToUnenroll.add(enrollment);
                } else {
                    ServiceAgreement serviceAgreement = serviceAgreementDao.getByServiceAgreementId(stitchedCustomer.getSA_ID());
                    enrollment.setServiceAgreement(serviceAgreement);
                    sa = serviceAgreement;
                    programServiceAgreementEnrollmentDao.save(enrollment);
                }
            }
            if (programProfile != null) {
                ServicePoint sp = sa.getAgreementPointMaps().get(0).getServicePoint();
                String meterType = getMeterType(sp.getMeter().getConfigType());
                EligibilityResult eligibilityResult = doEligibilityChecks(programProfile, meterType, sa, sp);
                if (!eligibilityResult.getErrors().isEmpty()) {
                    enrollmentsToUnenroll.add(enrollment);
                }
                enrollment.getSnapshots().addAll(ProgramEligibilitySnapshot.createSnapshots(eligibilityResult, enrollment, null, null));
                programServiceAgreementEnrollmentDao.save(enrollment);
            }
        }
    }

    private Customer isUniqueSAIDInFile(String sa_uuid, String sa_id, List<Customer> customers) {
        return customers.stream().filter(c -> !c.getSA_ID().equals(sa_id) && c.getSA_UUID().equals(sa_uuid)).findFirst().orElse(null);
    }

    @Transactional
    private FileProcessorLog getFileProcessorLogByName(String name) {
        List<FileProcessorLog> fileProcessorLogs = fileProcessorLogDao.getByFilename(name);
        if (fileProcessorLogs.size()>0) {
            fileProcessorLogs.sort(Comparator.comparing(FileProcessorLog::getProcessDate).reversed());
            return fileProcessorLogs.get(0);
        }
        return null;
    }

    @Transactional
    private void saveEnrollments(Customer customer) throws ProcessingException {
        ServiceAgreement sa = serviceAgreementDao.getByServiceAgreementId(customer.getSA_ID());
        ProgramServiceAgreementEnrollment newEnrollment = createPDPEnrollment(customer, sa);
        if (newEnrollment != null) {
            saveEnrollment(newEnrollment, newEnrollment.getProgram());
        }
        ProgramServiceAgreementEnrollment srEnrollment = createSREnrollment(customer, sa);
        if (srEnrollment != null) {
            saveEnrollment(srEnrollment, srEnrollment.getProgram());
        }
    }

    @Transactional
    private String getMeterType(String configType) {
        DataMapping dataMapping = dataMappingDao.getFirstByTypeAndSource(DataMappingType.METER_TYPE, configType);
        if (dataMapping != null) {
            return dataMapping.getDestination();
        }
        return null;
    }

    @Transactional
    @Modifying
    private void saveEnrollment(ProgramServiceAgreementEnrollment newEnrollment, Program program) throws ProcessingException {
        ProgramServiceAgreementEnrollment oldEnrollment = getPreviousEnrollment(newEnrollment, program);
        if (oldEnrollment != null) {
            newEnrollment.setId(oldEnrollment.getId());
            saveHistoryChanges(oldEnrollment, newEnrollment, null, Arrays.asList("uuid", "fsls", "snapshots", "program", "serviceAgreement"), "id");
        }
        programServiceAgreementEnrollmentDao.save(newEnrollment);
    }

    @Transactional
    private ProgramServiceAgreementEnrollment getPreviousEnrollment(ProgramServiceAgreementEnrollment enrollment, Program program) {
        Instant instant = enrollment.getEffectiveStartDate().toInstant().atZone(ZoneId.systemDefault()).plusDays(1L).toInstant();
        return programServiceAgreementEnrollmentDao.getFirstByProgramAndServiceAgreementAndEffectiveStartDateGreaterThanEqualAndEffectiveEndDateLessThan(program, enrollment.getServiceAgreement(), enrollment.getEffectiveStartDate(), Date.from(instant));
    }

    @Transactional
    private List<Pair<VModelEntity, VModelEntity>> generateVModelEntities(CustomerInterface customer) {
        List<Pair<VModelEntity, VModelEntity>> result = new ArrayList<>();

        Person personFromDatabase = personDao.getByPersonId(customer.getPERSON_ID());
        Person newPerson = populatePersonWithCustomerData(customer);
        result.add(new ImmutablePair<>(personFromDatabase, newPerson));

        Account accountFromDB = accountDao.getByAccountId(customer.getACCOUNT_ID());
        Account newAccount = populateAccountWithCustomerData(customer, newPerson);
        result.add(new ImmutablePair<>(accountFromDB, newAccount));

        ServiceAgreement saFromDB = serviceAgreementDao.getByServiceAgreementId(customer.getSA_ID());
        ServiceAgreement newSA = populateSAWithCustomerData(customer, newAccount);
        result.add(new ImmutablePair<>(saFromDB, newSA));

        Premise premiseFromDB = premiseDao.getByPremiseId(customer.getPREMISE_ID());
        Premise newPremise = populatePremiseWithCustomerData(customer);
        result.add(new ImmutablePair<>(premiseFromDB, newPremise));

        Meter meterFromDB = meterDao.getByMeterId(customer.getMETER_ID());
        Meter newMeter = populateMeterWithCustomerData(customer);
        result.add(new ImmutablePair<>(meterFromDB, newMeter));

        ServicePoint servicePointFromDB = servicePointDao.getByServicePointId(customer.getSP_ID());
        ServicePoint newServicePoint = populateServicePointWithCustomerData(customer, newPremise, newMeter);
        result.add(new ImmutablePair<>(servicePointFromDB, newServicePoint));

        AgreementPointMap agreementPointMapFromDB = agreementPointMapDao.getById(new AgreementPointMapPK(customer.getSP_ID(), customer.getSA_ID()));
        AgreementPointMap newAgreementPointMap = populateAgreementPointMapWithCustomerData(customer, newServicePoint, newSA);
        result.add(new ImmutablePair<>(agreementPointMapFromDB, newAgreementPointMap));

        return result;
    }

    private void createHistoryChanges(VModelEntity entityFromDB, VModelEntity newEntity) throws ProcessingException {
        if (entityFromDB != null) {
            saveHistoryChanges(entityFromDB, newEntity, newEntity.relationshipFieldNames(), newEntity.excludedFieldsToCompare(), newEntity.idFieldName());
        }
    }

    private void saveOrUpdate(VModelEntity newEntity) {
        EntityManager entityManager = managerFactoryBean.getNativeEntityManagerFactory().createEntityManager();
        try{
            entityManager.getTransaction().begin();
            entityManager.merge(newEntity);
            entityManager.getTransaction().commit();
        } catch (Exception e){
            entityManager.getTransaction().rollback();
        }finally {
            entityManager.close();
        }
    }

    @Transactional
    @Modifying
    private void saveHistoryChanges(Object entityFromDB, Object newEntity, List<String> relationshipFieldNames, List<String> excludedFields,
                                    String idFieldName) throws ProcessingException {
        try {
            logger.debug("comparing {} with {}", entityFromDB, newEntity);
            String author = "DRCC System";
            final List<History> histories = generateHistoryFromDifferences(entityFromDB, newEntity, relationshipFieldNames, excludedFields, idFieldName, author);
            for (History history : histories) {
                historyDao.save(history);
                logger.debug("history created {}", history);
            }
        } catch (Exception e) {
            logger.error("Error generating the history for " + newEntity, e);
            throw new ProcessingException(e);
        }
    }

    private Person populatePersonWithCustomerData(CustomerInterface customer) {
        Person newPerson = new Person();
        newPerson.setPersonId(customer.getPERSON_ID());
        newPerson.setCustomerName(customer.getLAST_NAME());
        newPerson.setBusinessName(customer.getDO_BUS_AS_NM());
        newPerson.setPhoneExtension(customer.getPHONE_EXTENSION());
        newPerson.setBusinessOwner(customer.getBUS_OWNER());
        return newPerson;
    }

    private Account populateAccountWithCustomerData(CustomerInterface customer, Person person) {
        Account account = new Account();
        account.setAccountId(customer.getACCOUNT_ID());
        account.setPerson(person);
        return account;
    }

    private ServiceAgreement populateSAWithCustomerData(CustomerInterface customer, Account newAccount) {
        ServiceAgreement serviceAgreement = new ServiceAgreement();
        serviceAgreement.setAccount(newAccount);
        serviceAgreement.setBillCycleCd(customer.getBILL_CYCLE_CD());
        serviceAgreement.setBillSystem(customer.getBILL_SYSTEM());
        serviceAgreement.setBusinessActivityDescription(customer.getBUSINESS_ACTIVITY_DESC());
        serviceAgreement.setCareFlag(customer.isCareFlag());
        serviceAgreement.setClimateZone(customer.getCLIMATE_ZONE());
        serviceAgreement.setCust_size(customer.getCUST_SIZE());
        serviceAgreement.setCustClassCd(customer.getCUST_CLASS_CD());
        serviceAgreement.setCustomerLSECompanyName(customer.getCUSTOMER_LSE_COMPANY_NAME());
        serviceAgreement.setDivisionCode19(customer.getDIVISION_CODE_19());
        serviceAgreement.setEndDate(customer.getSA_END_DATE());
        serviceAgreement.setEssDivisionCode(customer.getESS_DIVISION_CODE());
        serviceAgreement.setFeraFlag(customer.isFeraFlag());
        serviceAgreement.setHas3rdPartyDrp(customer.getHAS_3RD_PARTY_DRP());
        serviceAgreement.setLifeSupportInd(customer.isLifeSupport());
        serviceAgreement.setMailingAddress1(customer.getMAILING_ADDRESS1());
        serviceAgreement.setMailingAddress2(customer.getMAILING_ADDRESS2());
        serviceAgreement.setMailingCityUpr(customer.getMAILING_CITY_UPR());
        serviceAgreement.setMailingPostal(customer.getMAILING_ZIP());
        serviceAgreement.setMailingState(customer.getMAILING_STATE());
        serviceAgreement.setMarketSegment(customer.getMARKET_SEGMENT());
        serviceAgreement.setMedicalBaselineInd(customer.isMedicalBaseline());
        serviceAgreement.setNaics(customer.getSA_NAICS());
        serviceAgreement.setPhone(customer.getPHONE());
        serviceAgreement.setRateCodeEffectiveDate(customer.getRATE_CODE_EFFECTIVE_DATE());
        serviceAgreement.setRateSchedule(customer.getRS_CD());
        serviceAgreement.setResInd(customer.isResInd());
        serviceAgreement.setRevenueClassDesc(customer.getREVENUE_CLASS_DESC());
        serviceAgreement.setSaStatus(customer.getSA_STATUS());
        serviceAgreement.setSaUuid(customer.getSA_UUID());
        serviceAgreement.setServiceAgreementId(customer.getSA_ID());
        serviceAgreement.setStartDate(customer.getSA_START_DATE());
        serviceAgreement.setSupplierIsDrp(customer.isSupplierIsDRP());
        serviceAgreement.setTypeCd(customer.getSA_TYPE_CD());
        serviceAgreement.setUniqSaId(customer.getUNIQ_SA_ID());
        serviceAgreement.setUniqSaIdCreateDate(customer.getUNIQ_SA_ID_CREATE_DATE());
        serviceAgreement.setUniqSaIdWarnFlag(customer.getUNIQ_SA_ID_WARN_FLAG());
        serviceAgreement.setCustomerLseCode(customer.getCUSTOMER_LSE_CODE());
        serviceAgreement.setEssentialCustomerFlag(customer.getESSENTIAL_CUSTOMER_FLAG());
        return serviceAgreement;
    }

    private ProgramServiceAgreementEnrollment createPDPEnrollment(Customer customer, ServiceAgreement serviceAgreement) {
        if (customer.isPdpEnrolled() || customer.getPDP_START_DATE() != null) {
            ProgramServiceAgreementEnrollment enrollment = new ProgramServiceAgreementEnrollment();
            enrollment.setServiceAgreement(serviceAgreement);
            enrollment.setEnrollmentChannel("DRCM - CDW Parser");
            enrollment.setEnrollmentSource("CDW");
            enrollment.setBillProtection(customer.isPdpBillProtection());
            enrollment.setPlanOptions(customer.getPDP_PLAN_OPTIONS());
            enrollment.setReservationCapacityAppliedValue(customer.getPDP_RESV_CAP_VAL());
            enrollment.setEnrollmentStatus(customer.getPDP_STATUS());
            enrollment.setEffectiveStartDate(customer.getPDP_START_DATE());
            enrollment.setOriginalEffectiveStartDate(customer.getPDP_START_DATE());
            enrollment.setEffectiveEndDate(customer.getPDP_STOP_DATE());
            enrollment.setProgram(getProgramByName("PDP"));
            return enrollment;
        }
        return null;
    }

    private ProgramServiceAgreementEnrollment createSREnrollment(Customer customer, ServiceAgreement serviceAgreement) {
        if (customer.isSmartRate() || customer.getSR_START_DATE() != null) {
            ProgramServiceAgreementEnrollment enrollment = new ProgramServiceAgreementEnrollment();
            enrollment.setServiceAgreement(serviceAgreement);
            enrollment.setEnrollmentChannel("DRCM - CDW Parser");
            enrollment.setEnrollmentSource("CDW");
            enrollment.setBillProtection(customer.isSrBillProtection());
            enrollment.setEffectiveStartDate(customer.getSR_START_DATE());
            enrollment.setOriginalEffectiveStartDate(customer.getSR_START_DATE());
            enrollment.setEffectiveEndDate(customer.getSR_END_DATE());
            enrollment.setProgram(getProgramByName("Smart Rate"));
            return enrollment;
        }
        return null;
    }

    @Transactional
    private Program getProgramByName(String programName) {
        return programDao.getByName(programName);
    }

    private Premise populatePremiseWithCustomerData(CustomerInterface customer) {
        Premise premise = new Premise();
        premise.setBaseLineChar(customer.getPREM_BASELINE_CHAR());
        premise.setCounty(customer.getCOUNTY());
        premise.setPremiseId(customer.getPREMISE_ID());
        premise.setPremiseType(customer.getPREMISE_TYPE());
        premise.setServiceAddress1(customer.getSERVICE_ADDRESS1());
        premise.setServiceAddress2(customer.getSERVICE_ADDRESS2());
        premise.setServiceCityUpr(customer.getSERVICE_CITY_UPR());
        premise.setServicePostal(customer.getSERVICE_POSTAL());
        premise.setServiceState(customer.getSERVICE_STATE());
        return premise;
    }

    private Meter populateMeterWithCustomerData(CustomerInterface customer) {
        Meter meter = new Meter();
        meter.setBadgeNumber(customer.getMTR_BADGENBR());
        meter.setConfigType(customer.getMTR_CONFIG_TYPE());
        meter.setInstallDate(customer.getMTR_INSTALL_DT());
        meter.setMeterId(customer.getMETER_ID());
        meter.setMfg(customer.getMTR_MFG());
        meter.setReadFreq(customer.getMTR_READ_FREQ());
        meter.setSmModuleMfr(customer.getSM_MODULE_MFR());
        meter.setSmStatus(customer.getSM_STATUS());
        meter.setUninstallDate(customer.getMTR_UNINSTALL_DT());
        return meter;
    }

    private ServicePoint populateServicePointWithCustomerData(CustomerInterface customer, Premise newPremise, Meter newMeter) {
        ServicePoint servicePoint = new ServicePoint();
        servicePoint.setCircuitNumber(customer.getCIRCUIT_NUMBER());
        servicePoint.setCustomerMdmaCompanyName(customer.getCUSTOMER_MDMA_COMPANY_NAME());
        servicePoint.setCustomerMspCompanyName(customer.getCUSTOMER_MSP_COMPANY_NAME());
        servicePoint.setElecUsageNonres(customer.getELEC_USAGE_NONRES());
        servicePoint.setFeeder(customer.getFEEDER());
        servicePoint.setLatitude(customer.getLATITUDE());
        servicePoint.setLongitude(customer.getLONGITUDE());
        servicePoint.setMeter(newMeter);
        servicePoint.setMeterReadCycle12(customer.getCUST_METER_READ_CYCLE12());
        servicePoint.setOperationArea(customer.getOPERATION_AREA());
        servicePoint.setPremise(newPremise);
        servicePoint.setRobCode(customer.getROB_CODE());
        servicePoint.setCustomerMdmaCode(customer.getCUSTOMER_MDMA_CODE());
        servicePoint.setCustomerMspCode(customer.getCUSTOMER_MSP_CODE());
        servicePoint.setServicePointId(customer.getSP_ID());
        servicePoint.setServiceType(customer.getSERVICE_TYPE());
        servicePoint.setServiceVoltageClass(customer.getCUST_SERVICE_VOLTAGE_CLASS());
        servicePoint.setSourceSideDeviceNumber(customer.getSOURCE_SIDE_DEVICE_NUMBER());
        servicePoint.setSubstation(customer.getSUBSTATION().trim());
        servicePoint.setSubStationNumber(customer.getSUB_STATION_NUMBER());
        servicePoint.setTrfmrBadgeNumber(customer.getTRFMR_BDG_NUMBER());
        servicePoint.setTrfmrNumber(customer.getTRFMR_NUMBER());
        return servicePoint;
    }

    private AgreementPointMap populateAgreementPointMapWithCustomerData(CustomerInterface customer, ServicePoint newServicePoint, ServiceAgreement newSA) {
        AgreementPointMap agreementPointMap = new AgreementPointMap();
        agreementPointMap.setId(new AgreementPointMapPK(newServicePoint.getServicePointId(), newSA.getServiceAgreementId()));
        agreementPointMap.setEndDate(customer.getSA_SP_STOP_DTTM());
        agreementPointMap.setStartDate(customer.getSA_SP_START_DTTM());
        agreementPointMap.setServiceAgreement(newSA);
        agreementPointMap.setServicePoint(newServicePoint);
        agreementPointMap.setSaSpId(customer.getSA_SP_ID());
        return agreementPointMap;
    }

    private EligibilityResult doEligibilityChecks(ProgramProfile profile, String meterType, ServiceAgreement sa, ServicePoint sp) {
        EligibilityResult result = new EligibilityResult();
        result.setErrors(new ArrayList<>());
        result.setChecks(new ArrayList<>());

        EligibilityChecker.checkEnrollerSources(profile, result);
        EligibilityChecker.checkSaStatuses(profile, result, sa);
        EligibilityChecker.checkServiceType(profile, result, sp);
        EligibilityChecker.checkCustomerType(profile, result, sa);
        EligibilityChecker.checkRateSchedules(profile, result, sa);
        EligibilityChecker.checkPremiseTypes(profile, result, sp);
        EligibilityChecker.checkThirdPartyDRP(profile, result, sa);
        EligibilityChecker.checkMedicalBaseline(profile, result, sa);
        EligibilityChecker.checkLifeSupport(profile, result, sa);
        EligibilityChecker.checkMeterTypeEligible(profile, sp.getMeter(), result, meterType);

        return result;
    }
}