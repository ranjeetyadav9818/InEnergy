package com.inenergis.controller.program;

import com.inenergis.controller.lazyDataModel.LazyServiceAgreementApplicationEnrollmentDataModel;
import com.inenergis.entity.Note;
import com.inenergis.entity.program.ProgramAggregator;
import com.inenergis.model.GeneralSelectableDataModel;
import com.inenergis.service.NoteService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.service.ProgramAggregatorService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.picketlink.Identity;
import org.picketlink.idm.model.basic.User;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
@Getter
@Setter
public class AggregatorList implements Serializable{
	
	@Inject
	ProgramAggregatorService aggregatorService;
	@Inject
	NoteService noteService;
	@Inject
	EntityManager entityManager;
	@Inject
	Identity identity;
	@Inject
	UIMessage uiMessage;
	 
	Logger log =  LoggerFactory.getLogger(AggregatorList.class);

	@PostConstruct
    public void init(){
        Long id = ParameterEncoderService.getDefaultDecodedParameterAsLong();
        if (id != null) {
            ProgramAggregator aggregator = aggregatorService.getAggregatorById(id);
            list = new GeneralSelectableDataModel(Arrays.asList(aggregator));
            selectedAggregator = aggregator;
            filterName = aggregator.getName();
        }
    }

	private ProgramAggregator selectedAggregator;
	private GeneralSelectableDataModel list;
	private String filterName;
	private String filterPOCName;
	private String filterPOCPhone;
	private boolean aggregatorEditable = false;
	private LazyServiceAgreementApplicationEnrollmentDataModel participantsLazyModel;
	private LazyServiceAgreementApplicationEnrollmentDataModel enrollmentsIPLazyModel;
	private LazyServiceAgreementApplicationEnrollmentDataModel unenrollmentsIPLazyModel;
	private Note newNote;
	private List<Note> notes;

	public void onAggregatorSelect(SelectEvent event){
		selectedAggregator = ((ProgramAggregator) event.getObject());
        generateFilters();
	}

    public void generateFilters() {
        Map<String, Object> preFilter = generatePreFilter();
        participantsLazyModel = new LazyServiceAgreementApplicationEnrollmentDataModel(entityManager, preFilter);
        preFilter = generatePreFilter();
        preFilter.put("enrollmentStatus","Enrollment In Progress");
        enrollmentsIPLazyModel = new LazyServiceAgreementApplicationEnrollmentDataModel(entityManager, preFilter);
        preFilter = generatePreFilter();
        preFilter.put("enrollmentStatus","Unenrollment In Progress");
        unenrollmentsIPLazyModel = new LazyServiceAgreementApplicationEnrollmentDataModel(entityManager, preFilter);
        notes = noteService.getNotes(ProgramAggregator.class.getName(),selectedAggregator.getId().toString());
    }

    private Map<String, Object> generatePreFilter() {
		Map<String, Object> preFilter = new HashMap<>();
		preFilter.put("aggregator.id",selectedAggregator.getId());
		return preFilter;
	}

	public void search(){
		list = new GeneralSelectableDataModel(aggregatorService.getAggregators(filterName,filterPOCName,filterPOCPhone));
		selectedAggregator = null;
	}

	public void modifyAggregator(){
		aggregatorEditable = true;
	}

	public void saveAggregator(){
        try {
            aggregatorService.saveAggregator(selectedAggregator);
            cancelAggregator();
            uiMessage.addMessage("Aggregator "+ selectedAggregator.getName() +" saved");
        } catch (Exception e) {
            uiMessage.addMessage("Error trying to save aggregator, please try again later and contact your administrator if you keep having this problem");
            log.warn("Error saving an aggregator", e);
        }
	}
	public void cancelAggregator(){
		aggregatorEditable = false;
	}

	public void createNote(){
		newNote = new Note();
	}

	public void cancelNote(){
		newNote = null;
	}

	public void saveNote(){
		newNote.setAuthor(((User) identity.getAccount()).getEmail());
		newNote.setCreationDate(new Date());
		newNote.setEntity(ProgramAggregator.class.getName());
		newNote.setEntityId(selectedAggregator.getId().toString());
		noteService.saveNote(newNote);
		notes.add(newNote);
		cancelNote();
	}
}