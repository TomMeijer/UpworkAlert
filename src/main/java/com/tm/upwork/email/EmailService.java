package com.tm.upwork.email;

import com.tm.upwork.domain.job.model.JobDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final String from;
    private final String frontendUrl;

    @Autowired
    public EmailService(JavaMailSender mailSender,
                        TemplateEngine templateEngine,
                        @Value("${spring.mail.from}") String from,
                        @Value("${frontend.url}") String frontendUrl) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.from = from;
        this.frontendUrl = frontendUrl;
    }

    public void sendJobNotification(JobDto job, String recipient) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom(from);
            helper.setTo(recipient);
            helper.setSubject("New job: " + job.getTitle());
            helper.setText(buildEmailBody(job), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send email", e);
        }
    }

    private String buildEmailBody(JobDto job) {
        Context context = new Context();
        context.setVariable("job", job);
        context.setVariable("frontendUrl", frontendUrl);
        return templateEngine.process("job-notification", context);
    }
}
