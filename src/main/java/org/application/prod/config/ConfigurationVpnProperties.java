package org.application.prod.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "vpn")
public class ConfigurationVpnProperties {
    private String host;

    private String login;

    private String password;

    private Long configId;

    private String protocol;
}
