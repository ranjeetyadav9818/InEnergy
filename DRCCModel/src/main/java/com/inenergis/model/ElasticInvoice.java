package com.inenergis.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inenergis.util.json.LocalDateDeserializer;
import com.inenergis.util.json.LocalDateSerializer;
import com.inenergis.util.json.LocalDateTimeDeserializer;
import com.inenergis.util.json.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.beans.Transient;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by egamas on 05/09/2017.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElasticInvoice implements SearchMatch{

    public static final String INVOICE = "invoice";
    private NumberFormat formatter = new DecimalFormat("\t$###,###.###");

    private String invoiceNumber;
    private String description;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dueDate;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateFrom;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateTo;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime paymentDate;

    private String serviceAgreement;
    private List<String> servicePoints;
    private Long total;

    @Override
    public String toString(){
        return "#"+(invoiceNumber==null? StringUtils.EMPTY:invoiceNumber) + " - SA ID "+serviceAgreement + " - "+ formatter.format(getTotalInDollars());
    }

    @Transient
    public BigDecimal getTotalInDollars() {
        return (new BigDecimal(total)).divide(new BigDecimal(100));
    }

    @Override
    @Transient
    public String getId() {
        return invoiceNumber;
    }

    @Override
    public String getType() {
        return INVOICE;
    }

    @Override
    @Transient
    public String getIcon() {
        return "attach_money";
    }

    @Override
    public String getPermission() {
        return null;
    }
}
