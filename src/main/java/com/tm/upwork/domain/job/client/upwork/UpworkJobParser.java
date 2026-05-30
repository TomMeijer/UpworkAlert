package com.tm.upwork.domain.job.client.upwork;

import com.tm.upwork.domain.job.JobDto;
import com.tm.upwork.domain.job.entity.JobStatus;
import com.tm.upwork.domain.job.entity.JobType;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UpworkJobParser {

    public List<JobDto> parseJobs(JSONObject response) {
        try {
            List<JobDto> jobs = new ArrayList<>();
            JSONArray edges = response.getJSONObject("data")
                    .getJSONObject("marketplaceJobPostingsSearch")
                    .getJSONArray("edges");
            for (int i = 0; i < edges.length(); i++) {
                JSONObject node = edges.getJSONObject(i)
                        .getJSONObject("node");
                jobs.add(mapToJob(node));
            }
            return jobs;
        } catch (JSONException e) {
            throw new IllegalStateException("Failed to parse jobs.", e);
        }
    }

    private JobDto mapToJob(JSONObject node) throws JSONException {
        var builder = JobDto.builder()
                .upworkId(node.getString("id"))
                .title(node.getString("title"))
                .description(node.optString("description"))
                .status(JobStatus.NEW)
                .url("https://www.upwork.com/nx/proposals/job/" + node.getString("ciphertext") + "/apply/")
                .publishedOn(LocalDateTime.parse(node.getString("publishedDateTime"), DateTimeFormatter.ISO_DATE_TIME))
                .experienceLevel(node.optString("experienceLevel"));

        if (!node.isNull("amount")) {
            builder.type(JobType.FIXED)
                    .fixedPrice(node.getJSONObject("amount").getDouble("rawValue"));
        } else if (!node.isNull("hourlyBudgetMin")) {
            builder.type(JobType.HOURLY)
                    .hourlyRateMin(node.getJSONObject("hourlyBudgetMin").getDouble("rawValue"));
            if (!node.isNull("hourlyBudgetMax")) {
                builder.hourlyRateMax(node.getJSONObject("hourlyBudgetMax").getDouble("rawValue"));
            }
        }

        if (!node.isNull("client")) {
            JSONObject client = node.getJSONObject("client");
            if (!client.isNull("location")) {
                JSONObject location = client.getJSONObject("location");
                builder.clientCountry(location.optString("country"));
            }
            builder.paymentVerified("VERIFIED".equalsIgnoreCase(client.optString("verificationStatus")))
                    .clientRating(client.optDouble("totalFeedback"));
            if (!client.isNull("totalSpent")) {
                builder.clientTotalSpent(client.getJSONObject("totalSpent").getDouble("rawValue"));
            }
        }

        if (!node.isNull("skills")) {
            JSONArray skillsArray = node.getJSONArray("skills");
            List<String> skills = new ArrayList<>();
            for (int j = 0; j < skillsArray.length(); j++) {
                skills.add(skillsArray.getJSONObject(j).getString("name"));
            }
            builder.requiredSkills(skills);
        }

        return builder.build();
    }
}
