package org.application.prod.client;




import org.application.prod.dto.PaymentDTO;
import org.application.prod.models.PaymentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;


@Service
public class PaymentClient {
    @Value("${payment.host}")
    private String baseUrl;

    private final WebClient webClient = WebClient.create(baseUrl);

    public Mono<?> createPayment(PaymentDTO paymentDTO) {
        return webClient.post()
                .uri("/createPayment")
                .bodyValue(paymentDTO)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorMessage -> Mono.error(new RuntimeException(errorMessage)))
                )
                .bodyToMono(PaymentResponse.class)
                .onErrorResume(e -> {

                    return Mono.just((PaymentResponse) Map.of("error", "Payment creation failed: " + e.getMessage()));
                });
    }




}
