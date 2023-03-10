package com.inenergis.entity.program;

import com.inenergis.entity.HistoryTracked;
import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.EventDurationOption;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString(exclude = {"profile", "products"})
@Entity
@Table(name = "PROGRAM_OPTION")
@HistoryTracked(notCheck = {"profile"})
public class ProgramOption extends IdentifiableEntity {

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_PROFILE_ID")
    private ProgramProfile profile;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private EventDurationOption type;

    @OneToMany(mappedBy = "option", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramProduct> products;

    public boolean notFilledIn(boolean removeChild) {
        Collection emptyObjectsToRemoveL2 = new ArrayList();
        List<ProgramProduct> productsCopy = null;
        if (this.getProducts() != null) {
            productsCopy = new ArrayList<>(this.getProducts());
            for (ProgramProduct programProduct : this.getProducts()) {
                if (StringUtils.isEmpty(programProduct.getName())) {
                    emptyObjectsToRemoveL2.add(programProduct);
                }
            }
            productsCopy.removeAll(emptyObjectsToRemoveL2);
            if (removeChild){
                this.getProducts().removeAll(emptyObjectsToRemoveL2);
            }
        }
        return (StringUtils.isEmpty(this.getName()) && this.getType() == null
                && ((productsCopy!= null && productsCopy.isEmpty()) || productsCopy == null));
    }

}