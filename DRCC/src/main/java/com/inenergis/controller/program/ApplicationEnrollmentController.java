package com.inenergis.controller.program;

import com.inenergis.commonServices.JMSUtilContract;
import com.inenergis.commonServices.Layer7PeakDemandHistoryServiceContract;
import com.inenergis.commonServices.ProgramServiceContract;
import com.inenergis.commonServices.WorkPlanServiceContract;
import com.inenergis.controller.carousel.GasServiceAgreementCarousel;
import com.inenergis.controller.carousel.LocationCarousel;
import com.inenergis.controller.carousel.ServiceAgreementCarousel;
import com.inenergis.controller.customerData.DataBeanList;
import com.inenergis.controller.customerData.GasDataBeanList;
import com.inenergis.controller.lazyDataModel.LazyDemandHistoryDataModel;
import com.inenergis.entity.AgreementPointMap;
import com.inenergis.entity.Layer7PeakDemandHistory;
import com.inenergis.entity.Note;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.genericEnum.ElectricalUnit;
import com.inenergis.entity.genericEnum.EnrolmentStatus;
import com.inenergis.entity.program.ProgramFirmServiceLevel;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollment;
import com.inenergis.entity.program.ProgramServiceAgreementEnrollmentAttribute;
import com.inenergis.entity.workflow.NotificationInstance;
import com.inenergis.entity.workflow.PlanInstance;
import com.inenergis.entity.workflow.ProgramPlanInstance;
import com.inenergis.entity.workflow.TaskInstance;
import com.inenergis.entity.workflow.WorkflowEngine;
import com.inenergis.exception.BusinessException;
import com.inenergis.service.MailService;
import com.inenergis.service.NoteService;
import com.inenergis.service.NotificationInstanceService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.FslRange;
import com.inenergis.util.PropertyAccessor;
import com.inenergis.util.UIMessage;
import com.inenergis.util.VelocityUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.picketlink.Identity;
import org.picketlink.idm.model.basic.User;
import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.inenergis.util.EnergyUtil.convertToWatts;

@Named
@ViewScoped
@Getter
@Setter
public class ApplicationEnrollmentController implements Serializable {

    @Inject
    EntityManager entityManager;
    @Inject
    private ProgramServiceContract programService;
    @Inject
    private LocationCarousel locationCarousel;
    @Inject
    private ServiceAgreementCarousel serviceAgreementCarousel;


    @Inject
    private GasServiceAgreementCarousel gasServiceAgreementCarousel;
    @Inject
    private Layer7PeakDemandHistoryServiceContract layer7PeakDemandHistoryService;
    @Inject
    private NoteService noteService;
    @Inject
    private WorkPlanServiceContract workPlanService;
    @Inject
    private NotificationInstanceService notificationInstanceService;
    @Inject
    private Identity identity;
    @Inject
    private UIMessage uiMessage;
    @Inject
    private MailService mailService;
    @Inject
    private PropertyAccessor propertyAccessor;
    @Inject
    private JMSUtilContract jmsUtil;

    private Logger log = LoggerFactory.getLogger(ApplicationEnrollmentController.class);

    private ProgramServiceAgreementEnrollment applicationEnrollment;
    private List<ProgramServiceAgreementEnrollmentAttribute> enrollmentAttributes;
    private List<DataBeanList> applicationDetails;
    private List<GasDataBeanList> gasApplicationDetails;
    private Note newNote;
    private List<Note> notes;
    private boolean summaryEditable;
    private String enrollmentStatusChange = null;
    private ProgramFirmServiceLevel newFSL;
    private FslRange fslRange;
    private String overridingReason;
    private boolean fslInRange = true;
    private List<ProgramPlanInstance> planInstances;
    LazyDemandHistoryDataModel lazyDemandHistoryDataModel;

