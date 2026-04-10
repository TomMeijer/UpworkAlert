package com.tm.upwork.domain.alert;

import com.google.common.collect.EvictingQueue;
import com.tm.upwork.domain.job.Job;
import com.tm.upwork.domain.job.JobService;
import com.tm.upwork.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Queue;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobAlertTask {

    private final JobService jobService;
    private final EmailService emailService;
    private final Queue<String> processedJobIds = EvictingQueue.create(1000);

    @Scheduled(fixedRate = 900000) // every 15 minutes
    public void checkForNewJobs() {
        log.info("Checking for new Upwork jobs.");
        try {
            log.info("Fetching jobs from Upwork...");
            List<Job> jobs = jobService.fetchNewJobs();
            log.info("{} jobs fetched.", jobs.size());
            for (Job job : jobs) {
                if (!processedJobIds.contains(job.getId())) {
                    log.info("Sending email notification for job: {}", job.getTitle());
                    emailService.sendJobNotification(job);
                    processedJobIds.add(job.getId());
                }
            }
        } catch (Exception e) {
            log.error("Error during job check.", e);
        }
        log.info("Job check finished.");
    }
}
