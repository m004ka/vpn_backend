package org.application.prod.service;

import org.application.prod.dto.PaymentDTO;
import org.application.prod.dto.TelegramUserDTO;
import org.application.prod.models.TelegramUser;

public interface TelegramControllerService {

    public TelegramUser getUserById(Long id);


    public TelegramUser createUser(TelegramUserDTO telegramUserDTO);


}
