package com.tm.upwork.domain.search;

import org.springframework.stereotype.Component;

@Component
public class SearchCriteriaMapper {

    public SearchCriteriaDto toDto(SearchCriteria entity) {
        return SearchCriteriaDto.builder()
                .minHourlyRate(entity.getMinHourlyRate())
                .minFixedPrice(entity.getMinFixedPrice())
                .categoryIds(entity.getCategoryIds())
                .locations(entity.getLocations())
                .searchExpression(entity.getSearchExpression())
                .build();
    }
}
