package com.tm.upwork.email;

import com.tm.upwork.domain.job.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String from;

    @Value("${spring.mail.to}")
    private String to;

    public void sendJobNotification(Job job) {
        var message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("New Upwork Job: " + job.getTitle());
        message.setText(buildEmailBody(job));
        mailSender.send(message);
    }

    private String buildEmailBody(Job job) {
        return String.format(
                "Title: %s\n" +
                "Price: %s\n" +
                "Client Country: %s\n" +
                "Skills: %s\n" +
                "Link: %s\n\n" +
                "Description:\n%s",
                job.getTitle(),
                job.getPriceString(),
                job.getClientCountry(),
                String.join(", ", job.getRequiredSkills()),
                job.getUrl(),
                job.getDescription()
        );
    }
}
