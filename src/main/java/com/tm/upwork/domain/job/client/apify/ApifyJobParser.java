package com.tm.upwork.domain.job.client.apify;

import com.tm.upwork.domain.job.JobDto;
import com.tm.upwork.domain.job.JobType;
import org.springframework.stereotype.Component;

import java.util.List;

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
        job.setPublishedOn(apifyJob.getAbsoluteDate());
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
            if (apifyJob.getBudget() != null && !apifyJob.getBudget().isEmpty()) {
                job.setFixedPrice(Double.parseDouble(apifyJob.getBudget().replaceAll("[^\\d.]", "")));
            }
        } else if ("Hourly".equals(apifyJob.getJobType())) {
            job.setType(JobType.HOURLY);
            if (apifyJob.getBudget() != null && !apifyJob.getBudget().isEmpty()) {
                String[] range = apifyJob.getBudget().split("-");
                job.setHourlyRateMin(Double.parseDouble(range[0].replaceAll("[^\\d.]", "")));
                if (range.length > 1) {
                    job.setHourlyRateMax(Double.parseDouble(range[1].replaceAll("[^\\d.]", "")));
                }
            }
        }
        return job;
    }
}
