package com.inenergis.entity.program;


import com.inenergis.entity.IdentifiableEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@EqualsAndHashCode(of = "customerType")
@Entity
@Table(name = "PROGRAM_ELIG_CUSTOMER_TYPE")
public class ProgramEligibilityCustomerType extends IdentifiableEntity {

    @Column(name = "CUSTOMER_TYPE")
    @Enumerated(EnumType.STRING)
    private CustomerType customerType;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_PROFILE_ID")
    private ProgramProfile profile;

    @Override
    public String toString() {
        return "customerType: " + customerType;
    }

    public enum CustomerType {

        COM_IND("COM/IND"),
        AGR("AGR"),
        ST_GOV("ST_GOV"),
        RES("RES"),
        NMDL("NMDL"),
        PYMT_MGT("PYMT_MGT"),
        TOU_NEGY("TOU_NEGY");

        CustomerType(String text) {
            this.text = text;
        }

        private String text;

        public String getText() {
            return text;
        }

        public static CustomerType getByText(String text) {
            CustomerType[] all = values();
            for (CustomerType customerType : all) {
                if (customerType.text.equalsIgnoreCase(text)) {
                    return customerType;
                }
            }
            return null;
        }

    }
}
