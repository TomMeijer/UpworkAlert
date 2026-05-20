package com.tm.upwork.domain.job.client.apify;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class ApifyJobDownloader {

    private final WebClient webClient;
    private final String apifyToken;

    public ApifyJobDownloader(WebClient.Builder webClientBuilder,
                              @Value("${apify.token}") String apifyToken) {
        this.webClient = webClientBuilder.baseUrl("https://api.apify.com/v2").build();
        this.apifyToken = apifyToken;
    }

    public List<ApifyJob> runSyncGetDatasetItems(ApifyInput input) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/acts/neatrat~upwork-job-scraper/run-sync-get-dataset-items")
                        .queryParam("token", apifyToken)
                        .build())
                .bodyValue(input)
                .retrieve()
                .bodyToFlux(ApifyJob.class)
                .collectList()
                .block();
    }
}
