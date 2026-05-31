package com.tm.upwork.domain.job;

import com.tm.upwork.domain.job.entity.Job;
import com.tm.upwork.domain.job.model.JobDto;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    public JobDto toDto(Job job) {
        return JobDto.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .type(job.getType())
                .status(job.getStatus())
                .hourlyRateMin(job.getHourlyRateMin())
                .hourlyRateMax(job.getHourlyRateMax())
                .fixedPrice(job.getFixedPrice())
                .clientCountry(job.getClientCountry())
                .requiredSkills(job.getRequiredSkills())
                .url(job.getUrl())
                .publishedOn(job.getPublishedOn())
                .experienceLevel(job.getExperienceLevel())
                .paymentVerified(job.getPaymentVerified())
                .clientRating(job.getClientRating())
                .clientTotalSpent(job.getClientTotalSpent())
                .build();
    }
}
