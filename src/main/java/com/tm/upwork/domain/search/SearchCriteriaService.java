package com.tm.upwork.domain.search;

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
    public void update(UpdateSearchCriteriaRequest request) {
        SearchCriteria entity = get();
        entity.setMinHourlyRate(request.getMinHourlyRate());
        entity.setMinFixedPrice(request.getMinFixedPrice());
        entity.setCategoryIds(request.getCategoryIds());
        entity.setLocations(request.getLocations());
        entity.setSearchExpression(request.getSearchExpression());
        searchCriteriaRepository.save(entity);
    }
}
