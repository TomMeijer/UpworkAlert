package com.tm.upwork.domain.job.client.apify;

import com.tm.upwork.domain.job.JobDto;
import com.tm.upwork.domain.job.client.JobClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class ApifyJobClient implements JobClient {

    private final ApifyJobDownloader apifyJobDownloader;
    private final ApifyJobParser apifyJobParser;
    private final ApifyInputBuilder apifyInputBuilder;

    @Override
    public List<JobDto> fetchNewJobs() {
        ApifyInput input = apifyInputBuilder.build();
        List<ApifyJob> apifyJobs = apifyJobDownloader.runSyncGetDatasetItems(input);
        return apifyJobParser.parseJobs(apifyJobs);
    }
}
