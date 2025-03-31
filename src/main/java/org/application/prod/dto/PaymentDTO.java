package org.application.prod.dto;

import lombok.Builder;
import lombok.Data;
import org.application.prod.models.Payment;


@Data
@Builder
public class PaymentDTO {

    private Long telegramUserId;

    private String username;

    private String mail;

    private Double value;

    private Boolean receipt;

    private String order_id;

    public static PaymentDTO toPaymentDTO(Payment payment){
        return PaymentDTO.builder()
                .telegramUserId(payment.getTelegramUser().getChatId())
                .username(payment.getTelegramUser().getUsername())
                .mail(payment.getMail())
                .value(payment.getValue())
                .receipt(payment.getReceipt())
                .order_id(payment.getOrderId())
                .build();
    }
}

