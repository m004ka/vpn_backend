package org.application.prod.service;

import lombok.RequiredArgsConstructor;
import org.application.prod.client.PaymentClient;
import org.application.prod.client.TelegramClient;
import org.application.prod.dto.CreatePaymentDTO;
import org.application.prod.dto.PaymentDTO;
import org.application.prod.dto.ResponsePayDTO;
import org.application.prod.dto.WebhookNotificationDTO;
import org.application.prod.models.Payment;
import org.application.prod.models.PaymentResponse;
import org.application.prod.models.TelegramUser;
import org.application.prod.repository.PaymentRepository;
import org.application.prod.repository.TelegramUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentClient paymentClient;
    private final PaymentRepository paymentRepository;
    private final TelegramUserRepository telegramUserRepository;
    private final TelegramClient tgClient;

    public ResponseEntity<?> paymentRequestTelegram(CreatePaymentDTO createPaymentDTO) {

        Payment payment = createPayment(createPaymentDTO);


        TelegramUser tgUser = telegramUserRepository.findById(createPaymentDTO.getId())
                .orElseThrow(() -> new RuntimeException("TelegramUser not found for ID: " + createPaymentDTO.getId()));


        PaymentDTO dto = PaymentDTO.builder()
                .telegramUserId(createPaymentDTO.getId())
                .username(tgUser.getUsername())
                .mail(payment.getMail())
                .value(payment.getValue())
                .receipt(payment.getReceipt())
                .orderId(payment.getOrderId())
                .build();

        try {
            ResponseEntity<?> paymentYookassaResponse = paymentClient.createPayment(dto)
                    .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                    .block();

            if (paymentYookassaResponse != null && paymentYookassaResponse.getBody() instanceof PaymentResponse) {
                PaymentResponse paymentResponse = (PaymentResponse) paymentYookassaResponse.getBody();
                createPayment(paymentResponse, createPaymentDTO);


                return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to get valid response from Yookassa");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Payment creation failed: " + e.getMessage());
        }


    }


    public Payment createPayment(PaymentResponse paymentResponse, CreatePaymentDTO createPaymentDTO) {
        Payment payment = Payment.builder()
                .telegramUser(telegramUserRepository.findByChatId(createPaymentDTO.getId()).orElseThrow(() -> new RuntimeException("TelegramUser not found for ID: " + createPaymentDTO.getId())))
                .mail(createPaymentDTO.getMail())
                .value(createPaymentDTO.getValue())
                .receipt(createPaymentDTO.getReceipt())
                .orderId(UUID.randomUUID().getMostSignificantBits())
                .yookassaPaymentId(paymentResponse.getId())
                .status(paymentResponse.getStatus())
                .build();

        paymentRepository.save(payment);

        return payment;
    }

    public Payment createPayment(CreatePaymentDTO createPaymentDTO) {
        Payment payment = Payment.builder()
                .telegramUser(telegramUserRepository.findByChatId(createPaymentDTO.getId()).orElseThrow(() -> new RuntimeException("TelegramUser not found for ID: " + createPaymentDTO.getId())))
                .mail(createPaymentDTO.getMail())
                .value(createPaymentDTO.getValue())
                .receipt(createPaymentDTO.getReceipt())
                .orderId(UUID.randomUUID().getMostSignificantBits())
                .yookassaPaymentId("еще не создан в Юкассе")
                .status("еще не создан в Юкассе")
                .build();

        paymentRepository.save(payment);

        return payment;
    }

    public boolean validPaymentDTO(CreatePaymentDTO createPaymentDTO) {
        return Stream.of(
                        createPaymentDTO.getId(),
                        createPaymentDTO.getMail(),
                        createPaymentDTO.getReceipt(),
                        createPaymentDTO.getValue())
                .noneMatch(Objects::isNull);
    }

    public Payment updatePayment(WebhookNotificationDTO notificationDTO){
        Payment payment = paymentRepository.getPaymentsByYookassaPaymentId(notificationDTO.getObject().getId())
                .orElseThrow(() -> new RuntimeException("Платеж не найден"));
        payment.setStatus(notificationDTO.getObject().getStatus());
        paymentRepository.save(payment);
        //todo Добавление времени в клиенте впн
        tgClient.updateTelegramPay(ResponsePayDTO.paymentRespToDTO(payment));
        return payment;
    }
}
