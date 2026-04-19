package com.tm.upwork.domain.job.apify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApifyInputBuilderTest {

    private ApifyInputBuilder apifyInputBuilder;

    @BeforeEach
    void setUp() {
        apifyInputBuilder = new ApifyInputBuilder();
    }

    @Test
    void testBuildWithAllValues() {
        // Given
        ReflectionTestUtils.setField(apifyInputBuilder, "minHourlyRate", "20");
        ReflectionTestUtils.setField(apifyInputBuilder, "minFixedPrice", "500");
        ReflectionTestUtils.setField(apifyInputBuilder, "categoryIds", List.of("123", "456"));
        ReflectionTestUtils.setField(apifyInputBuilder, "locations", List.of("USA", "UK"));
        ReflectionTestUtils.setField(apifyInputBuilder, "searchExpression", "java spring");

        // When
        ApifyInput result = apifyInputBuilder.build();

        // Then
        assertNotNull(result);
        assertEquals(Map.of("value", 1, "unit", "hours"), result.getMaxJobAge());
        
        String rawUrl = result.getRawUrl();
        assertTrue(rawUrl.contains("https://www.upwork.com/nx/search/jobs/"));
        assertTrue(rawUrl.contains("hourly_rate=20-"));
        assertTrue(rawUrl.contains("amount=500-"));
        assertTrue(rawUrl.contains("category2_uid=123,456"));
        assertTrue(rawUrl.contains("location=USA,UK"));
        assertTrue(rawUrl.contains("q=java%20spring"));
        assertTrue(rawUrl.contains("sort=recency"));
        assertTrue(rawUrl.contains("t=0,1"));
        assertTrue(rawUrl.contains("page=1"));
        assertTrue(rawUrl.contains("per_page=10"));
    }

    @Test
    void testBuildWithEmptyValues() {
        // Given - all fields are null by default or we can set them to empty
        ReflectionTestUtils.setField(apifyInputBuilder, "minHourlyRate", "");
        ReflectionTestUtils.setField(apifyInputBuilder, "minFixedPrice", null);
        ReflectionTestUtils.setField(apifyInputBuilder, "categoryIds", List.of());
        ReflectionTestUtils.setField(apifyInputBuilder, "locations", null);
        ReflectionTestUtils.setField(apifyInputBuilder, "searchExpression", null);

        // When
        ApifyInput result = apifyInputBuilder.build();

        // Then
        assertNotNull(result);
        String rawUrl = result.getRawUrl();
        assertTrue(rawUrl.contains("https://www.upwork.com/nx/search/jobs/"));
        assertFalse(rawUrl.contains("hourly_rate="));
        assertFalse(rawUrl.contains("amount="));
        assertFalse(rawUrl.contains("category2_uid="));
        assertFalse(rawUrl.contains("location="));
        assertFalse(rawUrl.contains("q="));
        
        // Defaults should still be there
        assertTrue(rawUrl.contains("sort=recency"));
        assertTrue(rawUrl.contains("t=0,1"));
        assertTrue(rawUrl.contains("page=1"));
        assertTrue(rawUrl.contains("per_page=10"));
    }
}
