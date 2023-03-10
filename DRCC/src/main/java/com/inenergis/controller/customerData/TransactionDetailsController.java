package com.inenergis.controller.customerData;


import com.inenergis.controller.carousel.ContractEntityCarousel;
import com.inenergis.controller.model.EnergyArrayDataBeanList;
import com.inenergis.entity.Note;
import com.inenergis.entity.contract.ContractEntity;
import com.inenergis.entity.contract.PointOfContact;
import com.inenergis.model.FakeTransaction;
import com.inenergis.model.FakeTransactionDetails;
import com.inenergis.service.ContractEntityService;
import com.inenergis.service.NoteService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.picketlink.Identity;
import org.picketlink.idm.model.basic.User;
import org.primefaces.event.RowEditEvent;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
@Getter
@Setter
public class TransactionDetailsController implements Serializable {

    private List<FakeTransactionDetails> details;

    @PostConstruct
    public void init() {
        FakeTransactionDetails detail = FakeTransactionDetails.builder().date("05/22/2017").seller("Producer 1").quantity("1.23 MW").price("$9.50").buyer("Utility 1")
                .quantity("1.23 MW").consumer("Utility 1").producer("Producer 1").actualProduction("1.11 MW").lastMeasuredDate("05/25/2017").status("Settled")
                .offerQuantity("1.23 MW").realQuantity("1.11 MW").total("$10,545").build();
        details = Arrays.asList(detail);
    }
}
