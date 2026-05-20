package com.tm.upwork.email;

import com.tm.upwork.domain.job.JobDto;
import com.tm.upwork.domain.job.JobType;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private EmailService emailService;

    private final String fromEmail = "from@example.com";
    private final String toEmail = "to@example.com";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "from", fromEmail);
        ReflectionTestUtils.setField(emailService, "to", toEmail);
    }

    @Test
    void shouldSendJobNotification() {
        // Given
        JobDto job = new JobDto();
        job.setTitle("Java Developer");
        job.setType(JobType.FIXED);
        job.setFixedPrice(500.0);
        job.setClientCountry("United States");
        job.setRequiredSkills(List.of("Java", "Spring Boot"));
        job.setUrl("https://upwork.com/jobs/123");
        job.setDescription("Looking for a Java developer with Spring Boot experience.");

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("job-notification"), any(Context.class))).thenReturn("<html>HTML Body</html>");

        // When
        emailService.sendJobNotification(job);

        // Then
        verify(mailSender).send(mimeMessage);
        verify(templateEngine).process(eq("job-notification"), any(Context.class));
    }
}
