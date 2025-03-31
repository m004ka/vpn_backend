package org.application.prod.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.application.prod.dto.CreatePaymentDTO;
import org.application.prod.dto.TelegramUserDTO;
import org.application.prod.models.PaymentResponse;
import org.application.prod.models.TelegramUser;
import org.application.prod.service.PaymentService;
import org.application.prod.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController //todo Украсить и логи
@RequestMapping("/api/telegram")
@RequiredArgsConstructor
public class TelegramController {

    private final UserService userService;
    private final PaymentService paymentService;

    @GetMapping("/getUserById")
    public ResponseEntity<?> getUser(@RequestParam Long id) {
        log.info("Запрос на получение пользователя с ID {}", id);
        try {
            TelegramUser user = userService.getUser(id);
            log.info("Пользователь с ID {} найден", id);
            System.out.println(TelegramUserDTO.toTgUserDTO(user));
            return ResponseEntity.ok(TelegramUserDTO.toTgUserDTO(user));
        } catch (RuntimeException e) {
            log.warn("Пользователь с ID {} не найден: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Ошибка при получении пользователя с ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody TelegramUserDTO telegramUserDTO) {
        log.info("Запрос на создание пользователя с данными: {}", telegramUserDTO);
        try {
            TelegramUser user = userService.createUser(telegramUserDTO);
            log.info("Пользователь с ID {} успешно создан", user.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(TelegramUserDTO.toTgUserDTO(user));
        } catch (RuntimeException e) {
            log.warn("Ошибка при создании пользователя: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            log.error("Ошибка при создании пользователя: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/payment/create")
    public ResponseEntity<?> createPayment(@RequestBody CreatePaymentDTO createPaymentDTO) {
        log.info("Запрос на создание платежа с данными: {}", createPaymentDTO);
        if (!paymentService.checkRequestPayment(createPaymentDTO)) {
            log.warn("Форма платежа отправлена неправильно");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Форма отправлена неправильно");
        }
        try {
            PaymentResponse response = paymentService.createPayment(createPaymentDTO);
            log.info("Платеж успешно создан с ID {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            log.warn("Ошибка при создании платежа: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            log.error("Ошибка при создании платежа: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getTime")
    public ResponseEntity<?> getTime(@RequestParam Long id) {
        log.info("Запрос на получение времени истечения подписки для пользователя с ID {}", id);
        try {
            List<Long> time = userService.getLostUser(id);
            log.info("Время истечения подписки для пользователя с ID {}: {}", id, time);
            return ResponseEntity.ok(time);
        } catch (RuntimeException e) {
            log.warn("Ошибка при получении времени истечения подписки для пользователя с ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("Ошибка при получении времени истечения подписки для пользователя с ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/get")
    public HttpStatus get(){
        log.warn("ВСЕ ДОШЛО БЛЯТЬ");

        return HttpStatus.CREATED;
    }
}
