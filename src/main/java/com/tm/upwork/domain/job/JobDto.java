package com.tm.upwork.domain.job;

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
public class JobDto {
    private final Integer id;
    private final String upworkId;
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
    private final boolean paymentVerified;
    private final Double clientRating;
    private final Double clientTotalSpent;

    public String getPriceString() {
        if (type == JobType.FIXED) {
            return "Fixed: $" + fixedPrice;
        } else if (type == JobType.HOURLY) {
            var hourlyString = "Hourly: $" + hourlyRateMin;
            if (hourlyRateMax != null) {
                hourlyString += " - $" + hourlyRateMax;
            }
            return hourlyString;
        }
        return "Not specified";
    }
}
