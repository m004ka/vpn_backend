package org.application.prod.service;

import lombok.RequiredArgsConstructor;
import org.application.prod.client.TelegramClient;
import org.application.prod.dto.TelegramUserDTO;
import org.application.prod.models.Payment;
import org.application.prod.models.TelegramUser;
import org.application.prod.repository.TelegramUserRepository;
import org.application.prod.service.vpn.VpnService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final VpnService vpnService;
    private final TelegramUserRepository userRepository;
    private final TelegramClient tgClient;

    public TelegramUser createUser(TelegramUserDTO userDTO) {
        TelegramUser user = TelegramUser.builder()
                .chatId(userDTO.getChatId())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .username(userDTO.getUsername())
                .link(userDTO.getLink())
                .registeredAt(userDTO.getRegisteredAt())
                .payments(new ArrayList<>())
                .build();

        userRepository.save(user);
        vpnService.createClient(user);

        return user;
    }

    public void updateUser(TelegramUser user, Payment payment) {
        user.getPayments().add(payment);
        userRepository.save(user);
    }

    public void updateUserNotification(TelegramUser user, Payment payment){
        user.getPayments().add(payment);
        userRepository.save(user);

        vpnService.updateClient(user, payment.getValue());
        tgClient.updateTelegramPayment(payment);
    }

    public TelegramUser getUser(Long chatId) {
        return userRepository.findByChatId(chatId).orElseThrow(() -> new RuntimeException("Пользователь с chatId: " + chatId + " не найден"));
    }

    public List<Long> getLostUser(Long chatId) {
        TelegramUser user = userRepository.findByChatId(chatId).orElseThrow(() -> new RuntimeException("Пользователь с chatId: " + chatId + " не найден"));
        return vpnService.getTimeToLostClient(user);
    }
}
