package com.inenergis.controller.program;

import com.inenergis.controller.lazyDataModel.LazyRateCodeDataModel;
import com.inenergis.entity.genericEnum.ActivityStatus;
import com.inenergis.entity.genericEnum.RateCodeSector;
import com.inenergis.entity.program.rateProgram.RateCode;
import com.inenergis.entity.program.rateProgram.RateCodeSectors;
import com.inenergis.service.RateCodeService;
import com.inenergis.util.PickListElement;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.primefaces.model.DualListModel;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
@ViewScoped
@Getter
@Setter
public class RateCodeLibraryController implements Serializable {
    @Inject
    EntityManager entityManager;

    @Inject
    private UIMessage uiMessage;

    @Inject
    RateCodeService rateCodeService;

    private Logger log = LoggerFactory.getLogger(RateCodeLibraryController.class);

    private LazyRateCodeDataModel lazyRateCodeModel;

    private boolean renderNewCode = false;

    private DualListModel<PickListElement> sectorPickList = new DualListModel<>();
    private List<PickListElement> sectorList = new ArrayList<>();

    private RateCode rateCode;

    public void addCode() {
        rateCode = new RateCode();
        initPickList();
        renderNewCode = true;
    }

    public void cancel() {
        rateCode = null;
        renderNewCode = false;
    }

    public void save() {
        rateCode.setRateStatus(ActivityStatus.ACTIVE);
        if (CollectionUtils.isNotEmpty(sectorPickList.getTarget())) {
            final List<RateCodeSectors> sectors = sectorPickList.getTarget().stream().map(s -> {
                final String id = s.getCode();
                final RateCodeSector sector = RateCodeSector.valueOf(id);
                final RateCodeSectors rateCodeSectors = new RateCodeSectors();
                rateCodeSectors.setRateCode(rateCode);
                rateCodeSectors.setSector(sector);
                return rateCodeSectors;
            }).collect(Collectors.toList());
            rateCode.setSectors(sectors);
        } else {
            rateCode.setSectors(new ArrayList<>());
        }
        if (rateCodeService.searchOthersWithSameCodeAndSector(rateCode) == 0) {
            rateCode = rateCodeService.update(rateCode);
            renderNewCode = false;
        } else {
            uiMessage.addMessage("A Rate Code with the same code and some of the same sectors exists.",
                    FacesMessage.SEVERITY_ERROR);
        }
    }

    public void activate(RateCode code) {
        code.setRateStatus(ActivityStatus.ACTIVE);
        rateCodeService.update(code);
    }

    public void inactivate(RateCode code) {
        code.setRateStatus(ActivityStatus.INACTIVE);
        rateCodeService.update(code);
    }

    public void modify(RateCode code) {
        rateCode = code;
        initPickList();
        final List<PickListElement> newTarget = code.getSectors().stream().map(s -> new PickListElement(s.getSector().getName(), s.getSector().name())).collect(Collectors.toList());
        final List<PickListElement> target = sectorPickList.getTarget();
        final List<PickListElement> source = sectorPickList.getSource();
        target.addAll(newTarget);
        target.forEach(s -> {
            if (source.contains(s)) {
                source.remove(s);
            }
        });
        renderNewCode = true;
    }

    @PostConstruct
    public void init() {
        final Map<String, Object> prefFilters = new HashMap();
        lazyRateCodeModel = new LazyRateCodeDataModel(entityManager, prefFilters);
        initPickList();

    }

    private void initPickList() {
        sectorPickList = new DualListModel<>();
        sectorList = new ArrayList<>();
        final List<PickListElement> source = Arrays.stream(RateCodeSector.values()).map(s -> new PickListElement(s.getName(), s.name())).collect(Collectors.toList());
        sectorPickList.getSource().addAll(source);
        sectorList.addAll(source);
    }
}
