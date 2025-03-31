package org.application.prod.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.application.prod.dto.PaymentDTO;
import org.application.prod.models.PaymentResponse;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentClient {

    private final PaymentFeignClient paymentFeignClient;

    public PaymentResponse createPayment(PaymentDTO paymentDTO) {
        try {
            log.error("Пеймент: " + paymentDTO);
            return paymentFeignClient.createPayment(paymentDTO);
        } catch (Exception e) {
            log.warn("Ошибка в PaymentClient: " + e.getMessage());
        }
        return  PaymentResponse.builder().build();
    }
}
