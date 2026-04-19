package com.tm.upwork.domain.job.apify;

import com.tm.upwork.domain.job.Job;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void testFetchNewJobs() {
        // Given
        ApifyInput mockInput = ApifyInput.builder().build();
        List<ApifyJob> mockApifyJobs = List.of(new ApifyJob());
        List<Job> expectedJobs = List.of(new Job());

        when(apifyInputBuilder.build()).thenReturn(mockInput);
        when(apifyJobClient.runSyncGetDatasetItems(mockInput)).thenReturn(mockApifyJobs);
        when(apifyJobParser.parseJobs(mockApifyJobs)).thenReturn(expectedJobs);

        // When
        List<Job> actualJobs = apifyJobService.fetchNewJobs();

        // Then
        assertNotNull(actualJobs);
        assertEquals(expectedJobs, actualJobs);

        verify(apifyInputBuilder).build();
        verify(apifyJobClient).runSyncGetDatasetItems(mockInput);
        verify(apifyJobParser).parseJobs(mockApifyJobs);
    }
}
