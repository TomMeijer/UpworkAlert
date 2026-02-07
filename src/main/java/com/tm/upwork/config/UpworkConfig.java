package com.tm.upwork.config;

import com.Upwork.api.Config;
import com.Upwork.api.OAuthClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class UpworkConfig {

    @Value("${upwork.api.client-id}")
    private String clientId;

    @Value("${upwork.api.client-secret}")
    private String clientSecret;

    @Bean
    public OAuthClient upworkOAuthClient() {
        var properties = new Properties();
        properties.setProperty("clientId", clientId);
        properties.setProperty("clientSecret", clientSecret);
        properties.setProperty("grantType", "client_credentials");
        return new OAuthClient(new Config(properties));
    }
}
