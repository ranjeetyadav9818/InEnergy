package com.inenergis.controller.program;

import com.inenergis.entity.BaseServiceAgreement;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.program.RatePlan;
import com.inenergis.service.RatePlanService;
import com.inenergis.service.ServiceAgreementService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Named
@ViewScoped
@Getter
@Setter
public class RateBulkUploadController implements Serializable {

    public static final String THERE_WAS_AN_EXCEPTION_TRYING_TO_PROCESS_THE_FILE_0_PLEASE_CONTACT_YOUR_ADMINISTRATOR_ABOUT_THE_EXPECTED_FORMAT_OF_THE_FILE = "There was an exception trying to process the file {0}, please contact your administrator about the expected format of the file";
    @Inject
    RatePlanService ratePlanService;
    @Inject
    ServiceAgreementService serviceAgreementService;
    @Inject
    UIMessage uiMessage;

    Logger log =  LoggerFactory.getLogger(RateBulkUploadController.class);

    @PostConstruct
    public void init() {

    }
    public void submit(FileUploadEvent event) throws IOException {
        UploadedFile file = event.getFile();
        if (file == null) {
            uiMessage.addMessage("file is mandatory", FacesMessage.SEVERITY_ERROR);
        }else{
            try{
                CSVParser parser = CSVFormat.DEFAULT.parse(new InputStreamReader(file.getInputstream()));
                Iterator<CSVRecord> iterator = parser.iterator();
                List<BulkEnrollment> enrollments = new ArrayList<>();
                while(iterator.hasNext()){
                    CSVRecord record = iterator.next();
                    BulkEnrollment bulkEnrollment = new BulkEnrollment();
                    bulkEnrollment.setRatePlan(ratePlanService.getById(Long.parseLong(record.get(0))));
                    bulkEnrollment.setServiceAgreement(serviceAgreementService.getById(record.get(1)));
                    enrollments.add(bulkEnrollment);
                }
                ratePlanService.enroll(enrollments);
                uiMessage.addMessage("{0} records processed from file {1}", parser.getRecordNumber(), file.getFileName());
            } catch (Exception e){
                uiMessage.addMessage(THERE_WAS_AN_EXCEPTION_TRYING_TO_PROCESS_THE_FILE_0_PLEASE_CONTACT_YOUR_ADMINISTRATOR_ABOUT_THE_EXPECTED_FORMAT_OF_THE_FILE,
                        FacesMessage.SEVERITY_ERROR,file.getFileName());
                log.error(THERE_WAS_AN_EXCEPTION_TRYING_TO_PROCESS_THE_FILE_0_PLEASE_CONTACT_YOUR_ADMINISTRATOR_ABOUT_THE_EXPECTED_FORMAT_OF_THE_FILE,e);
            }

        }
    }

    @Getter
    @Setter
    public class BulkEnrollment{
        private RatePlan ratePlan;
        private BaseServiceAgreement serviceAgreement;
    }
}