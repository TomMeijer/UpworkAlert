package com.tm.upwork.domain.job;

import com.tm.upwork.domain.job.entity.Job;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    public Job mapToEntity(JobDto dto) {
        Job job = new Job();
        job.setUpworkId(dto.getUpworkId());
        job.setTitle(dto.getTitle());
        job.setDescription(dto.getDescription());
        job.setType(dto.getType());
        job.setHourlyRateMin(dto.getHourlyRateMin());
        job.setHourlyRateMax(dto.getHourlyRateMax());
        job.setFixedPrice(dto.getFixedPrice());
        job.setClientCountry(dto.getClientCountry());
        job.setRequiredSkills(dto.getRequiredSkills());
        job.setUrl(dto.getUrl());
        job.setPublishedOn(dto.getPublishedOn());
        job.setExperienceLevel(dto.getExperienceLevel());
        job.setPaymentVerified(dto.isPaymentVerified());
        job.setClientRating(dto.getClientRating());
        job.setClientTotalSpent(dto.getClientTotalSpent());
        return job;
    }

    public JobDto mapToDto(Job job) {
        var builder = JobDto.builder()
                .id(job.getId())
                .upworkId(job.getUpworkId())
                .title(job.getTitle())
                .description(job.getDescription())
                .type(job.getType())
                .hourlyRateMin(job.getHourlyRateMin())
                .hourlyRateMax(job.getHourlyRateMax())
                .fixedPrice(job.getFixedPrice())
                .clientCountry(job.getClientCountry())
                .requiredSkills(job.getRequiredSkills())
                .url(job.getUrl())
                .publishedOn(job.getPublishedOn())
                .experienceLevel(job.getExperienceLevel())
                .paymentVerified(job.isPaymentVerified())
                .clientRating(job.getClientRating())
                .clientTotalSpent(job.getClientTotalSpent());
        return builder.build();
    }
}
