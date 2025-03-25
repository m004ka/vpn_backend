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
    @Id
    private Long chatId;

    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private String link;
    private Timestamp registeredAt;

    @OneToMany(mappedBy = "telegramUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;
}
