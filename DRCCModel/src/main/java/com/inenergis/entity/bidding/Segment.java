package com.inenergis.entity.bidding;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.program.Program;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "SEGMENT")
public class Segment extends IdentifiableEntity {

    @Column(name = "STATUS")
    String status;
    @Column(name = "NAME")
    String name;
    @ManyToOne
    @JoinColumn(name = "BID_ID")
    Bid bid;
    @ManyToOne
    @JoinColumn(name = "PROGRAM_ID")
    Program program;

    @Column(name = "CAPACITY_HE1")
    Long capacityHe1;
    @Column(name = "PRICE_HE1")
    Long priceHe1;
    @Column(name = "CAPACITY_HE2")
    Long capacityHe2;
    @Column(name = "PRICE_HE2")
    Long priceHe2;
    @Column(name = "CAPACITY_HE3")
    Long capacityHe3;
    @Column(name = "PRICE_HE3")
    Long priceHe3;
    @Column(name = "CAPACITY_HE4")
    Long capacityHe4;
    @Column(name = "PRICE_HE4")
    Long priceHe4;
    @Column(name = "CAPACITY_HE5")
    Long capacityHe5;
    @Column(name = "PRICE_HE5")
    Long priceHe5;
    @Column(name = "CAPACITY_HE6")
    Long capacityHe6;
    @Column(name = "PRICE_HE6")
    Long priceHe6;
    @Column(name = "CAPACITY_HE7")
    Long capacityHe7;
    @Column(name = "PRICE_HE7")
    Long priceHe7;
    @Column(name = "CAPACITY_HE8")
    Long capacityHe8;
    @Column(name = "PRICE_HE8")
    Long priceHe8;
    @Column(name = "CAPACITY_HE9")
    Long capacityHe9;
    @Column(name = "PRICE_HE9")
    Long priceHe9;
    @Column(name = "CAPACITY_HE10")
    Long capacityHe10;
    @Column(name = "PRICE_HE10")
    Long priceHe10;
    @Column(name = "CAPACITY_HE11")
    Long capacityHe11;
    @Column(name = "PRICE_HE11")
    Long priceHe11;
    @Column(name = "CAPACITY_HE12")
    Long capacityHe12;
    @Column(name = "PRICE_HE12")
    Long priceHe12;
    @Column(name = "CAPACITY_HE13")
    Long capacityHe13;
    @Column(name = "PRICE_HE13")
    Long priceHe13;
    @Column(name = "CAPACITY_HE14")
    Long capacityHe14;
    @Column(name = "PRICE_HE14")
    Long priceHe14;
    @Column(name = "CAPACITY_HE15")
    Long capacityHe15;
    @Column(name = "PRICE_HE15")
    Long priceHe15;
    @Column(name = "CAPACITY_HE16")
    Long capacityHe16;
    @Column(name = "PRICE_HE16")
    Long priceHe16;
    @Column(name = "CAPACITY_HE17")
    Long capacityHe17;
    @Column(name = "PRICE_HE17")
    Long priceHe17;
    @Column(name = "CAPACITY_HE18")
    Long capacityHe18;
    @Column(name = "PRICE_HE18")
    Long priceHe18;
    @Column(name = "CAPACITY_HE19")
    Long capacityHe19;
    @Column(name = "PRICE_HE19")
    Long priceHe19;
    @Column(name = "CAPACITY_HE20")
    Long capacityHe20;
    @Column(name = "PRICE_HE20")
    Long priceHe20;
    @Column(name = "CAPACITY_HE21")
    Long capacityHe21;
    @Column(name = "PRICE_HE21")
    Long priceHe21;
    @Column(name = "CAPACITY_HE22")
    Long capacityHe22;
    @Column(name = "PRICE_HE22")
    Long priceHe22;
    @Column(name = "CAPACITY_HE23")
    Long capacityHe23;
    @Column(name = "PRICE_HE23")
    Long priceHe23;
    @Column(name = "CAPACITY_HE24")
    Long capacityHe24;
    @Column(name = "PRICE_HE24")
    Long priceHe24;

