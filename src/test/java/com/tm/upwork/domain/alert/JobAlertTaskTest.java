package com.tm.upwork.domain.alert;

import com.tm.upwork.domain.job.JobDto;
import com.tm.upwork.domain.job.JobService;
import com.tm.upwork.domain.job.client.JobClient;
import com.tm.upwork.email.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobAlertTaskTest {

    @Mock
    private JobClient jobClient;

    @Mock
    private JobService jobService;

    @Mock
    private EmailService emailService;

    private JobAlertTask jobAlertTask;

    private JobDto job1;
    private JobDto job2;

    @BeforeEach
    void setUp() {
        jobAlertTask = new JobAlertTask(jobClient, jobService, emailService, true);

        job1 = new JobDto();
        job1.setId("job1");
        job1.setTitle("Title 1");

        job2 = new JobDto();
        job2.setId("job2");
        job2.setTitle("Title 2");
    }

    @Test
    void checkForNewJobs_shouldSaveAndSendEmailForNewJobs() {
        // Given
        when(jobClient.fetchNewJobs()).thenReturn(List.of(job1, job2));

        // When
        jobAlertTask.checkForNewJobs();

        // Then
        verify(jobService).saveJob(job1);
        verify(jobService).saveJob(job2);
        verify(emailService).sendJobNotification(job1);
        verify(emailService).sendJobNotification(job2);
        verifyNoMoreInteractions(emailService);
    }

    @Test
    void checkForNewJobs_shouldNotSaveOrSendEmailForAlreadyProcessedJobs() {
        // Given
        when(jobClient.fetchNewJobs()).thenReturn(List.of(job1));

        // First run
        jobAlertTask.checkForNewJobs();
        verify(jobService).saveJob(job1);
        verify(emailService).sendJobNotification(job1);

        // Second run with same job
        when(jobClient.fetchNewJobs()).thenReturn(List.of(job1));
        jobAlertTask.checkForNewJobs();

        // Should still only have called saveJob and sendJobNotification once in total
        verify(jobService, times(1)).saveJob(job1);
        verify(emailService, times(1)).sendJobNotification(job1);
    }

    @Test
    void checkForNewJobs_shouldEvictOldestWhenLimitExceeded() {
        // Given
        Queue<String> processedJobIds = (Queue<String>) ReflectionTestUtils.getField(jobAlertTask, "processedJobIds");
        for (int i = 0; i < 100; i++) {
            processedJobIds.add("old_job_" + i);
        }

        // The next job added should evict "old_job_0"
        when(jobClient.fetchNewJobs()).thenReturn(List.of(job1));

        // When
        jobAlertTask.checkForNewJobs();

        // Then
        assertEquals(100, processedJobIds.size());
        assertFalse(processedJobIds.contains("old_job_0"));
        assertTrue(processedJobIds.contains("old_job_1"));
        assertTrue(processedJobIds.contains("job1"));
        verify(jobService).saveJob(job1);
        verify(emailService).sendJobNotification(job1);
    }

    @Test
    void checkForNewJobs_shouldHandleExceptionsGracefully() {
        // Given
        when(jobClient.fetchNewJobs()).thenThrow(new RuntimeException("API Error"));

        // When
        jobAlertTask.checkForNewJobs();

        // Then
        verifyNoInteractions(jobService);
        verifyNoInteractions(emailService);
    }

    @Test
    void checkForNewJobs_shouldNotSendEmailWhenDisabled() {
        // Given
        jobAlertTask = new JobAlertTask(jobClient, jobService, emailService, false);
        when(jobClient.fetchNewJobs()).thenReturn(List.of(job1));

        // When
        jobAlertTask.checkForNewJobs();

        // Then
        verify(jobService).saveJob(job1);
        verifyNoInteractions(emailService);
    }
}
