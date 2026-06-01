package com.tm.upwork.domain.job.client.upwork;

import com.tm.upwork.domain.search.SearchCriteriaService;
import com.tm.upwork.domain.search.model.SearchCriteriaDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpworkJobQueryBuilder {

    private static final int PAGE_SIZE = 10;

    private final SearchCriteriaService searchCriteriaService;

    public String buildQuery() {
        SearchCriteriaDto criteria = searchCriteriaService.get();
        var filterBuilder = new StringBuilder();
        if (criteria.getSearchExpression() != null && !criteria.getSearchExpression().isEmpty()) {
            filterBuilder.append(String.format("searchExpression_eq: \"%s\" ", criteria.getSearchExpression()));
        }
        if (criteria.getCategoryIds() != null && !criteria.getCategoryIds().isEmpty()) {
            filterBuilder.append(String.format("categoryIds_any: [%s] ", String.join(",", criteria.getCategoryIds())));
        }
        if (criteria.getLocations() != null && !criteria.getLocations().isEmpty()) {
            filterBuilder.append(String.format("locations_any: [%s] ", String.join(",", criteria.getLocations())));
        }
        if (criteria.getMinFixedPrice() != null) {
            filterBuilder.append(String.format("budgetRange_eq: { rangeStart: %s } ", criteria.getMinFixedPrice()));
        }
        if (criteria.getMinHourlyRate() != null) {
            filterBuilder.append(String.format("hourlyRate_eq: { rangeStart: %s } ", criteria.getMinHourlyRate()));
        }
        filterBuilder.append(String.format("pagination_eq: { after: \"0\", first: %d } ", PAGE_SIZE));

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
                "    pageInfo {" +
                "      hasNextPage" +
                "      endCursor" +
                "    }" +
                "  }" +
                "}";
    }
}