    @Column(name = "STATUS_HE1")
    String statusHe1;
    @Column(name = "ISO_MESSAGE_HE1")
    String isoMessageHe1;
    @Column(name = "STATUS_HE2")
    String statusHe2;
    @Column(name = "ISO_MESSAGE_HE2")
    String isoMessageHe2;
    @Column(name = "STATUS_HE3")
    String statusHe3;
    @Column(name = "ISO_MESSAGE_HE3")
    String isoMessageHe3;
    @Column(name = "STATUS_HE4")
    String statusHe4;
    @Column(name = "ISO_MESSAGE_HE4")
    String isoMessageHe4;
    @Column(name = "STATUS_HE5")
    String statusHe5;
    @Column(name = "ISO_MESSAGE_HE5")
    String isoMessageHe5;
    @Column(name = "STATUS_HE6")
    String statusHe6;
    @Column(name = "ISO_MESSAGE_HE6")
    String isoMessageHe6;
    @Column(name = "STATUS_HE7")
    String statusHe7;
    @Column(name = "ISO_MESSAGE_HE7")
    String isoMessageHe7;
    @Column(name = "STATUS_HE8")
    String statusHe8;
    @Column(name = "ISO_MESSAGE_HE8")
    String isoMessageHe8;
    @Column(name = "STATUS_HE9")
    String statusHe9;
    @Column(name = "ISO_MESSAGE_HE9")
    String isoMessageHe9;
    @Column(name = "STATUS_HE10")
    String statusHe10;
    @Column(name = "ISO_MESSAGE_HE10")
    String isoMessageHe10;
    @Column(name = "STATUS_HE11")
    String statusHe11;
    @Column(name = "ISO_MESSAGE_HE11")
    String isoMessageHe11;
    @Column(name = "STATUS_HE12")
    String statusHe12;
    @Column(name = "ISO_MESSAGE_HE12")
    String isoMessageHe12;
    @Column(name = "STATUS_HE13")
    String statusHe13;
    @Column(name = "ISO_MESSAGE_HE13")
    String isoMessageHe13;
    @Column(name = "STATUS_HE14")
    String statusHe14;
    @Column(name = "ISO_MESSAGE_HE14")
    String isoMessageHe14;
    @Column(name = "STATUS_HE15")
    String statusHe15;
    @Column(name = "ISO_MESSAGE_HE15")
    String isoMessageHe15;
    @Column(name = "STATUS_HE16")
    String statusHe16;
    @Column(name = "ISO_MESSAGE_HE16")
    String isoMessageHe16;
    @Column(name = "STATUS_HE17")
    String statusHe17;
    @Column(name = "ISO_MESSAGE_HE17")
    String isoMessageHe17;
    @Column(name = "STATUS_HE18")
    String statusHe18;
    @Column(name = "ISO_MESSAGE_HE18")
    String isoMessageHe18;
    @Column(name = "STATUS_HE19")
    String statusHe19;
    @Column(name = "ISO_MESSAGE_HE19")
    String isoMessageHe19;
    @Column(name = "STATUS_HE20")
    String statusHe20;
    @Column(name = "ISO_MESSAGE_HE20")
    String isoMessageHe20;
    @Column(name = "STATUS_HE21")
    String statusHe21;
    @Column(name = "ISO_MESSAGE_HE21")
    String isoMessageHe21;
    @Column(name = "STATUS_HE22")
    String statusHe22;
    @Column(name = "ISO_MESSAGE_HE22")
    String isoMessageHe22;
    @Column(name = "STATUS_HE23")
    String statusHe23;
    @Column(name = "ISO_MESSAGE_HE23")
    String isoMessageHe23;
    @Column(name = "STATUS_HE24")
    String statusHe24;
    @Column(name = "ISO_MESSAGE_HE24")
    String isoMessageHe24;

    @Transient
    Boolean isEditable;

    public List<Long> getPricesAsList() {
        return Arrays.asList(
                priceHe1,
                priceHe2,
                priceHe3,
                priceHe4,
                priceHe5,
                priceHe6,
                priceHe7,
                priceHe8,
                priceHe9,
                priceHe10,
                priceHe11,
                priceHe12,
                priceHe13,
                priceHe14,
                priceHe15,
                priceHe16,
                priceHe17,
                priceHe18,
                priceHe19,
                priceHe20,
                priceHe21,
                priceHe22,
                priceHe23,
                priceHe24
        );
    }

    public List<Long> getCapacitiesAsList() {
        return Arrays.asList(
                capacityHe1,
                capacityHe2,
                capacityHe3,
                capacityHe4,
                capacityHe5,
                capacityHe6,
                capacityHe7,
                capacityHe8,
                capacityHe9,
                capacityHe10,
                capacityHe11,
                capacityHe12,
                capacityHe13,
                capacityHe14,
                capacityHe15,
                capacityHe16,
                capacityHe17,
                capacityHe18,
                capacityHe19,
                capacityHe20,
                capacityHe21,
                capacityHe22,
                capacityHe23,
                capacityHe24
        );
    }


