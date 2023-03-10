package com.inenergis.model;

import com.inenergis.entity.program.Program;
import com.inenergis.entity.program.ProgramOption;
import com.inenergis.entity.program.ProgramProfile;
import com.inenergis.entity.program.SublapProgramMapping;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Antonio on 18/08/2017.
 */
public final class ElasticProgramConverter {

    private ElasticProgramConverter() {
    }

    public static ElasticProgram convert(Program program) {

        final List<String> optionNames = new ArrayList();
        final List<String> drmsProgramIds = new ArrayList();
        final ProgramProfile activeProfile = program.getActiveProfile();
        String isoProductMapping = null;

        if (activeProfile != null) {
            optionNames.addAll(CollectionUtils.isEmpty(activeProfile.getOptions()) ? Collections.emptyList() :
                    activeProfile.getOptions().stream().map(ProgramOption::getName).distinct().collect(Collectors.toList()));
            drmsProgramIds.addAll(CollectionUtils.isEmpty(activeProfile.getSublapProgramMappings()) ? Collections.emptyList() :
                    activeProfile.getSublapProgramMappings().stream().map(SublapProgramMapping::getDrmsProgramId).distinct().collect(Collectors.toList()));
            isoProductMapping = activeProfile.getWholesaleIsoProduct() == null ? null : activeProfile.getWholesaleIsoProduct().getName();
        }

        final List<String> profileNames = CollectionUtils.isEmpty(program.getProfiles()) ? Collections.emptyList() :
                program.getProfiles().stream().map(ProgramProfile::getName).distinct().collect(Collectors.toList());

        return ElasticProgram.builder()
                .name(program.getName())
                .id(program.getId())
                .profileNames(profileNames)
                .optionNames(optionNames)
                .isoProductMapping(isoProductMapping)
                .drmsProgramIds(drmsProgramIds).build();
    }

}
