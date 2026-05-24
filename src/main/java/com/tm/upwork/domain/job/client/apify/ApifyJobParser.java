package com.tm.upwork.domain.job.client.apify;

import com.tm.upwork.domain.job.JobDto;
import com.tm.upwork.domain.job.JobType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ApifyJobParser {

    public List<JobDto> parseJobs(List<ApifyJob> apifyJobs) {
        return apifyJobs.stream()
                .map(this::mapToJob)
                .toList();
    }

    public JobDto mapToJob(ApifyJob apifyJob) {
        JobDto job = new JobDto();
        job.setId(apifyJob.getId());
        job.setTitle(apifyJob.getTitle());
        job.setDescription(apifyJob.getDescription());
        job.setUrl(apifyJob.getUrl());
        if (apifyJob.getAbsoluteDate() != null && !apifyJob.getAbsoluteDate().isEmpty()) {
            job.setPublishedOn(LocalDateTime.parse(apifyJob.getAbsoluteDate(), DateTimeFormatter.ISO_DATE_TIME));
        }
        job.setClientCountry(apifyJob.getClientLocation());
        job.setRequiredSkills(apifyJob.getTags());
        job.setExperienceLevel(apifyJob.getExperienceLevel());
        job.setClientRating(apifyJob.getClientRating());
        job.setClientTotalSpent(apifyJob.getClientTotalSpent());

        if (apifyJob.getPaymentVerified() instanceof Boolean paymentVerifiedBool) {
            job.setPaymentVerified(paymentVerifiedBool);
        } else if (apifyJob.getPaymentVerified() instanceof String paymentVerifiedString) {
            job.setPaymentVerified("VERIFIED".equalsIgnoreCase(paymentVerifiedString));
        }

        if ("Fixed".equals(apifyJob.getJobType())) {
            job.setType(JobType.FIXED);
            job.setFixedPrice(parseAmount(apifyJob.getBudget()));
        } else if ("Hourly".equals(apifyJob.getJobType())) {
            job.setType(JobType.HOURLY);
            if (apifyJob.getBudget() != null && !apifyJob.getBudget().isBlank()) {
                String[] range = apifyJob.getBudget().split("-");
                job.setHourlyRateMin(parseAmount(range[0]));
                if (range.length > 1) {
                    job.setHourlyRateMax(parseAmount(range[1]));
                }
            }
        }
        return job;
    }

    private Double parseAmount(String input) {
        if (input == null || input.isBlank()) {
            return null;
        }
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group());
        }
        return null;
    }
}
