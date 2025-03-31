package org.application.prod.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelegramUser {

    //todo Если приходит без юзернейма то надо делать id + номер айди
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private Long chatId;
    private String firstName;
    private String lastName;
    private String username;
    private String link;
    private Timestamp registeredAt;

    @OneToMany(mappedBy = "telegramUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;
}
