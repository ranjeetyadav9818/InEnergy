package com.inenergis.entity.program;

import com.inenergis.entity.HistoryTracked;
import com.inenergis.entity.IdentifiableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "PROGRAM_MULTI_PARTICIPATION")
@ToString(exclude = {"programs","optionId"})
@HistoryTracked(notCheck = {"profile"})
public class ProgramMultipleParticipation extends IdentifiableEntity{

    @ManyToOne
    @JoinColumn(name = "PROGRAM_PROFILE_ID")
    private ProgramProfile profile;

    @OneToMany(mappedBy = "participation", fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramMultipleParticipationProgram> programs;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_OPTION_ID")
    private ProgramOption option;

    @Transient
    private String optionId;

    @PostLoad
    public void onLoad(){
        if(getOption()!=null){
            optionId = getOption().getUuid();
        }
    }

    public boolean notFilledIn() {
       return programs.isEmpty();
    }
}