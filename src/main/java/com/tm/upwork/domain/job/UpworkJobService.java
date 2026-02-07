package com.tm.upwork.domain.job;

import com.Upwork.api.OAuthClient;
import com.Upwork.api.Routers.Graphql;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UpworkJobService {

    private final OAuthClient oauthClient;
    private final UpworkJobParser jobParser;
    private final UpworkJobQueryBuilder queryBuilder;

    public List<Job> fetchNewJobs() throws JSONException {
        String query = queryBuilder.buildQuery();
        var params = new HashMap<String, String>();
        params.put("query", query);
        var graphql = new Graphql(oauthClient);
        JSONObject response = graphql.Execute(params);
        return jobParser.parseJobs(response);
    }
}
