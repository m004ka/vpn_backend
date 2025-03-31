package org.application.prod.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class VpnApiConfig {
    private String token;

}