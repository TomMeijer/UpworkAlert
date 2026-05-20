package com.tm.upwork.domain.job.client.apify;

import com.tm.upwork.domain.job.JobDto;
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
class ApifyJobClientTest {

    @Mock
    private ApifyJobDownloader apifyJobDownloader;

    @Mock
    private ApifyJobParser apifyJobParser;

    @Mock
    private ApifyInputBuilder apifyInputBuilder;

    @InjectMocks
    private ApifyJobClient apifyJobService;

    @Test
    void testFetchNewJobs() {
        // Given
        ApifyInput mockInput = ApifyInput.builder().build();
        List<ApifyJob> mockApifyJobs = List.of(new ApifyJob());
        List<JobDto> expectedJobs = List.of(new JobDto());

        when(apifyInputBuilder.build()).thenReturn(mockInput);
        when(apifyJobDownloader.runSyncGetDatasetItems(mockInput)).thenReturn(mockApifyJobs);
        when(apifyJobParser.parseJobs(mockApifyJobs)).thenReturn(expectedJobs);

        // When
        List<JobDto> actualJobs = apifyJobService.fetchNewJobs();

        // Then
        assertNotNull(actualJobs);
        assertEquals(expectedJobs, actualJobs);

        verify(apifyInputBuilder).build();
        verify(apifyJobDownloader).runSyncGetDatasetItems(mockInput);
        verify(apifyJobParser).parseJobs(mockApifyJobs);
    }
}
