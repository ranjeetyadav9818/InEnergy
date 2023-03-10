package com.inenergis.network.multichain;

import com.inenergis.network.pgerestclient.GsonHelper;
import lombok.Data;

import java.util.Date;

@Data
public class SettlementForTest {

    private String producer;
    private String producerAddress;
    private String consumer;
    private String consumerAddress;
    private Date date;
    private Long realProductionInWatts;
    private String transactionId;

    public String toJson() {
        return GsonHelper.getGson().toJson(this);
    }

    public static SettlementForTest fromJson(String json){
        return GsonHelper.getGson().fromJson(json,SettlementForTest.class);
    }
}
