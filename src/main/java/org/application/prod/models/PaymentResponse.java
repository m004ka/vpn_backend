package org.application.prod.models;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Map;

@Data
@Builder
public class PaymentResponse {
    private String id;
    private String status;
    private boolean paid;
    private Amount amount;
    private Confirmation confirmation;


    private ZonedDateTime created_at;

    private String description;
    private Map<String, Object> metadata;
    private Recipient recipient;
    private boolean refundable;
    private boolean test;

    @Data
    public static class Confirmation {
        private String type;
        private String confirmation_url;
    }

    @Data
    public static class Recipient {
        private String account_id;
        private String gateway_id;
    }


}
