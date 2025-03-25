package org.application.prod.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.application.prod.models.Payment;
import org.application.prod.models.PaymentResponse;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePayDTO {
    private String id;
    private String confirmation_url;
    private String account_id;

    public static ResponsePayDTO paymentRespToDTO(PaymentResponse paymentResponse) {
        return ResponsePayDTO.builder()
                .id(paymentResponse.getId())
                .confirmation_url(paymentResponse.getConfirmation().getConfirmation_url())
                .account_id(paymentResponse.getRecipient().getAccount_id())
                .build();
    }

    public static ResponsePayDTO paymentRespToDTO(Payment payment){
        return ResponsePayDTO.builder()
                .account_id(payment.getTelegramUserId().toString())
                .id(payment.getYookassaPaymentId())
                .build();

    }
}
