package com.tm.upwork.domain.job;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class JobDto {
    private String id;
    private String title;
    private String description;
    private JobType type;
    private Double hourlyRateMin;
    private Double hourlyRateMax;
    private Double fixedPrice;
    private String clientCountry;
    private List<String> requiredSkills;
    private String url;
    private LocalDateTime publishedOn;
    private String experienceLevel;
    private boolean paymentVerified;
    private Double clientRating;
    private Double clientTotalSpent;

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
