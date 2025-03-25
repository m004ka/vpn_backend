package org.application.prod.controllers;

import lombok.RequiredArgsConstructor;
import org.application.prod.dto.CreatePaymentDTO;
import org.application.prod.dto.ResponsePayDTO;
import org.application.prod.dto.TelegramUserDTO;
import org.application.prod.models.PaymentResponse;
import org.application.prod.service.PaymentService;
import org.application.prod.service.TelegramControllerServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/telegram")
@RequiredArgsConstructor
public class TelegramController {

    private final TelegramControllerServiceImpl telegramControllerService;
    private final PaymentService paymentService;

    @GetMapping("/getUserById")
    public String getUser(@RequestParam Long id){
        telegramControllerService.getUserById(id);


        return null;
    }

    @PostMapping("/create")
    public String createUser(@RequestBody TelegramUserDTO telegramUserDTO){
        telegramControllerService.
    }

    @PostMapping("/payment/create")
    public ResponseEntity<?> createPayment(@RequestBody CreatePaymentDTO createPaymentDTO) {

        if (createPaymentDTO == null || !paymentService.validPaymentDTO(createPaymentDTO)) {
            return ResponseEntity.badRequest().body("Invalid payment data");
        }

        try {
            ResponseEntity<?> responseEntity = paymentService.paymentRequestTelegram(createPaymentDTO);

            if (responseEntity.getBody() instanceof PaymentResponse) {
                PaymentResponse paymentResponse = (PaymentResponse) responseEntity.getBody();
                return ResponseEntity.status(HttpStatus.CREATED).body(ResponsePayDTO.paymentRespToDTO(paymentResponse));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to process payment response");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Payment creation failed: " + e.getMessage());
        }
    }


}
