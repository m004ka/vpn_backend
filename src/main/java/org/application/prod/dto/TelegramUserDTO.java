package org.application.prod.dto;

import lombok.Builder;
import lombok.Data;
import org.application.prod.models.TelegramUser;

import java.sql.Timestamp;

@Data
@Builder
public class TelegramUserDTO {

    private Long chatId;
    private String firstName;
    private String lastName;
    private String username;

    private String link;

    private Timestamp registeredAt;

    public static TelegramUserDTO toTgUserDTO(TelegramUser user){
        return TelegramUserDTO.builder()
                .chatId(user.getChatId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .link(user.getLink())
                .registeredAt(user.getRegisteredAt())
                .build();
    }
}
