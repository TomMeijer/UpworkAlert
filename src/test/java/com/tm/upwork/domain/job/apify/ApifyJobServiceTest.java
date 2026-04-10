package com.tm.upwork.domain.job.apify;

import com.tm.upwork.domain.job.Job;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApifyJobServiceTest {

    @Mock
    private ApifyJobClient apifyJobClient;

    @Mock
    private ApifyJobParser apifyJobParser;

    @Mock
    private ApifyInputBuilder apifyInputBuilder;

    @InjectMocks
    private ApifyJobService apifyJobService;

    @Test
    void fetchNewJobs_ShouldReturnParsedJobs() {
        // Given
        ApifyInput input = ApifyInput.builder().query("Java").build();
        ApifyJob apifyJob = new ApifyJob();
        apifyJob.setId("123");
        List<ApifyJob> apifyJobs = List.of(apifyJob);
        Job job = new Job();
        job.setId("123");
        List<Job> expectedJobs = List.of(job);

        when(apifyInputBuilder.build()).thenReturn(input);
        when(apifyJobClient.runSyncGetDatasetItems(input)).thenReturn(apifyJobs);
        when(apifyJobParser.parseJobs(apifyJobs)).thenReturn(expectedJobs);

        // When
        List<Job> result = apifyJobService.fetchNewJobs();

        // Then
        assertEquals(expectedJobs, result);
        verify(apifyInputBuilder).build();
        verify(apifyJobClient).runSyncGetDatasetItems(input);
        verify(apifyJobParser).parseJobs(apifyJobs);
    }
}
