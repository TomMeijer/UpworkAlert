package com.tm.upwork.email;

import com.tm.upwork.domain.job.Job;
import com.tm.upwork.domain.job.JobType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

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
        Job job = new Job();
        job.setTitle("Java Developer");
        job.setType(JobType.FIXED);
        job.setFixedPrice(500.0);
        job.setClientCountry("United States");
        job.setRequiredSkills(List.of("Java", "Spring Boot"));
        job.setUrl("https://upwork.com/jobs/123");
        job.setDescription("Looking for a Java developer with Spring Boot experience.");

        // When
        emailService.sendJobNotification(job);

        // Then
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertThat(sentMessage.getFrom()).isEqualTo(fromEmail);
        assertThat(sentMessage.getTo()).containsExactly(toEmail);
        assertThat(sentMessage.getSubject()).isEqualTo("New Upwork Job: Java Developer");
        
        String body = sentMessage.getText();
        assertThat(body).contains("Title: Java Developer");
        assertThat(body).contains("Price: Fixed: $500.0");
        assertThat(body).contains("Client Country: United States");
        assertThat(body).contains("Skills: Java, Spring Boot");
        assertThat(body).contains("Link: https://upwork.com/jobs/123");
        assertThat(body).contains("Description:\nLooking for a Java developer with Spring Boot experience.");
    }
}
