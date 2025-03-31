package org.application.prod.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "telegram_user_id", nullable = false)
    private TelegramUser telegramUser;

    private Boolean receipt;
    private String mail;
    private Double value;

    @Column(name = "order_id")
    private String orderId;

    private String yookassaPaymentId;
    private String status;
}

