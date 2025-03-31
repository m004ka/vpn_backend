package org.application.prod.service.vpn;

import lombok.RequiredArgsConstructor;
import org.application.prod.client.VpnClient;
import org.application.prod.models.TelegramUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VpnService {

    private final TimeService timeService;
    private final VpnClient vpnClient;
    public void createClient(TelegramUser telegramUser){
        vpnClient.addClientToInbound(telegramUser);
    }

    public void updateClient(TelegramUser telegramUser, Double value){
        timeService.updateTime(telegramUser, value);
    }

    public void deleteClient(TelegramUser telegramUser){
        vpnClient.deleteClientToInbound(telegramUser);
    }

    public List<Long> getTimeToLostClient(TelegramUser telegramUser){
       return timeService.checkTime(telegramUser);
    }


}
