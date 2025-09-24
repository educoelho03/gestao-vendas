package br.com.ms_notificacoes.service;

import br.com.ms_notificacoes.dto.ClienteSaveDto;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    private static final String MAIL_ENDPOINT = "mail/send";
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final SendGrid sendGrid;
    private final Email fromEmail;

    public EmailService(SendGrid sendGrid, Email fromEmail) {
        this.sendGrid = sendGrid;
        this.fromEmail = fromEmail;
    }

    @KafkaListener(topics = "cliente-added-topic", groupId = "cliente-group")
    public void sendWelcomeEmail(ClienteSaveDto clienteDto) {
        log.info("Mensagem recebida do Kafka - Cliente: {}", clienteDto.getNome());
        if (clienteDto.getEmail() == null || clienteDto.getEmail().isEmpty()) {
            log.error("E-mail não encontrado para o cliente: {}", clienteDto.getNome());
            return;
        }

        String to = clienteDto.getEmail();
        String subject = "Bem-vindo ao Sistema de Gestão de Vendas!";
        String body = "Olá " + clienteDto.getNome() + " %s! Sua conta foi criada com sucesso.\n\n" +
                        "Agradecemos por se cadastrar em nosso sistema.\n";

        sendMail(to, subject, body);
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
