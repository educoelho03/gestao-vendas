package br.com.ms_notificacoes.service;

import br.com.ms_notificacoes.dto.ClienteSaveDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendgrid.Method;
import com.sendgrid.Request;
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

    private final SendGrid sendGrid;
    private final Email fromEmail;
    private ObjectMapper objectMapper;

    public EmailService(ObjectMapper objectMapper, Email fromEmail, SendGrid sendGrid) {
        this.objectMapper = objectMapper;
        this.fromEmail = fromEmail;
        this.sendGrid = sendGrid;
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
            throw new RuntimeException(e);
        }
    }

    public void sendMail(String to, String subject, String body){
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(fromEmail, subject, toEmail, content);

        try {
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint(MAIL_ENDPOINT);
            request.setBody(mail.build());

            sendGrid.api(request);
            log.info("E-mail enviado com sucesso para: {}", to);
        } catch (IOException e) {
            log.error("Erro ao enviar e-mail para: {}", to, e);
            throw new RuntimeException(e);
        }
    }

}
