package com.tm.upwork.domain.job.apify;

import com.tm.upwork.domain.job.Job;
import com.tm.upwork.domain.job.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
@Primary
@RequiredArgsConstructor
public class ApifyJobService implements JobService {
    private static final int JOB_AGE_MARGIN_MINUTES = 2;
    private static final int DEFAULT_JOB_AGE_HOURS = 1;

    private final ApifyJobClient apifyJobClient;
    private final ApifyJobParser apifyJobParser;
    private final ApifyInputBuilder apifyInputBuilder;

    private Instant lastRetrievalTime;

    @Override
    public List<Job> fetchNewJobs() {
        Instant now = Instant.now();
        Map<String, Object> maxJobAge;
        if (lastRetrievalTime != null) {
            long minutes = Duration.between(lastRetrievalTime, now).toMinutes() + 1;
            maxJobAge = Map.of("value", minutes + JOB_AGE_MARGIN_MINUTES, "unit", "minutes");
        } else {
            maxJobAge = Map.of("value", DEFAULT_JOB_AGE_HOURS, "unit", "hours");
        }
        ApifyInput input = apifyInputBuilder.build(maxJobAge);
        List<ApifyJob> apifyJobs = apifyJobClient.runSyncGetDatasetItems(input);
        List<Job> jobs = apifyJobParser.parseJobs(apifyJobs);
        lastRetrievalTime = now;
        return jobs;
    }
}
