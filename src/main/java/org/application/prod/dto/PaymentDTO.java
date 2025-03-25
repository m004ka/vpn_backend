package org.application.prod.dto;

import lombok.Builder;
import lombok.Data;
import org.application.prod.models.PaymentResponse;
import org.application.prod.repository.TelegramUserRepository;

@Data
@Builder
public class PaymentDTO {

    private Long telegramUserId;

    private String username;

    private String mail;

    private Double value;

    private Boolean receipt;

    private Long orderId;
}