    @PostConstruct
    public void init() {
        applicationEnrollment = programService.getServiceAgreementEnrollment(ParameterEncoderService.getDefaultDecodedParameterAsLong());
        enrollmentAttributes = programService.getAttributesFromEnrollment(applicationEnrollment);
        loadCarouselData();
        notes = noteService.getNotes(ProgramServiceAgreementEnrollment.class.getName(), applicationEnrollment.getId().toString());
        enrollmentStatusChange = applicationEnrollment.getEnrollmentStatus();
        fslRange = getSavedFslRange();
        planInstances = applicationEnrollment.getPlanInstances();
        lazyDemandHistoryDataModel = new LazyDemandHistoryDataModel(entityManager, generatePreFilter(applicationEnrollment.getServiceAgreement().getServiceAgreementId()));
    }

    private FslRange getSavedFslRange() {


        List<Layer7PeakDemandHistory> peakDemandHistoryList = layer7PeakDemandHistoryService.getBy(applicationEnrollment.getServiceAgreement(), applicationEnrollment.getProgram().getActiveProfile().getFlsTimeHorizon());


        List<Long> fslSummerOnPeakWattList = peakDemandHistoryList.stream()
                .map(Layer7PeakDemandHistory::getSummerOnPeakKw)
                .filter(StringUtils::isNotEmpty)
                .mapToDouble(Double::parseDouble)
                .filter(monthAverage -> monthAverage != 0)
                .boxed()
                .map(monthAverage -> convertToWatts(monthAverage, ElectricalUnit.KW))
                .collect(Collectors.toList());

        List<Long> fslPartialPeakWattList = peakDemandHistoryList.stream()
                .map(Layer7PeakDemandHistory::getWinterPartialPeakKw)
                .filter(StringUtils::isNotEmpty)
                .mapToDouble(Double::parseDouble)
                .filter(monthAverage -> monthAverage != 0)
                .boxed()
                .map(monthAverage -> convertToWatts(monthAverage, ElectricalUnit.KW))
                .collect(Collectors.toList());

        return new FslRange(applicationEnrollment.getProgram().getActiveProfile().getFslRules(), fslSummerOnPeakWattList, fslPartialPeakWattList);
    }

    private void loadCarouselData() {
        if (applicationEnrollment.getLastLocation() != null) {
            applicationDetails = locationCarousel.printLocationCarousel(applicationEnrollment.getLastLocation());
        } else {
            AgreementPointMap fakeApm = new AgreementPointMap();
            fakeApm.setServiceAgreement(applicationEnrollment.getServiceAgreement());//It is fake because it will show only SA info
            String commodity = applicationEnrollment.getServiceAgreement().getDecriminatorValue();
            if(commodity.equals("Gas")){
                gasApplicationDetails = gasServiceAgreementCarousel.printServiceAgreementCarousel(fakeApm);
            }
            else{
                applicationDetails = serviceAgreementCarousel.printServiceAgreementCarousel(fakeApm);
            }
        }
    }

    public void createNote() {
        newNote = new Note();
    }

    public void cancelNote() {
        newNote = null;
    }

    public void saveNote() {
        newNote.setAuthor(((User) identity.getAccount()).getEmail());
        newNote.setCreationDate(new Date());
        newNote.setEntity(ProgramServiceAgreementEnrollment.class.getName());
        newNote.setEntityId(applicationEnrollment.getId().toString());
        noteService.saveNote(newNote);
        notes.add(newNote);
        cancelNote();
    }

    public void modifySummary() {
        summaryEditable = true;
    }

    public void cancelSummary() {
        summaryEditable = false;
        enrollmentStatusChange = applicationEnrollment.getEnrollmentStatus();
    }

