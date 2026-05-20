package com.tm.upwork.domain.job;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JobService {
    
    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    @Transactional
    public void saveJob(JobDto jobDto) {
        Job job = jobMapper.mapToEntity(jobDto);
        jobRepository.save(job);
    }
}
