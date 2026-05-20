package com.tm.upwork.domain.job.client.apify;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApifyInput {
    private String query;
    private String rawUrl;
    private List<String> experienceLevel;
    private List<String> jobType;
    private Boolean paymentVerified;
    private List<String> fixedPriceRange;
    private List<String> hourlyRateRange;
    private List<String> clientHistory;
    private List<String> location;
    private List<Map<String, Object>> cookies;
    private List<Map<String, Object>> customFilters;
    private Integer page;
    private Integer pagesToScrape;
    private Integer perPage;
    private String sort;
    private Map<String, Object> maxJobAge;
}