    @Transient
    private long forecastedCapacityHe1 = 0L;
    @Transient
    private long forecastedCapacityHe2 = 0L;
    @Transient
    private long forecastedCapacityHe3 = 0L;
    @Transient
    private long forecastedCapacityHe4 = 0L;
    @Transient
    private long forecastedCapacityHe5 = 0L;
    @Transient
    private long forecastedCapacityHe6 = 0L;
    @Transient
    private long forecastedCapacityHe7 = 0L;
    @Transient
    private long forecastedCapacityHe8 = 0L;
    @Transient
    private long forecastedCapacityHe9 = 0L;
    @Transient
    private long forecastedCapacityHe10 = 0L;
    @Transient
    private long forecastedCapacityHe11 = 0L;
    @Transient
    private long forecastedCapacityHe12 = 0L;
    @Transient
    private long forecastedCapacityHe13 = 0L;
    @Transient
    private long forecastedCapacityHe14 = 0L;
    @Transient
    private long forecastedCapacityHe15 = 0L;
    @Transient
    private long forecastedCapacityHe16 = 0L;
    @Transient
    private long forecastedCapacityHe17 = 0L;
    @Transient
    private long forecastedCapacityHe18 = 0L;
    @Transient
    private long forecastedCapacityHe19 = 0L;
    @Transient
    private long forecastedCapacityHe20 = 0L;
    @Transient
    private long forecastedCapacityHe21 = 0L;
    @Transient
    private long forecastedCapacityHe22 = 0L;
    @Transient
    private long forecastedCapacityHe23 = 0L;
    @Transient
    private long forecastedCapacityHe24 = 0L;

    @Transient
    private long safetyFactorHe1 = 0L;
    @Transient
    private long safetyFactorHe2 = 0L;
    @Transient
    private long safetyFactorHe3 = 0L;
    @Transient
    private long safetyFactorHe4 = 0L;
    @Transient
    private long safetyFactorHe5 = 0L;
    @Transient
    private long safetyFactorHe6 = 0L;
    @Transient
    private long safetyFactorHe7 = 0L;
    @Transient
    private long safetyFactorHe8 = 0L;
    @Transient
    private long safetyFactorHe9 = 0L;
    @Transient
    private long safetyFactorHe10 = 0L;
    @Transient
    private long safetyFactorHe11 = 0L;
    @Transient
    private long safetyFactorHe12 = 0L;
    @Transient
    private long safetyFactorHe13 = 0L;
    @Transient
    private long safetyFactorHe14 = 0L;
    @Transient
    private long safetyFactorHe15 = 0L;
    @Transient
    private long safetyFactorHe16 = 0L;
    @Transient
    private long safetyFactorHe17 = 0L;
    @Transient
    private long safetyFactorHe18 = 0L;
    @Transient
    private long safetyFactorHe19 = 0L;
    @Transient
    private long safetyFactorHe20 = 0L;
    @Transient
    private long safetyFactorHe21 = 0L;
    @Transient
    private long safetyFactorHe22 = 0L;
    @Transient
    private long safetyFactorHe23 = 0L;
    @Transient
    private long safetyFactorHe24 = 0L;

    public List<Long> getForecastedCapacitiessAsList() {
        return Arrays.asList(
                forecastedCapacityHe1,
                forecastedCapacityHe2,
                forecastedCapacityHe3,
                forecastedCapacityHe4,
                forecastedCapacityHe5,
                forecastedCapacityHe6,
                forecastedCapacityHe7,
                forecastedCapacityHe8,
                forecastedCapacityHe9,
                forecastedCapacityHe10,
                forecastedCapacityHe11,
                forecastedCapacityHe12,
                forecastedCapacityHe13,
                forecastedCapacityHe14,
                forecastedCapacityHe15,
                forecastedCapacityHe16,
                forecastedCapacityHe17,
                forecastedCapacityHe18,
                forecastedCapacityHe19,
                forecastedCapacityHe20,
                forecastedCapacityHe21,
                forecastedCapacityHe22,
                forecastedCapacityHe23,
                forecastedCapacityHe24
        );
    }

