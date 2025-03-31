package org.application.prod.client;

import org.application.prod.dto.PaymentDTO;
import org.application.prod.models.PaymentResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "paymentClient", url = "${payment.host}")
public interface PaymentFeignClient {

    @PostMapping("/api/yookassa/createPayment")
    PaymentResponse createPayment(@RequestBody PaymentDTO paymentDTO);
}
