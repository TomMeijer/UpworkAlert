package com.tm.upwork.domain.search;

import com.tm.upwork.domain.search.entity.SearchCriteria;
import com.tm.upwork.domain.search.model.UpdateSearchCriteriaParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchCriteriaService {

    private final SearchCriteriaRepository searchCriteriaRepository;

    public SearchCriteria get() {
        return searchCriteriaRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new IllegalStateException("No search criteria found."));
    }

    @Transactional
    public void update(UpdateSearchCriteriaParams params) {
        SearchCriteria entity = get();
        entity.setMinHourlyRate(params.getMinHourlyRate());
        entity.setMinFixedPrice(params.getMinFixedPrice());
        entity.setCategoryIds(params.getCategoryIds());
        entity.setLocations(params.getLocations());
        entity.setSearchExpression(params.getSearchExpression());
        searchCriteriaRepository.save(entity);
    }
}
