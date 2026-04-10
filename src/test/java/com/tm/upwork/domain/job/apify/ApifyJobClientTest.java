package com.tm.upwork.domain.job.apify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApifyJobClientTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private ApifyJobClient apifyJobClient;
    private static final String TEST_TOKEN = "test-token";

    @BeforeEach
    void setUp() {
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        apifyJobClient = new ApifyJobClient(webClientBuilder, TEST_TOKEN);
    }

    @Test
    void testRunSyncGetDatasetItems_Success() {
        // Prepare mock data
        ApifyJob job1 = new ApifyJob();
        job1.setId("1");
        job1.setTitle("Job 1");

        ApifyJob job2 = new ApifyJob();
        job2.setId("2");
        job2.setTitle("Job 2");

        ApifyInput input = ApifyInput.builder()
                .query("Java")
                .pagesToScrape(1)
                .build();

        // Mock WebClient fluent API
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(any(Function.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(input)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(ApifyJob.class)).thenReturn(Flux.just(job1, job2));

        // Call the client
        List<ApifyJob> result = apifyJobClient.runSyncGetDatasetItems(input);

        // Verify result
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("Job 1", result.get(0).getTitle());
        assertEquals("2", result.get(1).getId());
        assertEquals("Job 2", result.get(1).getTitle());

        // Verify client interaction
        verify(webClient).post();
    }

    @Test
    void testRunSyncGetDatasetItems_Empty() {
        ApifyInput input = ApifyInput.builder().build();

        // Mock WebClient fluent API for empty result
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(any(Function.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(input)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(ApifyJob.class)).thenReturn(Flux.empty());

        // Call the client
        List<ApifyJob> result = apifyJobClient.runSyncGetDatasetItems(input);

        // Verify result
        assertNotNull(result);
        assertEquals(0, result.size());
    }
}
