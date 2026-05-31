package com.tm.upwork.domain.alert;

import com.google.common.collect.EvictingQueue;
import com.tm.upwork.domain.job.JobService;
import com.tm.upwork.domain.job.client.JobClient;
import com.tm.upwork.domain.job.client.UpworkJob;
import com.tm.upwork.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Queue;

@Slf4j
@Component
public class JobAlertTask {

    private final JobClient jobClient;
    private final JobService jobService;
    private final EmailService emailService;
    private final boolean emailEnabled;

    private final Queue<String> processedJobIds = EvictingQueue.create(100);

    @Autowired
    public JobAlertTask(JobClient jobClient,
                        JobService jobService,
                        EmailService emailService,
                        @Value("${notifications.email.enabled}") boolean emailEnabled) {
        this.jobClient = jobClient;
        this.jobService = jobService;
        this.emailService = emailService;
        this.emailEnabled = emailEnabled;
    }

    @Scheduled(fixedRate = 900000) // every 15 minutes
    public void checkForNewJobs() {
        log.info("Checking for new Upwork jobs.");
        try {
            log.info("Fetching jobs from Upwork...");
            List<UpworkJob> jobs = jobClient.fetchNewJobs();
            log.info("{} jobs fetched.", jobs.size());
            for (UpworkJob job : jobs) {
                if (!processedJobIds.contains(job.getId())) {
                    saveJob(job);
                    if (emailEnabled) {
                        sendNotification(job);
                    }
                    processedJobIds.add(job.getId());
                }
            }
        } catch (Exception e) {
            log.error("Error during job check.", e);
        }
        log.info("Job check finished.");
    }

    private void saveJob(UpworkJob job) {
        log.info("Saving new job: {}", job.getTitle());
        try {
            jobService.save(job);
        } catch (Exception e) {
            log.error("Failed to save job.", e);
        }
    }

    private void sendNotification(UpworkJob job) {
        log.info("Sending email notification.");
        try {
            emailService.sendJobNotification(job);
        } catch (Exception e) {
            log.error("Failed to send email.", e);
        }
    }
}
