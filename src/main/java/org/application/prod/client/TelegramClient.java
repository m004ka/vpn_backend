package org.application.prod.client;

import org.application.prod.dto.ResponsePayDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TelegramClient {

    @Value("${telegram.host}")
    private String baseUrl;

    private final WebClient webClient;

    public TelegramClient(@Value("${telegram.host}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public Mono<String> updateTelegramPay(ResponsePayDTO responsePayDTO) {
        return webClient.post()
                .uri("/updatePayment")
                .bodyValue(responsePayDTO)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> {
                    return Mono.just("error: " + e.getMessage());
                });
    }
}
