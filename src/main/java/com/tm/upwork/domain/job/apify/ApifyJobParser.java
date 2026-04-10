package com.tm.upwork.domain.job.apify;

import com.tm.upwork.domain.job.Job;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApifyJobParser {

    public List<Job> parseJobs(List<ApifyJob> apifyJobs) {
        return apifyJobs.stream()
                .map(this::mapToJob)
                .toList();
    }

    public Job mapToJob(ApifyJob apifyJob) {
        Job job = new Job();
        job.setId(apifyJob.getId());
        job.setTitle(apifyJob.getTitle());
        job.setDescription(apifyJob.getDescription());
        job.setUrl(apifyJob.getUrl());
        job.setPublishedOn(apifyJob.getAbsoluteDate());
        job.setClientCountry(apifyJob.getClientLocation());
        job.setRequiredSkills(apifyJob.getTags());

        if (apifyJob.getBudget() != null) {
            if ("Fixed".equals(apifyJob.getJobType())) {
                job.setFixedPrice(Double.parseDouble(apifyJob.getBudget().replaceAll("[^\\d.]", "")));
            } else if ("Hourly".equals(apifyJob.getJobType())) {
                // Budget might be a range like "$20 - $50" or "$20"
                String numericPart = apifyJob.getBudget().split("-")[0].replaceAll("[^\\d.]", "");
                job.setHourlyRate(Double.parseDouble(numericPart));
            }
        }
        return job;
    }
}
