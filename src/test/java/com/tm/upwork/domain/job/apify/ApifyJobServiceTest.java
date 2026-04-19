package com.tm.upwork.domain.job.apify;

import com.tm.upwork.domain.job.Job;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
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

        when(apifyInputBuilder.build(Map.of("value", 1, "unit", "hours"))).thenReturn(input);
        when(apifyJobClient.runSyncGetDatasetItems(input)).thenReturn(apifyJobs);
        when(apifyJobParser.parseJobs(apifyJobs)).thenReturn(expectedJobs);

        // When
        List<Job> result = apifyJobService.fetchNewJobs();

        // Then
        assertEquals(expectedJobs, result);
        verify(apifyInputBuilder).build(Map.of("value", 1, "unit", "hours"));
        verify(apifyJobClient).runSyncGetDatasetItems(input);
        verify(apifyJobParser).parseJobs(apifyJobs);
    }

    @Test
    void fetchNewJobs_ShouldIncludeMaxJobAgeInSecondCall() {
        // Given
        ApifyInput input1 = ApifyInput.builder().build();
        ApifyInput input2 = ApifyInput.builder().build();

        when(apifyInputBuilder.build(Map.of("value", 1, "unit", "hours"))).thenReturn(input1);
        when(apifyInputBuilder.build(argThat(map -> map != null && "minutes".equals(map.get("unit"))))).thenReturn(input2);

        // When
        apifyJobService.fetchNewJobs(); // First call, lastRetrievalTime is set
        apifyJobService.fetchNewJobs(); // Second call, should have maxJobAge

        // Then
        verify(apifyInputBuilder).build(Map.of("value", 1, "unit", "hours"));
        verify(apifyInputBuilder).build(argThat(map -> map != null && "minutes".equals(map.get("unit"))));
    }
}
