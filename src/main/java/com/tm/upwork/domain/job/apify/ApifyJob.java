package com.tm.upwork.domain.job.apify;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ApifyJob {
    private String id;
    private String subId;
    private String title;
    private String description;
    private String url;
    private String budget;
    private String relativeDate;
    private String absoluteDate;
    private String jobType; // Fixed, Hourly
    private String experienceLevel;
    private String clientLocation;
    private Object paymentVerified; // boolean or string
    private List<String> allowedApplicantCountries;
    private String clientName;
    private Double clientNameConfidence;
    private Double clientAvgHourlyRate;
    private Double clientRating;
    private Double clientHireRatePercent;
    private Double clientTotalSpent;
    private Boolean hasHired;
    private Integer proposals;
    private List<Map<String, Object>> questions;
    private List<String> tags;
}
