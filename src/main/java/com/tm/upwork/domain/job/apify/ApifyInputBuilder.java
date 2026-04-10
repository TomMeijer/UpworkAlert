package com.tm.upwork.domain.job.apify;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
public class ApifyInputBuilder {

    @Value("${search.criteria.min-hourly-rate}")
    private String minHourlyRate;

    @Value("${search.criteria.min-fixed-price}")
    private String minFixedPrice;

    @Value("${search.criteria.category-ids}")
    private String categoryIds;

    @Value("${search.criteria.locations}")
    private List<String> locations;

    @Value("${search.criteria.searchExpression}")
    private String searchExpression;

    public ApifyInput build() {
        String rawUrl = UriComponentsBuilder.fromHttpUrl("https://www.upwork.com/nx/search/jobs/")
                .queryParam("amount", minFixedPrice + "-")
                .queryParam("category2_uid", categoryIds)
                .queryParam("hourly_rate", minHourlyRate + "-")
                .queryParam("location", String.join(",", locations))
                .queryParam("q", searchExpression)
                .queryParam("sort", "recency")
                .queryParam("t", "0,1")
                .queryParam("page", "1")
                .queryParam("per_page", "50")
                .build()
                .encode()
                .toUriString();

        return ApifyInput.builder()
                .rawUrl(rawUrl)
                .build();
    }
}
