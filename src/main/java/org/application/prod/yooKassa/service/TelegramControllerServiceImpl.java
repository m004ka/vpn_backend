package org.application.prod.yooKassa.service;

import lombok.RequiredArgsConstructor;
import org.application.prod.repository.TelegramUserRepository;
import org.application.prod.telegram.models.TelegramUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramControllerServiceImpl {
    private final TelegramUserRepository tgUserRepository;

    public String getUserById(Long id){
        TelegramUser telegramUser = tgUserRepository.findByChatId(id).orElseThrow(() -> new RuntimeException("Аккаунт c Id" + id + " не найден"));
        return null;
    }

}
