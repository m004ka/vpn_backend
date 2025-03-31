package org.application.prod.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.application.prod.config.ConfigurationVpnProperties;
import org.application.prod.config.VpnApiConfig;
import org.application.prod.models.TelegramUser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class VpnClient {

    private final ConfigurationVpnProperties vpnProperties;
    private final VpnFeignClient vpnFeignClient;
    private final VpnApiConfig vpnApiConfig;

    public List<String> sendPostAuthorization(){
        Map<String, String> body = new HashMap<>();
        body.put("username", vpnProperties.getLogin());
        body.put("password", vpnProperties.getPassword());
        try {
            ResponseEntity<String> response = vpnFeignClient.sendPostAuthorization(body);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Авторизация успешна");
            } else {
                log.error("Ошибка при отправке запроса на авторизацию. Статус: " + response.getStatusCode());
            }
            return response.getHeaders().get("Set-Cookie");
        }catch (Exception e){
            log.warn("Авторизация не прошла запрос не ушёл" + e.getMessage());
            return  List.of();
        }

    }

    public void addClientToInbound(TelegramUser telegramUser){
        long expiryTimestampMillis = Instant.now().plus(7, ChronoUnit.DAYS).atZone(ZoneOffset.UTC).toInstant().toEpochMilli();

        Map<String, Object> clientData = getStringObjectMap(telegramUser, expiryTimestampMillis);

        Map<String, Object> settings = new HashMap<>();
        settings.put("clients", Collections.singletonList(clientData));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", vpnProperties.getConfigId());

        try {
            String settingsJson = new ObjectMapper().writeValueAsString(settings);
            requestBody.put("settings", settingsJson);

            System.out.println(requestBody);

            ResponseEntity<String> response = vpnFeignClient.addClientToInbound(requestBody, vpnApiConfig.getToken());

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Ответ от сервера: " + response.getBody());
            } else {
                log.error("Ошибка при отправке запроса. Статус: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.warn("Ошибка при отправке запроса для пользователя (chatId: " + telegramUser.getChatId() + "): " + e.getMessage());
        }
    }

    public void updateClientToInbound(TelegramUser telegramUser, Long updatedTime){
        Map<String, Object> clientData = getStringObjectMap(telegramUser, updatedTime);

        Map<String, Object> settings = new HashMap<>();
        settings.put("clients", Collections.singletonList(clientData));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", vpnProperties.getConfigId());

        try {
            String settingsJson = new ObjectMapper().writeValueAsString(settings);
            requestBody.put("settings", settingsJson);

            ResponseEntity<String> response = vpnFeignClient.updateClientInbound(telegramUser.getChatId() ,requestBody, vpnApiConfig.getToken());

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Ответ от сервера: " + response.getBody());
            } else {
                log.error("Ошибка при отправке запроса. Статус: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.warn("Ошибка при отправке запроса для пользователя (chatId: " + telegramUser.getChatId() + "): " + e.getMessage());
        }
    }

    public void deleteClientToInbound(TelegramUser telegramUser){
        try {

            ResponseEntity<String> response = vpnFeignClient.deleteClientInbound(vpnProperties.getConfigId() ,telegramUser.getChatId(), vpnApiConfig.getToken());
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Ответ от сервера: " + response.getBody());
            } else {
                log.error("Ошибка при отправке запроса. Статус: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.warn("Ошибка при отправке запроса для пользователя (chatId: " + telegramUser.getChatId() + "): " + e.getMessage());
        }
    }

    public Long getTimeToLeft(TelegramUser telegramUser) {
        try {

            ResponseEntity<String> response = vpnFeignClient.getTimeToLeft(telegramUser.getChatId(), vpnApiConfig.getToken());

            if (response.getStatusCode().is2xxSuccessful()) {

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                JsonNode objArray = rootNode.get("obj");
                if (objArray != null && objArray.size() > 0) {
                    JsonNode firstObject = objArray.get(0);
                    Long expiryTime = firstObject.get("expiryTime").asLong();
                    log.info("Время до конца: " + expiryTime);
                    return expiryTime;
                } else {
                    log.error("Пустой массив 'obj'. Статус: " + response.getStatusCode());
                }
            } else {
                log.error("Ошибка при отправке запроса. Статус: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.warn("Ошибка при выполнении GET-запроса для пользователя (id: " + telegramUser.getChatId() + "): " + e.getMessage());
        }

        return null;
    }

    private Map<String, Object> getStringObjectMap(TelegramUser telegramUser, Long updatedTime) {
        Map<String, Object> clientData = new HashMap<>();
        clientData.put("id", String.valueOf(telegramUser.getChatId()));
        clientData.put("flow", vpnProperties.getProtocol());
        clientData.put("email", telegramUser.getUsername());
        clientData.put("limitIp", 0);
        clientData.put("totalGB", 0);
        clientData.put("expiryTime", updatedTime);
        clientData.put("enable", true);
        clientData.put("tgId", telegramUser.getUserId());
        clientData.put("subId", telegramUser.getUsername());
        clientData.put("reset", 0);
        return clientData;
    }
}
