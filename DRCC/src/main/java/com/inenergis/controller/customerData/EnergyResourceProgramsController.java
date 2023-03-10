package com.inenergis.controller.customerData;

import com.inenergis.commonServices.ProgramServiceContract;
import com.inenergis.controller.lazyDataModel.LazyProgramModel;
import com.inenergis.entity.program.Program;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

@Named
@ViewScoped
@Getter
@Setter
public class EnergyResourceProgramsController implements Serializable {

    Logger log =  LoggerFactory.getLogger(EnergyResourceProgramsController.class);

    @Inject
    private EntityManager entityManager;

    @Inject
    private UIMessage uiMessage;

    @Inject
    private ProgramServiceContract programService;

    private LazyProgramModel programs;

    private boolean newProgram = false;

    private Program program;

    @PostConstruct
    public void init() {
        programs = new LazyProgramModel(entityManager, new HashMap<>());
    }

    public void createNewProgram() {
        newProgram = true;
        program = new Program();
    }

    public void cancelNewProgram() {
        newProgram = false;
        program = null;
    }

    public void saveNewProgram() {
        if (!program.isCapNumberValid()) {
            uiMessage.addMessage("Cap number must be an integer", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            programService.saveProgram(program);
            uiMessage.addMessage("Program {0} saved", program.getName());
            newProgram = false;
        } catch (IOException e) {
            uiMessage.addMessage("Error trying to save program, please try again later and contact your administrator if you keep having this problem");
            log.warn("Error saving a program", e);
        }
    }
}