    public void saveSummary() {
        boolean correct = isEnrollmentInCorrectStatus();
        if (!correct) {
            return;
        }
        try {
            programService.updateApplicationEnrollment(applicationEnrollment, ((User) identity.getAccount()).getEmail());
            uiMessage.addMessage("Application {0} updated", applicationEnrollment.getId());
        } catch (JMSException e) {
            log.error("Exception connecting to JMS server", e);
            uiMessage.addMessage("Customer {0} was not unenrolled due to an error on the server, please try again later or contact your administrator",
                    FacesMessage.SEVERITY_ERROR, applicationEnrollment.getServiceAgreement().getAccount().getPerson().getBusinessName());
        } catch (BusinessException e) {
            log.error(e.getMessage());
            uiMessage.addMessage(e.getMessage(), FacesMessage.SEVERITY_ERROR, e.getBusinessInfo());
        }
        cancelSummary();
    }

    private boolean isEnrollmentInCorrectStatus() {
        if (enrollmentStatusChange != null && !enrollmentStatusChange.equals(applicationEnrollment.getEnrollmentStatus())) {
            if (enrollmentStatusChange.equals(EnrolmentStatus.UNENROLLED.getName()) || enrollmentStatusChange.equals(EnrolmentStatus.CANCELLED.getName())) {

                if (applicationEnrollment.getEffectiveEndDate() == null) {
                    uiMessage.addMessage("Please provide Effective End Date for this status change", FacesMessage.SEVERITY_ERROR);
                    return false;
                }

                if (StringUtils.isEmpty(applicationEnrollment.getUnenrollReason())) {
                    uiMessage.addMessage("Please select unenroll reason for this status change", FacesMessage.SEVERITY_ERROR);
                    return false;
                }

            } else if (enrollmentStatusChange.equals(EnrolmentStatus.ENROLLED.getName())) {
                if (applicationEnrollment.getEffectiveStartDate() == null) {
                    applicationEnrollment.setEffectiveStartDate(new Date());
                }
            } else if (enrollmentStatusChange.equals(EnrolmentStatus.REINSTATE.getName())) {
                applicationEnrollment.setEffectiveStartDate(applicationEnrollment.getOriginalEffectiveStartDate());
                if (applicationEnrollment.getEffectiveEndDate() != null || StringUtils.isNotEmpty(applicationEnrollment.getUnenrollReason())) {
                    uiMessage.addMessage("If you want to reinstate, clear the fields Effective End Date and Unenrollment reason", FacesMessage.SEVERITY_ERROR);
                    return false;
                }
            }
            applicationEnrollment.setEnrollmentStatus(enrollmentStatusChange);
        }
        if (applicationEnrollment.getEffectiveEndDate() != null || StringUtils.isNotEmpty(applicationEnrollment.getUnenrollReason())) {
            if (!applicationEnrollment.getEnrollmentStatus().equals(EnrolmentStatus.UNENROLLED.getName()) && !applicationEnrollment.getEnrollmentStatus().equals(EnrolmentStatus.CANCELLED.getName())) {
                uiMessage.addMessage("If either Effective End Date or Unenrollment reason are provided, enrollment status has to be Unenrolled", FacesMessage.SEVERITY_ERROR);
                return false;
            }
        }
        return true;
    }

    public void addNewFSL() {
        newFSL = new ProgramFirmServiceLevel();
    }

    public void cancelNewFSL() {
        newFSL = null;
    }

