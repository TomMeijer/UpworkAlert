package com.tm.upwork.domain.job.upwork;

import com.tm.upwork.domain.job.Job;
import com.tm.upwork.domain.job.JobType;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UpworkJobParser {

    public List<Job> parseJobs(JSONObject response) throws JSONException {
        List<Job> jobs = new ArrayList<>();
        if (response == null || !response.has("data")) {
            return jobs;
        }

        JSONObject data = response.getJSONObject("data");
        if (data.isNull("marketplaceJobPostingsSearch")) return jobs;
        JSONObject search = data.getJSONObject("marketplaceJobPostingsSearch");

        if (search.isNull("edges")) return jobs;
        JSONArray edges = search.getJSONArray("edges");

        for (int i = 0; i < edges.length(); i++) {
            JSONObject edge = edges.getJSONObject(i);
            JSONObject node = edge.getJSONObject("node");
            Job job = new Job();
            job.setId(node.optString("id"));
            job.setTitle(node.optString("title"));
            job.setDescription(node.optString("description"));
            String ciphertext = node.optString("ciphertext");
            if (ciphertext != null && !ciphertext.isEmpty()) {
                job.setUrl("https://www.upwork.com/jobs/" + ciphertext);
            }
            job.setPublishedOn(node.optString("publishedDateTime"));

            // Price handling
            if (!node.isNull("amount")) {
                job.setType(JobType.FIXED);
                JSONObject amount = node.getJSONObject("amount");
                if (!amount.isNull("rawValue")) {
                    job.setFixedPrice(amount.optDouble("rawValue"));
                }
            }

            if (!node.isNull("hourlyBudgetMin")) {
                job.setType(JobType.HOURLY);
                JSONObject hourlyBudgetMin = node.getJSONObject("hourlyBudgetMin");
                if (!hourlyBudgetMin.isNull("rawValue")) {
                    job.setHourlyRateMin(hourlyBudgetMin.optDouble("rawValue"));
                }
            }

            if (!node.isNull("hourlyBudgetMax")) {
                JSONObject hourlyBudgetMax = node.getJSONObject("hourlyBudgetMax");
                if (!hourlyBudgetMax.isNull("rawValue")) {
                    job.setHourlyRateMax(hourlyBudgetMax.optDouble("rawValue"));
                }
            }

            // Client Country
            if (!node.isNull("client")) {
                JSONObject client = node.getJSONObject("client");
                if (!client.isNull("location")) {
                    JSONObject location = client.getJSONObject("location");
                    job.setClientCountry(location.optString("country"));
                }
            }

            // Skills
            if (!node.isNull("skills")) {
                JSONArray skillsArray = node.getJSONArray("skills");
                List<String> skills = new ArrayList<>();
                for (int j = 0; j < skillsArray.length(); j++) {
                    skills.add(skillsArray.getJSONObject(j).optString("name"));
                }
                job.setRequiredSkills(skills);
            }

            jobs.add(job);
        }
        return jobs;
    }
}
