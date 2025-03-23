package org.application.prod.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentDTO {

    Long telegramUserId;

    String email;

    Boolean receipt;

    Double value;
}