    public void saveNewFSL() {
        final ProgramFirmServiceLevel lastFSL = applicationEnrollment.getLastFSL();
        if (lastFSL != null) {
            lastFSL.setEffectiveEndDate(newFSL.getEffectiveStartDate());
            lastFSL.setLastUpdated(new Date());
            lastFSL.setLastUpdatedBy(((User) identity.getAccount()).getEmail());
        }
        newFSL.setApplication(applicationEnrollment);
        newFSL.setLastUpdated(new Date());
        newFSL.setLastUpdatedBy(((User) identity.getAccount()).getEmail());
        if (applicationEnrollment.getFsls() == null) {
            applicationEnrollment.setFsls(new ArrayList<>());
        }

        if (!fslRange.isInRange(newFSL) && StringUtils.isEmpty(overridingReason)) {
            fslInRange = false;
            return;
        }

        applicationEnrollment.getFsls().add(newFSL);

        if (StringUtils.isNotEmpty(overridingReason)) {
            applicationEnrollment.getSnapshots().add(newFSL.generateSnapshot(fslInRange, overridingReason));
        }

        try {
            programService.updateApplicationEnrollment(applicationEnrollment, ((User) identity.getAccount()).getEmail());
        } catch (JMSException e) {
            log.error("Exception connecting to JMS server, but updating FSLs should never happen", e);
            uiMessage.addMessage("Customer {0} was not unenrolled due to an error on the server, please try again later or contact your administrator",
                    FacesMessage.SEVERITY_ERROR, applicationEnrollment.getServiceAgreement().getAccount().getPerson().getBusinessName());
        } catch (BusinessException e) {
            log.error(e.getMessage());
            uiMessage.addMessage(e.getMessage(), FacesMessage.SEVERITY_ERROR, e.getBusinessInfo());
        }
        cancelNewFSL();
    }

    public List<String> getAvailableEnrollmentStatuses() {
        List<String> statuses = new ArrayList<>();
        statuses.add(applicationEnrollment.getEnrollmentStatus());
        try {
            EnrolmentStatus enrollmentStatus = EnrolmentStatus.getByName(applicationEnrollment.getEnrollmentStatus());
            switch (enrollmentStatus) {
                case IN_PROGRESS:
                    statuses.add(EnrolmentStatus.CANCELLED.getName());
                    statuses.add(EnrolmentStatus.ENROLLED.getName());
                    break;
                case ENROLLED:
                    statuses.add(EnrolmentStatus.UNENROLLED.getName());
                    break;
                case UNENROLLED:
                    statuses.add(EnrolmentStatus.REINSTATE.getName());
                    break;
            }
        } catch (Exception e) {
        }
        return statuses;
    }

    public String getWorkflowStatuses() {
        return applicationEnrollment.getPlanInstances().stream()
                .map(plan -> plan.getWorkPlan().getType().getName() + " is " + plan.getStatus().getText())
                .collect(Collectors.joining(", "));
    }

    public void onWorkflowEdit(RowEditEvent event) {
        TaskInstance task = (TaskInstance) event.getObject();
        task.setLastUpdated(new Date());
        (new WorkflowEngine()).manageTaskInstanceTransitions(task.getPlanInstance(), task, new VelocityUtil(), propertyAccessor.getProperties(),jmsUtil);
        workPlanService.savePlanInstance(task.getPlanInstance());
        applicationEnrollment = programService.getServiceAgreementEnrollment(applicationEnrollment.getId());
        planInstances = applicationEnrollment.getPlanInstances();
    }

    public void onWorkflowCancel(RowEditEvent event) {

    }

    public void pause(PlanInstance planInstance) {
        final List<NotificationInstance> notifications = (new WorkflowEngine()).pause(planInstance);
        workPlanService.savePlanInstance(planInstance);
        if (!CollectionUtils.isEmpty(notifications)) {
            notificationInstanceService.saveAll(notifications);
        }
        applicationEnrollment = programService.getServiceAgreementEnrollment(applicationEnrollment.getId());
        planInstances = applicationEnrollment.getPlanInstances();
    }

    public void resume(PlanInstance planInstance) {
        final List<NotificationInstance> notifications = (new WorkflowEngine()).resume(planInstance);
        workPlanService.savePlanInstance(planInstance);
        if (!CollectionUtils.isEmpty(notifications)) {
            notificationInstanceService.saveAll(notifications);
        }
        applicationEnrollment = programService.getServiceAgreementEnrollment(applicationEnrollment.getId());
        planInstances = applicationEnrollment.getPlanInstances();
    }

    private Map<String, Object> generatePreFilter(String serviceAgreementId) {
        Map<String, Object> prefFilters = new HashMap<>();
        prefFilters.put("serviceAgreement.serviceAgreementId", serviceAgreementId);
        return prefFilters;
    }

}