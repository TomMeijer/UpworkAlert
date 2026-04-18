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
    private final ApifyJobClient apifyJobClient;
    private final ApifyJobParser apifyJobParser;
    private final ApifyInputBuilder apifyInputBuilder;

    private Instant lastRetrievalTime;

    @Override
    public List<Job> fetchNewJobs() {
        Instant now = Instant.now();
        Map<String, Object> maxJobAge;
        if (lastRetrievalTime != null) {
            long minutes = Duration.between(lastRetrievalTime, now).toMinutes();
            maxJobAge = Map.of("value", minutes + 1, "unit", "minutes");
        } else {
            maxJobAge = Map.of("value", 1, "unit", "hours");
        }
        lastRetrievalTime = now;
        ApifyInput input = apifyInputBuilder.build(maxJobAge);
        List<ApifyJob> apifyJobs = apifyJobClient.runSyncGetDatasetItems(input);
        return apifyJobParser.parseJobs(apifyJobs);
    }
}
