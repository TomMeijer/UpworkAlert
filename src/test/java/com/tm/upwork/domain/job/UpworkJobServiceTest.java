package com.tm.upwork.domain.job;

import com.Upwork.api.OAuthClient;
import com.Upwork.api.Routers.Graphql;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpworkJobServiceTest {

    @Mock
    private OAuthClient oauthClient;

    @Mock
    private UpworkJobParser jobParser;

    @Mock
    private UpworkJobQueryBuilder queryBuilder;

    @InjectMocks
    private UpworkJobService upworkJobService;

    @Test
    void testFetchNewJobsSuccess() throws JSONException {
        // Given
        String query = "mock-query";
        when(queryBuilder.buildQuery()).thenReturn(query);

        JSONObject mockResponse = new JSONObject();
        List<Job> expectedJobs = Collections.singletonList(new Job());
        when(jobParser.parseJobs(any(JSONObject.class))).thenReturn(expectedJobs);

        try (MockedConstruction<Graphql> mocked = mockConstruction(Graphql.class,
                (mock, context) -> {
                    when(mock.Execute(any())).thenReturn(mockResponse);
                })) {

            // When
            List<Job> actualJobs = upworkJobService.fetchNewJobs();

            // Then
            assertNotNull(actualJobs);
            assertEquals(expectedJobs.size(), actualJobs.size());
            assertEquals(expectedJobs, actualJobs);

            verify(queryBuilder).buildQuery();
            verify(jobParser).parseJobs(mockResponse);
            
            // Verify Graphql construction and call
            assertEquals(1, mocked.constructed().size());
            Graphql graphqlMock = mocked.constructed().getFirst();
            
            var expectedParams = new HashMap<String, String>();
            expectedParams.put("query", query);
            verify(graphqlMock).Execute(expectedParams);
        }
    }

    @Test
    void testFetchNewJobsThrowsException() {
        // Given
        when(queryBuilder.buildQuery()).thenReturn("query");

        try (MockedConstruction<Graphql> mocked = mockConstruction(Graphql.class,
                (mock, context) -> {
                    when(mock.Execute(any())).thenThrow(new JSONException("GraphQL error"));
                })) {

            // When & Then
            assertThrows(JSONException.class, () -> {
                upworkJobService.fetchNewJobs();
            });

            verify(queryBuilder).buildQuery();
            verifyNoInteractions(jobParser);
        }
    }
}
