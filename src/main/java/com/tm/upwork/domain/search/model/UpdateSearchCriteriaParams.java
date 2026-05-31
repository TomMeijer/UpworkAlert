package com.tm.upwork.domain.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UpdateSearchCriteriaParams {
    private final Integer minHourlyRate;
    private final Integer minFixedPrice;
    private final List<String> categoryIds;
    private final List<String> locations;
    private final String searchExpression;
}
