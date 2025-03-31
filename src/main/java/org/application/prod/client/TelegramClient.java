package org.application.prod.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.application.prod.dto.PaymentDTO;
import org.application.prod.models.Payment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramClient {

    private final TelegramFeignClient telegramFeignClient;

    public void updateTelegramPayment(Payment payment){
        try {
             telegramFeignClient.updateTgPayment(PaymentDTO.toPaymentDTO(payment));
        } catch (Exception e){
            log.warn("Ошибка при отправкий update платежа на фронт тг :" + e.getMessage());
        }
    }
}
