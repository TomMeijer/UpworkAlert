package com.tm.upwork.domain.job.upwork;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpworkJobQueryBuilder {

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

    public String buildQuery() {
        var filterBuilder = new StringBuilder();
        if (searchExpression != null && !searchExpression.isEmpty()) {
            filterBuilder.append(String.format("searchExpression_eq: \"%s\" ", searchExpression));
        }
        if (categoryIds != null && !categoryIds.isEmpty()) {
            filterBuilder.append(String.format("categoryIds_any: [%s] ", String.join(",", categoryIds)));
        }
        if (locations != null && !locations.isEmpty()) {
            filterBuilder.append(String.format("locations_any: [%s] ", String.join(",", locations)));
        }
        if (minFixedPrice != null && !minFixedPrice.isEmpty()) {
            filterBuilder.append(String.format("budgetRange_eq: { rangeStart: %s } ", minFixedPrice));
        }
        if (minHourlyRate != null && !minHourlyRate.isEmpty()) {
            filterBuilder.append(String.format("hourlyRate_eq: { rangeStart: %s } ", minHourlyRate));
        }
        return "query {" +
                "  marketplaceJobPostingsSearch(" +
                "    marketPlaceJobFilter: {" + filterBuilder + "}" +
                "    searchType: USER_JOBS_SEARCH" +
                "    sortAttributes: { field: RECENCY }" +
                "  ) {" +
                "    edges {" +
                "      node {" +
                "        id" +
                "        title" +
                "        description" +
                "        ciphertext" +
                "        publishedDateTime" +
                "        amount { rawValue currency displayValue }" +
                "        hourlyBudgetType" +
                "        hourlyBudgetMin { rawValue currency displayValue }" +
                "        hourlyBudgetMax { rawValue currency displayValue }" +
                "        client { location { country } }" +
                "        skills { name }" +
                "      }" +
                "    }" +
                "  }" +
                "}";
    }
}
