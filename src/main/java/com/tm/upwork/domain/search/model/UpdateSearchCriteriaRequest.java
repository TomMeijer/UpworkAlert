package com.tm.upwork.domain.search.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateSearchCriteriaRequest {
    private Integer minHourlyRate;
    private Integer minFixedPrice;
    private List<String> categoryIds;
    private List<String> locations;
    private String searchExpression;
}
