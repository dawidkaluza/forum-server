package pl.dkaluza.forum.core.mail;


import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.Arrays;

@Component
@Profile("dev")
public class FakeToConsoleMailSender implements JavaMailSender {
    @Override
    public MimeMessage createMimeMessage() {
        throw new UnsupportedOperationException("Operation is unsupported");
    }

    @Override
    public MimeMessage createMimeMessage(InputStream inputStream) throws MailException {
        throw new UnsupportedOperationException("Operation is unsupported");
    }

    @Override
    public void send(MimeMessage mimeMessage) throws MailException {
        throw new UnsupportedOperationException("Operation is unsupported");
    }

    @Override
    public void send(MimeMessage... mimeMessages) throws MailException {
        throw new UnsupportedOperationException("Operation is unsupported");
    }

    @Override
    public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {
        throw new UnsupportedOperationException("Operation is unsupported");
    }

    @Override
    public void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {
        throw new UnsupportedOperationException("Operation is unsupported");
    }

    @Override
    public void send(SimpleMailMessage message) throws MailException {
        System.out.println("= = = = = = = = = = = = = = = = = = = = = = = =");
        System.out.println("*** NEW E-MAIL MESSAGE ****");
        System.out.println("From: " + message.getFrom());
        System.out.println("To: " + Arrays.toString(message.getTo()));
        System.out.println("Subject: " + message.getSubject());
        System.out.println("Text: " + message.getText());
        System.out.println("= = = = = = = = = = = = = = = = = = = = = = = =");
    }

    @Override
    public void send(SimpleMailMessage... messages) throws MailException {
        for (SimpleMailMessage message : messages) {
            send(message);
        }
    }
}
