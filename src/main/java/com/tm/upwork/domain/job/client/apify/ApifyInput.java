package com.tm.upwork.domain.job.client.apify;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApifyInput {
    private final String query;
    private final String rawUrl;
    private final List<String> experienceLevel;
    private final List<String> jobType;
    private final Boolean paymentVerified;
    private final List<String> fixedPriceRange;
    private final List<String> hourlyRateRange;
    private final List<String> clientHistory;
    private final List<String> location;
    private final List<Map<String, Object>> cookies;
    private final List<Map<String, Object>> customFilters;
    private final Integer page;
    private final Integer pagesToScrape;
    private final Integer perPage;
    private final String sort;
    private final Map<String, Object> maxJobAge;
}
