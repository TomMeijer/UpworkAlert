package com.tm.upwork.domain.search.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class SearchCriteriaDto {
    private final Integer minHourlyRate;
    private final Integer minFixedPrice;
    private final List<String> categoryIds;
    private final List<String> locations;
    private final String searchExpression;
}
