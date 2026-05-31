package com.tm.upwork.domain.job.client;

import com.tm.upwork.domain.job.entity.JobStatus;
import com.tm.upwork.domain.job.entity.JobType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class UpworkJob {
    private final String id;
    private final String title;
    private final String description;
    private final JobType type;
    private final JobStatus status;
    private final Double hourlyRateMin;
    private final Double hourlyRateMax;
    private final Double fixedPrice;
    private final String clientCountry;
    private final List<String> requiredSkills;
    private final String url;
    private final LocalDateTime publishedOn;
    private final String experienceLevel;
    private final Boolean paymentVerified;
    private final Double clientRating;
    private final Double clientTotalSpent;
}
