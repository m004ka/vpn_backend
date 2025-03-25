package org.application.prod.dto;

import lombok.Data;

@Data
public class CreatePaymentDTO {

    private Long id;

    private  String mail;

    private Boolean receipt;

    private Double value;
}
