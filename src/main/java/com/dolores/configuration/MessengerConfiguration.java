package com.dolores.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "messenger")
@Component
@Getter
@Setter
public class MessengerConfiguration {

    private String secret;
    private String pageAccessToken;
    private String webhookVerificationToken;
}
