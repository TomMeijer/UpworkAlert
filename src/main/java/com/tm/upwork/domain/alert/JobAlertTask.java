package com.tm.upwork.domain.alert;

import com.google.common.collect.EvictingQueue;
import com.tm.upwork.domain.job.JobService;
import com.tm.upwork.domain.job.client.JobClient;
import com.tm.upwork.domain.job.client.UpworkJob;
import com.tm.upwork.domain.notification.NotificationSettingsRepository;
import com.tm.upwork.domain.notification.NotificationSettingsService;
import com.tm.upwork.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Queue;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobAlertTask {

    private final JobClient jobClient;
    private final JobService jobService;
    private final EmailService emailService;
    private final NotificationSettingsService notificationSettingsService;

    private final Queue<String> processedJobIds = EvictingQueue.create(100);

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
                    sendNotification(job);
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
        var settings = notificationSettingsService.get();
        if (!settings.isEmailEnabled()) {
            return;
        }
        log.info("Sending email notification.");
        try {
            emailService.sendJobNotification(job, settings.getRecipientEmail());
        } catch (Exception e) {
            log.error("Failed to send email.", e);
        }
    }
}
