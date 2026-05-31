package com.tm.upwork.domain.job.client.upwork;

import com.Upwork.api.OAuthClient;
import com.Upwork.api.Routers.Graphql;
import com.tm.upwork.domain.job.client.JobClient;
import com.tm.upwork.domain.job.client.UpworkJob;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UpworkJobClient implements JobClient {

    private final OAuthClient oauthClient;
    private final UpworkJobParser jobParser;
    private final UpworkJobQueryBuilder queryBuilder;

    public List<UpworkJob> fetchNewJobs() {
        try {
            String query = queryBuilder.buildQuery();
            var params = new HashMap<String, String>();
            params.put("query", query);
            var graphql = new Graphql(oauthClient);
            JSONObject response = graphql.Execute(params);
            return jobParser.parseJobs(response);
        } catch (JSONException e) {
            throw new IllegalStateException("Failed to fetch jobs.", e);
        }
    }
}
