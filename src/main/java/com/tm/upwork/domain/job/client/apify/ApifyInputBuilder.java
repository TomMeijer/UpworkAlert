package com.tm.upwork.domain.job.client.apify;

import com.tm.upwork.domain.search.SearchCriteriaService;
import com.tm.upwork.domain.search.model.SearchCriteriaDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ApifyInputBuilder {

    private static final Map<String, Object> MAX_JOB_AGE = Map.of("value", 1, "unit", "hours");
    private static final int PAGE_SIZE = 10;

    private final SearchCriteriaService searchCriteriaService;

    public ApifyInput build() {
        SearchCriteriaDto criteria = searchCriteriaService.get();
        var builder = UriComponentsBuilder.fromUriString("https://www.upwork.com/nx/search/jobs/");
        if (criteria.getMinFixedPrice() != null) {
            builder.queryParam("amount", criteria.getMinFixedPrice() + "-");
        }
        if (criteria.getCategoryIds() != null && !criteria.getCategoryIds().isEmpty()) {
            builder.queryParam("category2_uid", String.join(",", criteria.getCategoryIds()));
        }
        if (criteria.getMinHourlyRate() != null) {
            builder.queryParam("hourly_rate", criteria.getMinHourlyRate() + "-");
        }
        if (criteria.getLocations() != null && !criteria.getLocations().isEmpty()) {
            builder.queryParam("location", String.join(",", criteria.getLocations()));
        }
        if (criteria.getSearchExpression() != null && !criteria.getSearchExpression().isEmpty()) {
            builder.queryParam("q", criteria.getSearchExpression());
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
