package com.tm.upwork.domain.job.apify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApifyInputBuilderTest {

    private ApifyInputBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new ApifyInputBuilder();
    }

    @Test
    void testBuild_Default() {
        ReflectionTestUtils.setField(builder, "locations", List.of());
        ApifyInput input = builder.build();

        assertNotNull(input);
        assertNotNull(input.getRawUrl());
        assertNull(input.getQuery());
        assertNull(input.getLocation());
    }

    @Test
    void testBuild_AllCriteria() {
        ReflectionTestUtils.setField(builder, "minHourlyRate", "35");
        ReflectionTestUtils.setField(builder, "minFixedPrice", "100");
        ReflectionTestUtils.setField(builder, "categoryIds", "531770282580668418");
        ReflectionTestUtils.setField(builder, "locations", List.of("Asia", "Europe", "Oceania"));
        ReflectionTestUtils.setField(builder, "searchExpression", "(Java OR Spring OR Angular)");

        ApifyInput input = builder.build();

        assertEquals("https://www.upwork.com/nx/search/jobs/?amount=100-&category2_uid=531770282580668418&hourly_rate=35-&location=Asia,Europe,Oceania&q=(Java%20OR%20Spring%20OR%20Angular)&sort=recency&t=0,1&page=1&per_page=50", input.getRawUrl());
        assertNull(input.getQuery());
        assertNull(input.getLocation());
        assertNull(input.getFixedPriceRange());
        assertNull(input.getHourlyRateRange());
    }

    @Test
    void testBuild_PartialCriteria() {
        ReflectionTestUtils.setField(builder, "searchExpression", "Angular");
        ReflectionTestUtils.setField(builder, "locations", List.of());

        ApifyInput input = builder.build();

        assertNotNull(input.getRawUrl());
        assertTrue(input.getRawUrl().contains("q=Angular"));
        assertNull(input.getQuery());
    }
}
