package org.application.prod.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.application.prod.client.VpnClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropertyUpdateService {

    private final VpnApiConfig vpnApiConfig;
    private final VpnClient vpnClient;



    @PostConstruct
    @Scheduled(cron = "0 0 0 * * ?") // Каждый день в полночь
    public void updateProperty() {
        try {
            log.info("Запуск обновления свойства panel.session...");

            String newValue = fetchNewValueFromApi();
            log.info("Получено новое значение для panel.session: {}", newValue);

            vpnApiConfig.setToken(newValue);

            log.info("Свойство panel.session успешно обновлено.");
        } catch (Exception e) {
            log.error("Ошибка при обновлении свойства panel.session: ", e);
        }
    }

    private String fetchNewValueFromApi() {
        log.info("Отправка запроса на получение нового значения panel.session...");
        List<String> cook = vpnClient.sendPostAuthorization();

        if (cook == null || cook.isEmpty()) {
            log.warn("Ответ от SendPostAuthorization пустой или null!");
            return "defaultSessionValue";
        }

        log.info("Успешно получено новое значение panel.session из API.");
        System.out.println("Кука  "  + extractCookie(cook.get(0)));
        return extractCookie(cook.get(0));
    }

    public static String extractCookie(String cookieString) {
        if (cookieString == null || cookieString.isEmpty()) {
            return null;
        }
        Pattern pattern = Pattern.compile("(3x-ui=[^;]+)");
        Matcher matcher = pattern.matcher(cookieString);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
}
