package com.tm.upwork.domain.job;

import com.tm.upwork.domain.job.entity.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<JobDto> getJobs(Pageable pageable) {
        return jobRepository.findAll(pageable)
                .map(jobMapper::mapToDto);
    }
}
