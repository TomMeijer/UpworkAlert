package com.tm.upwork.domain.search;

import com.tm.upwork.domain.search.entity.SearchCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SearchCriteriaRepository extends JpaRepository<SearchCriteria, Integer> {

    Optional<SearchCriteria> findFirstByOrderByIdAsc();
}
