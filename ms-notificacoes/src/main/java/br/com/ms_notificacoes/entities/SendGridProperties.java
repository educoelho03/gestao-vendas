package br.com.ms_notificacoes.entities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.sendgrid")
public class SendGridProperties {

    @Value("${spring.sendgrid.apiKey}")
    private String apiKey;

    @Value("${spring.sendgrid.from-email}")
    private String fromEmail;

    public String getApiKey() {
        return apiKey;
    }

    public String fromEmail() {
        return fromEmail;
    }
}
