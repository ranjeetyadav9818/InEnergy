package com.inenergis.controller.program;

import com.inenergis.commonServices.ProgramServiceContract;
import com.inenergis.controller.customerData.DataBean;
import com.inenergis.controller.customerData.DataBeanList;
import com.inenergis.controller.lazyDataModel.LazyServiceAgreementApplicationEnrollmentDataModel;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.entity.genericEnum.ComodityType;
import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.inenergis.util.ConstantsProviderModel.KW_PRECISION;

@Named
@ViewScoped
@Getter
@Setter
public class ProgramManagementController implements Serializable {

    @Inject
    ProgramServiceContract programService;
    @Inject
    EntityManager entityManager;
    @Inject
    UIMessage uiMessage;

    Logger log = LoggerFactory.getLogger(ProgramManagementController.class);

    private Program program;
    private ProgramProfile currentProfile;
    private List<DataBeanList> programDetails = new ArrayList<>();
    private LazyServiceAgreementApplicationEnrollmentDataModel participantsLazyModel;
    private LazyServiceAgreementApplicationEnrollmentDataModel enrollmentsIPLazyModel;

    @PostConstruct
    public void init() {
        program = programService.getProgram(Long.valueOf(ParameterEncoderService.getDefaultDecodedParameterAsLong()));
        if (program != null) {
            Map<String, Object> preFilter = generatePreFilter();
            preFilter.put("enrollmentStatus", Arrays.asList("Enrolled", "Unenrolled"));
            participantsLazyModel = new LazyServiceAgreementApplicationEnrollmentDataModel(entityManager, preFilter);

            preFilter = generatePreFilter();
            preFilter.put("enrollmentStatus", "In Progress");
            enrollmentsIPLazyModel = new LazyServiceAgreementApplicationEnrollmentDataModel(entityManager, preFilter);

            currentProfile = program.getActiveProfile();
            if (currentProfile != null) {
                loadCarouselData();
                return;
            }
        }
        throw new IllegalArgumentException("This program has no active profile");
    }

    private Map<String, Object> generatePreFilter() {
        Map<String, Object> preFilter = new HashMap<>();
        preFilter.put("program.id", program.getId());
        return preFilter;
    }

    private void loadCarouselData() {
        List<DataBean> cBeans = new ArrayList<>();
        cBeans.add(new DataBean("Program Name: ", program.getName()));
        cBeans.add(new DataBean("Program Status: ", program.isActive() ? "Active" : "Inactive"));
        cBeans.add(new DataBean("Program Options: ", CollectionUtils.isEmpty(currentProfile.getOptions()) ? "No" : "Yes"));
        cBeans.add(new DataBean("Commodity Type: ", program.getCommodity().toString()));

        DataBeanList dataBeanList = new DataBeanList("Program Overview", "icon-list-alt", cBeans);
        programDetails.add(dataBeanList);

        cBeans = new ArrayList<>();
        cBeans.add(new DataBean("Active SA's: ", programService.getProgramNumberOfActiveSAs(program).toString()));
        try {
            if(program.getCommodity().equals(ComodityType.Electricity)){
                Long sum = programService.getActiveSAs(program).stream()
                        .mapToLong(e -> Math.max(0, e.getAverageSummerOnPeakWatt() - e.getCurrentFSL().getValue().longValue() * KW_PRECISION)).sum();
                cBeans.add(new DataBean("Estimated capacity: ", "" + (new BigDecimal(sum / KW_PRECISION)).longValue() + " kW"));
            }
            else if(program.getCommodity().equals(ComodityType.Gas)){
                Long sum = programService.getActiveSAs(program).stream()
                        .mapToLong(e -> Math.max(0, e.getAverageSummerOnPeakWatt() - e.getCurrentFSL().getValue().longValue() * KW_PRECISION)).sum();
                cBeans.add(new DataBean("Estimated capacity: ", "" + (new BigDecimal(sum / KW_PRECISION)).longValue() + "Therms"));
            }
        } catch (Exception e) {
            cBeans.add(new DataBean("Estimated capacity: ", "Error on calculation"));
        }
        if(program.isCapActive()){
            if(program.getCommodity().equals(ComodityType.Electricity)){
                cBeans.add(new DataBean("Cap: ", program.getCapNumber().longValue() + " " + program.getCapUnit().getLabel()));
            }
            else if(program.getCommodity().equals(ComodityType.Gas)){
                cBeans.add(new DataBean("Cap: ", program.getCapNumber().longValue() + " " + program.getGasCapUnit().getName()));
            }
        }

        dataBeanList = new DataBeanList("Program Dashboard", "icon-data-science-inv", cBeans);
        programDetails.add(dataBeanList);

        cBeans = new ArrayList<>();
        cBeans.add(new DataBean("Equipment required: ", CollectionUtils.isEmpty(currentProfile.getEquipments()) ? "No" : "Yes"));
        cBeans.add(new DataBean("Customer notifications: ", CollectionUtils.isEmpty(currentProfile.getEnrollmentNotifications()) ? "No" : "Yes"));
        dataBeanList = new DataBeanList("Dispatch Overview", "icon-at-1", cBeans);
        programDetails.add(dataBeanList);
    }
}