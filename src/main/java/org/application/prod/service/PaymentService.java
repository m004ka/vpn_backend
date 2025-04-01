package org.application.prod.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.application.prod.client.PaymentClient;
import org.application.prod.dto.CreatePaymentDTO;
import org.application.prod.dto.NotificationDTO;
import org.application.prod.dto.PaymentDTO;
import org.application.prod.models.Payment;
import org.application.prod.models.PaymentResponse;
import org.application.prod.models.TelegramUser;
import org.application.prod.repository.PaymentRepository;
import org.application.prod.repository.TelegramUserRepository;

import org.application.prod.service.vpn.VpnService;
import org.springframework.stereotype.Service;


@Slf4j //todo Нормальное логирование добавить в сервис
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentClient paymentClient;
    private final UserService userService;
    private final PaymentRepository paymentRepository;
    private final TelegramUserRepository userRepository;
    private final VpnService vpnService;


    public PaymentResponse createPayment(CreatePaymentDTO createDTO) {
        TelegramUser tgUser = userRepository.findByChatId(createDTO.getId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        PaymentDTO paymentDTO = PaymentDTO.builder()
                .telegramUserId(tgUser.getUserId())
                .username(tgUser.getUsername())
                .mail(createDTO.getMail())
                .value(createDTO.getValue())
                .receipt(createDTO.getReceipt())
                .order_id(createDTO.getOrder_id())
                .build();

        PaymentResponse response = paymentClient.createPayment(paymentDTO);

        if (response.getId() == null) {
            throw new RuntimeException("Ошибка проведения платежа");
        }

        getSuccessfulPayment(paymentDTO, response, tgUser);

        userService.updateUser(tgUser, getSuccessfulPayment(paymentDTO, response, tgUser));
        return response;
    }

    public Payment updatePayment(NotificationDTO notificationDTO) {
        Payment payment = paymentRepository.getPaymentsByYookassaPaymentId(notificationDTO.getOrder_id())
                .orElseThrow(() -> new RuntimeException
                        ("Платеж с YooKassa_Id " + notificationDTO.getOrder_id() + " не найден"));
        if (notificationDTO.getStatus() != null) {
            payment.setStatus(notificationDTO.getStatus());
        }
        paymentRepository.save(payment);
        userService.updateUserNotification(payment.getTelegramUser() ,payment);
        vpnService.updateClient(payment.getTelegramUser(), payment.getValue());
        return payment;
    }


    public boolean checkRequestPayment(CreatePaymentDTO createDTO) {
        return createDTO != null && paymentRepository.getPaymentsByOrderId(createDTO.getOrder_id()).isEmpty();
    }


    private Payment getSuccessfulPayment(PaymentDTO paymentDTO, PaymentResponse response, TelegramUser tgUser) {
        return Payment.builder()
                .yookassaPaymentId(response.getId())
                .telegramUser(tgUser)
                .receipt(paymentDTO.getReceipt())
                .mail(paymentDTO.getMail())
                .value(paymentDTO.getValue())
                .orderId(paymentDTO.getOrder_id())
                .status(response.getStatus())
                .build();
    }

}