    public List<Long> getSafetyFactorsAsList() {
        return Arrays.asList(
                safetyFactorHe1,
                safetyFactorHe2,
                safetyFactorHe3,
                safetyFactorHe4,
                safetyFactorHe5,
                safetyFactorHe6,
                safetyFactorHe7,
                safetyFactorHe8,
                safetyFactorHe9,
                safetyFactorHe10,
                safetyFactorHe11,
                safetyFactorHe12,
                safetyFactorHe13,
                safetyFactorHe14,
                safetyFactorHe15,
                safetyFactorHe16,
                safetyFactorHe17,
                safetyFactorHe18,
                safetyFactorHe19,
                safetyFactorHe20,
                safetyFactorHe21,
                safetyFactorHe22,
                safetyFactorHe23,
                safetyFactorHe24
        );
    }

    public List<String> getStatusesHe() {
        return Arrays.asList(
                statusHe1,
                statusHe2,
                statusHe3,
                statusHe4,
                statusHe5,
                statusHe6,
                statusHe7,
                statusHe8,
                statusHe9,
                statusHe10,
                statusHe11,
                statusHe12,
                statusHe13,
                statusHe14,
                statusHe15,
                statusHe16,
                statusHe17,
                statusHe18,
                statusHe19,
                statusHe20,
                statusHe21,
                statusHe22,
                statusHe23,
                statusHe24
        );
    }

    public List<String> getIsoMessagesHe() {
        return Arrays.asList(
                isoMessageHe1,
                isoMessageHe2,
                isoMessageHe3,
                isoMessageHe4,
                isoMessageHe5,
                isoMessageHe6,
                isoMessageHe7,
                isoMessageHe8,
                isoMessageHe9,
                isoMessageHe10,
                isoMessageHe11,
                isoMessageHe12,
                isoMessageHe13,
                isoMessageHe14,
                isoMessageHe15,
                isoMessageHe16,
                isoMessageHe17,
                isoMessageHe18,
                isoMessageHe19,
                isoMessageHe20,
                isoMessageHe21,
                isoMessageHe22,
                isoMessageHe23,
                isoMessageHe24
        );
    }

    public Long getCapacityHe(int hour) {
        if (hour >= 1 && hour <= 24) {
            return getCapacitiesAsList().get(hour - 1);
        }

        return 0L;
    }

    public Long getForecastedCapacityHe(int hour) {
        if (hour >= 1 && hour <= 24) {
            return getForecastedCapacitiessAsList().get(hour - 1);
        }

        return 0L;
    }

    public Long getPriceHe(int hour) {
        if (hour >= 1 && hour <= 24) {
            return getPricesAsList().get(hour - 1);
        }

        return 0L;
    }

    public void setPriceHe(int hour, Long price) {
        if (hour == 1) {
            priceHe1 = price;
        }
        if (hour == 2) {
            priceHe2 = price;
        }
        if (hour == 3) {
            priceHe3 = price;
        }
        if (hour == 4) {
            priceHe4 = price;
        }
        if (hour == 5) {
            priceHe5 = price;
        }
        if (hour == 6) {
            priceHe6 = price;
        }
        if (hour == 7) {
            priceHe7 = price;
        }
        if (hour == 8) {
            priceHe8 = price;
        }
        if (hour == 9) {
            priceHe9 = price;
        }
        if (hour == 10) {
            priceHe10 = price;
        }
        if (hour == 11) {
            priceHe11 = price;
        }
        if (hour == 12) {
            priceHe12 = price;
        }
        if (hour == 13) {
            priceHe13 = price;
        }
        if (hour == 14) {
            priceHe14 = price;
        }
        if (hour == 15) {
            priceHe15 = price;
        }
        if (hour == 16) {
            priceHe16 = price;
        }
        if (hour == 17) {
            priceHe17 = price;
        }
        if (hour == 18) {
            priceHe18 = price;
        }
        if (hour == 19) {
            priceHe19 = price;
        }
        if (hour == 20) {
            priceHe20 = price;
        }
        if (hour == 21) {
            priceHe21 = price;
        }
        if (hour == 22) {
            priceHe22 = price;
        }
        if (hour == 23) {
            priceHe23 = price;
        }
        if (hour == 24) {
            priceHe24 = price;
        }
    }

