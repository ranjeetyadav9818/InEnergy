package com.inenergis.model.adapter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inenergis.entity.program.Program;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egamas on 25/09/2017.
 */
@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class ProgramAdapter {
    private Long id;
    private String name;
    private boolean active;
    private String programType;
    private String enrollmentStatus;
    List<String> messages;

    @JsonProperty("hasError")
    public boolean hasError(){
        return CollectionUtils.isNotEmpty(messages);
    }

    public ProgramAdapter() {
        this.messages = new ArrayList<>();
    }

    public static ProgramAdapter build (Program program){
        return builder() .id(program.getId())
                .name(program.getName())
                .active(program.isActive())
                .programType(program.getProgramType() != null ? program.getProgramType().getLabel() : null)
                .messages(new ArrayList<>())
                .build();
    }
}