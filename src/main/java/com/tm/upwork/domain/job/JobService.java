package com.tm.upwork.domain.job;

import com.tm.upwork.domain.job.client.UpworkJob;
import com.tm.upwork.domain.job.entity.Job;
import com.tm.upwork.domain.job.entity.JobStatus;
import com.tm.upwork.domain.job.model.JobDto;
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
    public JobDto save(UpworkJob upworkJob) {
        var job = new Job();
        job.setUpworkId(upworkJob.getId());
        job.setTitle(upworkJob.getTitle());
        job.setDescription(upworkJob.getDescription());
        job.setType(upworkJob.getType());
        job.setStatus(upworkJob.getStatus());
        job.setHourlyRateMin(upworkJob.getHourlyRateMin());
        job.setHourlyRateMax(upworkJob.getHourlyRateMax());
        job.setFixedPrice(upworkJob.getFixedPrice());
        job.setClientCountry(upworkJob.getClientCountry());
        job.setRequiredSkills(upworkJob.getRequiredSkills());
        job.setUrl(upworkJob.getUrl());
        job.setPublishedOn(upworkJob.getPublishedOn());
        job.setExperienceLevel(upworkJob.getExperienceLevel());
        job.setPaymentVerified(upworkJob.getPaymentVerified());
        job.setClientRating(upworkJob.getClientRating());
        job.setClientTotalSpent(upworkJob.getClientTotalSpent());
        job = jobRepository.save(job);
        return jobMapper.toDto(job);
    }

    public Page<JobDto> getPage(Pageable pageable) {
        return jobRepository.findAll(pageable).map(jobMapper::toDto);
    }

    public JobDto getById(int id) {
        return jobRepository.findById(id)
                .map(jobMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + id));
    }

    @Transactional
    public void delete(int id) {
        jobRepository.deleteById(id);
    }

    @Transactional
    public void updateStatus(int id, JobStatus status) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + id));
        job.setStatus(status);
        jobRepository.save(job);
    }
}
