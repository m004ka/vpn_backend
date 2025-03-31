package org.application.prod.service.vpn;

import lombok.RequiredArgsConstructor;
import org.application.prod.client.VpnClient;
import org.application.prod.models.TelegramUser;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeService {
    private final VpnClient vpnClient;
    public List<Long> checkTime(TelegramUser telegramUser){
        Long timeToLost = vpnClient.getTimeToLeft(telegramUser);
        if(timeToLost > Instant.now().toEpochMilli()){
            return getTime(timeToLost);
        }else {
            return getLostTime();
        }
    }

    public void updateTime(TelegramUser telegramUser, Double value) {
        Integer months = payValueToMonths(value);
        Long timeMillis = vpnClient.getTimeToLeft(telegramUser);
        LocalDateTime currentDateTime = Instant.ofEpochMilli(timeMillis)
                .atZone(ZoneOffset.UTC)
                .toLocalDateTime();

        LocalDateTime newDateTime = currentDateTime.plusMonths(months);
        long epochMilli = newDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        vpnClient.updateClientToInbound(telegramUser, epochMilli);
    }

    private List<Long> getTime(long lastTime) {
        long currentTime = Instant.now().toEpochMilli();
        long diff = lastTime - currentTime;

        long seconds = diff / 1000;

        long days = seconds / (24 * 60 * 60);
        seconds %= (24 * 60 * 60);

        long hours = seconds / (60 * 60);
        seconds %= (60 * 60);

        long minutes = seconds / 60;
        seconds %= 60;

        List<Long> arr = new ArrayList<>();
        arr.add(days);
        arr.add(hours);
        arr.add(minutes);
        arr.add(seconds);

        return arr;
    }

    private List<Long> getLostTime() {
        long days = 0;
        long hours = 0;
        long minutes = 0;
        long seconds = 0;

        List<Long> arr = new ArrayList<>();
        arr.add(days);
        arr.add(hours);
        arr.add(minutes);
        arr.add(seconds);
        return arr;
    }

    private Integer payValueToMonths(Double value) {
        if (value == 199.00) {
            return 1;
        } else if (value == 499.00) {
            return 3;
        } else if (value == 799.00) {
            return 6;
        } else if (value == 1499.00) {
            return 12;
        } else {
            return 0;
        }
    }

}
