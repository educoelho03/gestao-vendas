package br.com.ms_notificacoes.config;

import br.com.ms_notificacoes.entities.SendGridProperties;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
@EnableConfigurationProperties(SendGridConfig.class)
public class SendGridConfig {

    private final SendGridProperties sendGridProperties;

    public SendGridConfig(SendGridProperties sendGridProperties) {
        this.sendGridProperties = sendGridProperties;
    }

    @Bean
    public SendGrid sendGrid(){
        String apiKey = sendGridProperties.getApiKey();
        return new SendGrid(apiKey);
    }

    @Bean
    public Email fromEmail() {
        String fromEmail = sendGridProperties.getSenderEmail();
        return new Email(fromEmail);
    }

}
