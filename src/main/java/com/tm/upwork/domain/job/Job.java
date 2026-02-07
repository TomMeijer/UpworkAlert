package com.tm.upwork.domain.job;

import lombok.Data;
import java.util.List;

@Data
public class Job {
    private String id;
    private String title;
    private String description;
    private Double hourlyRate;
    private Double fixedPrice;
    private String clientCountry;
    private List<String> requiredSkills;
    private String url;
    private String publishedOn;

    public String getPriceString() {
        if (fixedPrice != null) {
            return "$" + fixedPrice + " (Fixed)";
        } else if (hourlyRate != null) {
            return "$" + hourlyRate + "/hr";
        }
        return "Not specified";
    }
}
