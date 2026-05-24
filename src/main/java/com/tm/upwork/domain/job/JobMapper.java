package com.tm.upwork.domain.job;

import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    public Job mapToEntity(JobDto dto) {
        Job job = new Job();
        job.setUpworkId(dto.getId());
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
        JobDto dto = new JobDto();
        dto.setId(job.getUpworkId());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setType(job.getType());
        dto.setHourlyRateMin(job.getHourlyRateMin());
        dto.setHourlyRateMax(job.getHourlyRateMax());
        dto.setFixedPrice(job.getFixedPrice());
        dto.setClientCountry(job.getClientCountry());
        dto.setRequiredSkills(job.getRequiredSkills());
        dto.setUrl(job.getUrl());
        dto.setPublishedOn(job.getPublishedOn());
        dto.setExperienceLevel(job.getExperienceLevel());
        dto.setPaymentVerified(job.isPaymentVerified());
        dto.setClientRating(job.getClientRating());
        dto.setClientTotalSpent(job.getClientTotalSpent());
        return dto;
    }
}
