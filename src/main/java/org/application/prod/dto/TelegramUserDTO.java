package org.application.prod.dto;

import lombok.Data;

import java.sql.Timestamp;
@Data
public class TelegramUserDTO {

    private Long chatId;
    private String firstName;
    private String lastName;
    private String username;

    private String link;

    private Timestamp registeredAt;
}
