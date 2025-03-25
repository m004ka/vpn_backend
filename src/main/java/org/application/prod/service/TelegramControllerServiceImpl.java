package org.application.prod.service;

import lombok.RequiredArgsConstructor;
import org.application.prod.dto.TelegramUserDTO;
import org.application.prod.repository.TelegramUserRepository;
import org.application.prod.models.TelegramUser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class TelegramControllerServiceImpl implements TelegramControllerService{
    private final TelegramUserRepository tgUserRepository;

    public TelegramUser getUserById(Long id){
        TelegramUser telegramUser = tgUserRepository.findByChatId(id).orElseThrow(() -> new RuntimeException("Аккаунт c Id" + id + " не найден"));
        if(telegramUser != null){
            return telegramUser;
        }else {
            return null;
        }
    }


    @Override
    public TelegramUser createUser(TelegramUserDTO telegramUserDTO) {
        TelegramUser tgUser = TelegramUser.builder()
                .chatId(telegramUserDTO.getChatId())
                .firstName(telegramUserDTO.getFirstName())
                .lastName(telegramUserDTO.getLastName())
                .link(telegramUserDTO.getLink())
                .registeredAt(telegramUserDTO.getRegisteredAt())
                .payments(new ArrayList<>())
                .build();

        tgUserRepository.save(tgUser);
        return tgUser;
    }



}