    public void setCapacityHe(int hour, Long capacity) {
        if (hour == 1) {
            capacityHe1 = capacity;
        }
        if (hour == 2) {
            capacityHe2 = capacity;
        }
        if (hour == 3) {
            capacityHe3 = capacity;
        }
        if (hour == 4) {
            capacityHe4 = capacity;
        }
        if (hour == 5) {
            capacityHe5 = capacity;
        }
        if (hour == 6) {
            capacityHe6 = capacity;
        }
        if (hour == 7) {
            capacityHe7 = capacity;
        }
        if (hour == 8) {
            capacityHe8 = capacity;
        }
        if (hour == 9) {
            capacityHe9 = capacity;
        }
        if (hour == 10) {
            capacityHe10 = capacity;
        }
        if (hour == 11) {
            capacityHe11 = capacity;
        }
        if (hour == 12) {
            capacityHe12 = capacity;
        }
        if (hour == 13) {
            capacityHe13 = capacity;
        }
        if (hour == 14) {
            capacityHe14 = capacity;
        }
        if (hour == 15) {
            capacityHe15 = capacity;
        }
        if (hour == 16) {
            capacityHe16 = capacity;
        }
        if (hour == 17) {
            capacityHe17 = capacity;
        }
        if (hour == 18) {
            capacityHe18 = capacity;
        }
        if (hour == 19) {
            capacityHe19 = capacity;
        }
        if (hour == 20) {
            capacityHe20 = capacity;
        }
        if (hour == 21) {
            capacityHe21 = capacity;
        }
        if (hour == 22) {
            capacityHe22 = capacity;
        }
        if (hour == 23) {
            capacityHe23 = capacity;
        }
        if (hour == 24) {
            capacityHe24 = capacity;
        }
    }

    public void setStatusHe(int hour, String status) {
        if (hour == 1) {
            statusHe1 = status;
        }
        if (hour == 2) {
            statusHe2 = status;
        }
        if (hour == 3) {
            statusHe3 = status;
        }
        if (hour == 4) {
            statusHe4 = status;
        }
        if (hour == 5) {
            statusHe5 = status;
        }
        if (hour == 6) {
            statusHe6 = status;
        }
        if (hour == 7) {
            statusHe7 = status;
        }
        if (hour == 8) {
            statusHe8 = status;
        }
        if (hour == 9) {
            statusHe9 = status;
        }
        if (hour == 10) {
            statusHe10 = status;
        }
        if (hour == 11) {
            statusHe11 = status;
        }
        if (hour == 12) {
            statusHe12 = status;
        }
        if (hour == 13) {
            statusHe13 = status;
        }
        if (hour == 14) {
            statusHe14 = status;
        }
        if (hour == 15) {
            statusHe15 = status;
        }
        if (hour == 16) {
            statusHe16 = status;
        }
        if (hour == 17) {
            statusHe17 = status;
        }
        if (hour == 18) {
            statusHe18 = status;
        }
        if (hour == 19) {
            statusHe19 = status;
        }
        if (hour == 20) {
            statusHe20 = status;
        }
        if (hour == 21) {
            statusHe21 = status;
        }
        if (hour == 22) {
            statusHe22 = status;
        }
        if (hour == 23) {
            statusHe23 = status;
        }
        if (hour == 24) {
            statusHe24 = status;
        }
    }

    public void setIsoMessagesHe(int hour, String status) {
        if (hour == 1) {
            isoMessageHe1 = status;
        }
        if (hour == 2) {
            isoMessageHe2 = status;
        }
        if (hour == 3) {
            isoMessageHe3 = status;
        }
        if (hour == 4) {
            isoMessageHe4 = status;
        }
        if (hour == 5) {
            isoMessageHe5 = status;
        }
        if (hour == 6) {
            isoMessageHe6 = status;
        }
        if (hour == 7) {
            isoMessageHe7 = status;
        }
        if (hour == 8) {
            isoMessageHe8 = status;
        }
        if (hour == 9) {
            isoMessageHe9 = status;
        }
        if (hour == 10) {
            isoMessageHe10 = status;
        }
        if (hour == 11) {
            isoMessageHe11 = status;
        }
        if (hour == 12) {
            isoMessageHe12 = status;
        }
        if (hour == 13) {
            isoMessageHe13 = status;
        }
        if (hour == 14) {
            isoMessageHe14 = status;
        }
        if (hour == 15) {
            isoMessageHe15 = status;
        }
        if (hour == 16) {
            isoMessageHe16 = status;
        }
        if (hour == 17) {
            isoMessageHe17 = status;
        }
        if (hour == 18) {
            isoMessageHe18 = status;
        }
        if (hour == 19) {
            isoMessageHe19 = status;
        }
        if (hour == 20) {
            isoMessageHe20 = status;
        }
        if (hour == 21) {
            isoMessageHe21 = status;
        }
        if (hour == 22) {
            isoMessageHe22 = status;
        }
        if (hour == 23) {
            isoMessageHe23 = status;
        }
        if (hour == 24) {
            isoMessageHe24 = status;
        }

    }
}