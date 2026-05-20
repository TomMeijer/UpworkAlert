package com.tm.upwork.domain.job.client.apify;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Component
public class ApifyInputBuilder {
    private static final Map<String, Object> MAX_JOB_AGE = Map.of("value", 1, "unit", "hours");
    private static final int PAGE_SIZE = 10;

    @Value("${search.criteria.min-hourly-rate}")
    private String minHourlyRate;

    @Value("${search.criteria.min-fixed-price}")
    private String minFixedPrice;

    @Value("${search.criteria.category-ids}")
    private List<String> categoryIds;

    @Value("${search.criteria.locations}")
    private List<String> locations;

    @Value("${search.criteria.searchExpression}")
    private String searchExpression;

    public ApifyInput build() {
        var builder = UriComponentsBuilder.fromUriString("https://www.upwork.com/nx/search/jobs/");
        if (minFixedPrice != null && !minFixedPrice.isEmpty()) {
            builder.queryParam("amount", minFixedPrice + "-");
        }
        if (categoryIds != null && !categoryIds.isEmpty()) {
            builder.queryParam("category2_uid", String.join(",", categoryIds));
        }
        if (minHourlyRate != null && !minHourlyRate.isEmpty()) {
            builder.queryParam("hourly_rate", minHourlyRate + "-");
        }
        if (locations != null && !locations.isEmpty()) {
            builder.queryParam("location", String.join(",", locations));
        }
        if (searchExpression != null && !searchExpression.isEmpty()) {
            builder.queryParam("q", searchExpression);
        }
        String rawUrl = builder
                .queryParam("sort", "recency")
                .queryParam("t", "0,1")
                .queryParam("page", 1)
                .queryParam("per_page", PAGE_SIZE)
                .build()
                .encode()
                .toUriString();

        return ApifyInput.builder()
                .rawUrl(rawUrl)
                .maxJobAge(MAX_JOB_AGE)
                .build();
    }
}
