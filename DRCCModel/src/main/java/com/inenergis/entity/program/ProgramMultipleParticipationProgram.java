package com.inenergis.entity.program;

import com.inenergis.entity.HistoryTracked;
import com.inenergis.entity.IdentifiableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "PROGRAM_MULTI_PARTICIPATION_PROGRAM")
@HistoryTracked(notCheck = "participation")
public class ProgramMultipleParticipationProgram extends IdentifiableEntity{

    @ManyToOne
    @JoinColumn(name = "PROGRAM_ID")
    private Program program;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_MULTI_PARTICIPATION_ID")
    private ProgramMultipleParticipation participation;

    @Override
    public String toString() {
        try{
            return "Option: "+participation.getOption().getName()+" program: "+program.getName();
        }catch (NullPointerException e){
            return "notValid";
        }
    }
}