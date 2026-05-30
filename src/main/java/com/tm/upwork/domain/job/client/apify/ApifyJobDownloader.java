package com.tm.upwork.domain.job.client.apify;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class ApifyJobDownloader {

    private final RestClient restClient;
    private final String apifyToken;

    public ApifyJobDownloader(RestClient.Builder restClientBuilder,
                              @Value("${apify.token}") String apifyToken) {
        this.restClient = restClientBuilder.baseUrl("https://api.apify.com/v2").build();
        this.apifyToken = apifyToken;
    }

    public List<ApifyJob> runSyncGetDatasetItems(ApifyInput input) {
        return restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/acts/neatrat~upwork-job-scraper/run-sync-get-dataset-items")
                        .queryParam("token", apifyToken)
                        .build())
                .body(input)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
