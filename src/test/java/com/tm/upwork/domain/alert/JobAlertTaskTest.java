package com.tm.upwork.domain.alert;

import com.tm.upwork.domain.job.Job;
import com.tm.upwork.domain.job.JobService;
import com.tm.upwork.email.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
    private JobService jobService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private JobAlertTask jobAlertTask;

    private Job job1;
    private Job job2;

    @BeforeEach
    void setUp() {
        job1 = new Job();
        job1.setId("job1");
        job1.setTitle("Title 1");

        job2 = new Job();
        job2.setId("job2");
        job2.setTitle("Title 2");
    }

    @Test
    void checkForNewJobs_shouldSendEmailForNewJobs() {
        // Given
        when(jobService.fetchNewJobs()).thenReturn(List.of(job1, job2));

        // When
        jobAlertTask.checkForNewJobs();

        // Then
        verify(emailService).sendJobNotification(job1);
        verify(emailService).sendJobNotification(job2);
        verifyNoMoreInteractions(emailService);
    }

    @Test
    void checkForNewJobs_shouldNotSendEmailForAlreadyProcessedJobs() {
        // Given
        when(jobService.fetchNewJobs()).thenReturn(List.of(job1));

        // First run
        jobAlertTask.checkForNewJobs();
        verify(emailService).sendJobNotification(job1);

        // Second run with same job
        when(jobService.fetchNewJobs()).thenReturn(List.of(job1));
        jobAlertTask.checkForNewJobs();

        // Should still only have called sendJobNotification once in total
        verify(emailService, times(1)).sendJobNotification(job1);
    }

    @Test
    void checkForNewJobs_shouldEvictOldestWhenLimitExceeded() {
        // Given
        Queue<String> processedJobIds = (Queue<String>) ReflectionTestUtils.getField(jobAlertTask, "processedJobIds");
        for (int i = 0; i < 1000; i++) {
            processedJobIds.add("old_job_" + i);
        }

        // The next job added should evict "old_job_0"
        when(jobService.fetchNewJobs()).thenReturn(List.of(job1));

        // When
        jobAlertTask.checkForNewJobs();

        // Then
        assertEquals(1000, processedJobIds.size());
        assertFalse(processedJobIds.contains("old_job_0"));
        assertTrue(processedJobIds.contains("old_job_1"));
        assertTrue(processedJobIds.contains("job1"));
        verify(emailService).sendJobNotification(job1);
    }

    @Test
    void checkForNewJobs_shouldHandleExceptionsGracefully() {
        // Given
        when(jobService.fetchNewJobs()).thenThrow(new RuntimeException("API Error"));

        // When
        jobAlertTask.checkForNewJobs();

        // Then
        verifyNoInteractions(emailService);
    }
}
