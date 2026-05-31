package com.tm.upwork.domain.search;

import com.tm.upwork.util.StringListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class SearchCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer minHourlyRate;
    private Integer minFixedPrice;

    @Convert(converter = StringListConverter.class)
    private List<String> categoryIds;

    @Convert(converter = StringListConverter.class)
    private List<String> locations;

    private String searchExpression;
}
