package org.application.prod.dto;

import lombok.Data;

@Data
public class WebhookNotificationDTO {
    private String type;
    private String event;
    private PaymentObject object;

    @Data
    public static class PaymentObject {
        private String id;
        private String status;
        private boolean paid;
        private Amount amount;

        @Data
        public static class Amount {
            private String value;
            private String currency;
        }
    }
}
