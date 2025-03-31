package org.application.prod.controllers;

import lombok.RequiredArgsConstructor;
import org.application.prod.client.TelegramClient;
import org.application.prod.dto.NotificationDTO;
import org.application.prod.models.Payment;
import org.application.prod.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final TelegramClient telegramClient;

    @PostMapping("/notification")
    public ResponseEntity<?> paymentNotification(@RequestBody NotificationDTO notificationDTO) {


        try {
            Payment updatedPayment = paymentService.updatePayment(notificationDTO);
            telegramClient.updateTelegramPayment(updatedPayment);
            return ResponseEntity.ok(updatedPayment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при обновлении платежа: " + e.getMessage());
        }
    }

}
