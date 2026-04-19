package com.tm.upwork.domain.job.apify;

import com.tm.upwork.domain.job.Job;
import com.tm.upwork.domain.job.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class ApifyJobService implements JobService {
    private final ApifyJobClient apifyJobClient;
    private final ApifyJobParser apifyJobParser;
    private final ApifyInputBuilder apifyInputBuilder;

    @Override
    public List<Job> fetchNewJobs() {
        ApifyInput input = apifyInputBuilder.build();
        List<ApifyJob> apifyJobs = apifyJobClient.runSyncGetDatasetItems(input);
        return apifyJobParser.parseJobs(apifyJobs);
    }
}
