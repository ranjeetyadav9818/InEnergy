package com.inenergis.entity.contract;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.genericEnum.POCType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "POINT_OF_CONTACT")
@Getter
@Setter
@NoArgsConstructor
public class PointOfContact extends IdentifiableEntity {

    @Column(name = "POC_TYPE")
    @Enumerated(EnumType.STRING)
    POCType pocType;

    @Column(name = "NAME")
    private String name;
    @Column(name = "TITLE")
    private String title;

    @ManyToOne
    @JoinColumn(name = "CONTRACT_ENTITY_ID")
    private ContractEntity contractEntity;

    @OneToMany(mappedBy = "pointOfContact", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<POCEmail> pocEmails;

    @OneToMany(mappedBy = "pointOfContact", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<POCPhone> pocPhones;

    public PointOfContact(ContractEntity contractEntity) {
        super();
        this.contractEntity = contractEntity;
        this.pocPhones =  new ArrayList<>();
        this.pocEmails = new ArrayList<>();
    }

    public String getAllMails(){
        if (CollectionUtils.isEmpty(pocEmails)) {
            return null;
        }
        return String.join(" | ",pocEmails.stream().map(poc -> poc.getEmail()).collect(Collectors.toList()));
    }
    public String getAllPhones(){
        if (CollectionUtils.isEmpty(pocEmails)) {
            return null;
        }
        return String.join(" | ",pocPhones.stream().map(poc -> poc.getNumber()).collect(Collectors.toList()));
    }
}
