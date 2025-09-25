package br.com.ms_notificacoes.service;

import br.com.ms_notificacoes.dto.ClienteSaveDto;
import br.com.ms_notificacoes.entities.SendGridProperties;
import br.com.ms_notificacoes.utils.MaskUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private static final String MAIL_ENDPOINT = "mail/send";

    private final SendGridProperties sendGridProperties;
    private final Email fromEmail;
    private ObjectMapper objectMapper;

    public EmailService(ObjectMapper objectMapper, Email fromEmail, SendGridProperties sendGridProperties) {
        this.objectMapper = objectMapper;
        this.fromEmail = fromEmail;
        this.sendGridProperties = sendGridProperties;
    }

    @KafkaListener(topics = "cliente-added-topic", groupId = "cliente-group")
    public void sendWelcomeEmail(String mensagemJson) {
        try {
            ClienteSaveDto clienteDto = objectMapper.readValue(mensagemJson, ClienteSaveDto.class);

            log.info("Mensagem recebida do Kafka - Cliente: {}", clienteDto.getNome());

            if (clienteDto.getEmail() == null || clienteDto.getEmail().isEmpty()) {
                log.error("E-mail não encontrado para o cliente: {}", clienteDto.getNome());
                return;
            }

            String to = clienteDto.getEmail();
            String subject = "Bem-vindo ao Sistema de Gestão de Vendas!";
            String body = "Olá " + clienteDto.getNome() + "! Sua conta foi criada com sucesso.\n\n" +
                    "Agradecemos por se cadastrar em nosso sistema.\n";

            sendMail(to, subject, body);

        } catch (JsonProcessingException e) {
            log.error("Erro ao processar o JSON: " + mensagemJson);
            throw new RuntimeException(e);
        }
    }

    public void sendMail(String to, String subject, String body){
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(fromEmail, subject, toEmail, content);

        SendGrid sg = new SendGrid(sendGridProperties.getApiKey());
        Request request =  new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint(MAIL_ENDPOINT);
            request.setBody(mail.build());

            Response response = sg.api(request);

            if(response.getStatusCode() == 400) {
                log.error("Failed to send email. Status code: " + response.getStatusCode() + ", body: " + response.getBody());
            }

            log.info("response: {}", response.getBody());
            log.info("E-mail enviado com sucesso para: {}", MaskUtil.maskEmail(to));
        } catch (IOException e) {
            log.error("Erro ao enviar e-mail para: {}", MaskUtil.maskEmail(to), e);
            throw new RuntimeException(e);
        }
    }

}
