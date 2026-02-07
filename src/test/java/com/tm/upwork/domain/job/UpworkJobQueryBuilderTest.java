package com.tm.upwork.domain.job;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpworkJobQueryBuilderTest {

    private UpworkJobQueryBuilder queryBuilder;

    @BeforeEach
    void setUp() {
        queryBuilder = new UpworkJobQueryBuilder();
    }

    @Test
    void buildQuery_EmptyCriteria() {
        String query = queryBuilder.buildQuery();

        assertNotNull(query);
        assertTrue(query.contains("marketPlaceJobFilter: {}"));
        assertTrue(query.contains("marketplaceJobPostingsSearch"));
    }

    @Test
    void buildQuery_AllCriteriaSet() {
        ReflectionTestUtils.setField(queryBuilder, "minHourlyRate", 20.0);
        ReflectionTestUtils.setField(queryBuilder, "minFixedPrice", 500.0);
        ReflectionTestUtils.setField(queryBuilder, "categoryIds", List.of("123", "456"));
        ReflectionTestUtils.setField(queryBuilder, "locations", List.of("US", "UK"));
        ReflectionTestUtils.setField(queryBuilder, "searchExpression", "java spring");

        String query = queryBuilder.buildQuery();

        assertNotNull(query);
        assertTrue(query.contains("searchExpression_eq: \"java spring\""));
        assertTrue(query.contains("categoryIds_any: [123,456]"));
        assertTrue(query.contains("locations_any: [US,UK]"));
        assertTrue(query.contains("budgetRange_eq: { rangeStart: 500 }"));
        assertTrue(query.contains("hourlyRate_eq: { rangeStart: 20 }"));
    }

    @Test
    void buildQuery_OnlySearchExpression() {
        ReflectionTestUtils.setField(queryBuilder, "searchExpression", "python");

        String query = queryBuilder.buildQuery();

        assertTrue(query.contains("searchExpression_eq: \"python\""));
        assertTrue(!query.contains("categoryIds_any"));
        assertTrue(!query.contains("locations_any"));
        assertTrue(!query.contains("budgetRange_eq"));
        assertTrue(!query.contains("hourlyRate_eq"));
    }

    @Test
    void buildQuery_OnlyMinFixedPrice() {
        ReflectionTestUtils.setField(queryBuilder, "minFixedPrice", 1000.0);

        String query = queryBuilder.buildQuery();

        assertTrue(query.contains("budgetRange_eq: { rangeStart: 1000 }"));
    }

    @Test
    void buildQuery_OnlyMinHourlyRate() {
        ReflectionTestUtils.setField(queryBuilder, "minHourlyRate", 50.0);

        String query = queryBuilder.buildQuery();

        assertTrue(query.contains("hourlyRate_eq: { rangeStart: 50 }"));
    }
}
