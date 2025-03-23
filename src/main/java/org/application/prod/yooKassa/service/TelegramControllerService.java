package org.application.prod.yooKassa.service;

import org.application.prod.dto.PaymentDTO;

public interface TelegramControllerService {

    public String getUserById(Long id);

    public String createPayment(PaymentDTO paymentDTO);

    public String createUser();


}
