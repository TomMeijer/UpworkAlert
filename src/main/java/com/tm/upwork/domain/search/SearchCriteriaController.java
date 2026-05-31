package com.tm.upwork.domain.search;

import com.tm.upwork.domain.search.model.SearchCriteriaDto;
import com.tm.upwork.domain.search.model.UpdateSearchCriteriaParams;
import com.tm.upwork.domain.search.model.UpdateSearchCriteriaRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search-criteria")
@RequiredArgsConstructor
public class SearchCriteriaController {

    private final SearchCriteriaService searchCriteriaService;
    private final SearchCriteriaMapper searchCriteriaMapper;

    @GetMapping
    public SearchCriteriaDto get() {
        return searchCriteriaMapper.toDto(searchCriteriaService.get());
    }

    @PutMapping
    public void update(@RequestBody @Valid UpdateSearchCriteriaRequest request) {
        searchCriteriaService.update(new UpdateSearchCriteriaParams(
                request.getMinHourlyRate(),
                request.getMinFixedPrice(),
                request.getCategoryIds(),
                request.getLocations(),
                request.getSearchExpression()
        ));
    }
}
