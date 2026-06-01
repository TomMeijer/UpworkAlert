package com.tm.upwork.domain.search;

import com.tm.upwork.domain.search.entity.SearchCriteria;
import com.tm.upwork.domain.search.model.SearchCriteriaDto;
import com.tm.upwork.domain.search.model.UpdateSearchCriteriaParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchCriteriaService {

    private final SearchCriteriaRepository searchCriteriaRepository;
    private final SearchCriteriaMapper searchCriteriaMapper;

    public SearchCriteriaDto get() {
        return searchCriteriaRepository.findFirstByOrderByIdAsc()
                .map(searchCriteriaMapper::toDto)
                .orElseThrow(() -> new IllegalStateException("No search criteria found."));
    }

    @Transactional
    public void update(UpdateSearchCriteriaParams params) {
        SearchCriteria entity = searchCriteriaRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new IllegalStateException("No search criteria found."));
        entity.setMinHourlyRate(params.getMinHourlyRate());
        entity.setMinFixedPrice(params.getMinFixedPrice());
        entity.setCategoryIds(params.getCategoryIds());
        entity.setLocations(params.getLocations());
        entity.setSearchExpression(params.getSearchExpression());
        searchCriteriaRepository.save(entity);
    }
}
