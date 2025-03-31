package org.application.prod.client;

import org.application.prod.dto.PaymentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "telegramClient", url = "${telegram.host}")
public interface TelegramFeignClient {

    @PostMapping("/api/telegram/updatePayment")
    public void updateTgPayment(@RequestBody PaymentDTO paymentDTO);
}
