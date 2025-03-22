package org.application.prod.telegram.models;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class TelegramUser {
    private Long userId;
    @Id
    private Long chatId;
    private String firstName;
    private String lastName;
    private String username;

    private String link;

    private Timestamp registeredAt;
}