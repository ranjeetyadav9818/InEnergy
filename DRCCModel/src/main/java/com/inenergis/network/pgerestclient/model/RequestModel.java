package com.inenergis.network.pgerestclient.model;

import com.inenergis.network.pgerestclient.GsonHelper;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestModel {
    public transient String urlToken;
    public transient Class<?> responseModel;

    class IdModel {
        private String id;

        IdModel(String id) {
            this.id = id;
        }
    }

    public enum IdType {
        UUID,
        SAID
    }

    public String toJson() {
        return GsonHelper.getGson().toJson(this);
    }
}
