package com.tm.upwork.domain.job.client;

import com.tm.upwork.domain.job.JobDto;

import java.util.List;

public interface JobClient {

    List<JobDto> fetchNewJobs();
}
