package com.tm.upwork.domain.job.client;

import java.util.List;

public interface JobClient {

    List<UpworkJob> fetchNewJobs();
}
