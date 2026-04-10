package com.tm.upwork.domain.job.upwork;

import com.tm.upwork.domain.job.Job;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UpworkJobParserTest {

    private UpworkJobParser parser;

    @BeforeEach
    void setUp() {
        parser = new UpworkJobParser();
    }

    @Test
    void testParseJobs_NullResponse() throws JSONException {
        List<Job> jobs = parser.parseJobs(null);
        assertNotNull(jobs);
        assertTrue(jobs.isEmpty());
    }

    @Test
    void testParseJobs_EmptyData() throws JSONException {
        JSONObject response = new JSONObject();
        List<Job> jobs = parser.parseJobs(response);
        assertNotNull(jobs);
        assertTrue(jobs.isEmpty());
    }

    @Test
    void testParseJobs_NoMarketplaceJobPostingsSearch() throws JSONException {
        JSONObject response = new JSONObject();
        response.put("data", new JSONObject());
        List<Job> jobs = parser.parseJobs(response);
        assertNotNull(jobs);
        assertTrue(jobs.isEmpty());
    }

    @Test
    void testParseJobs_ValidResponse() throws JSONException {
        JSONObject response = new JSONObject();
        JSONObject data = new JSONObject();
        JSONObject search = new JSONObject();
        JSONArray edges = new JSONArray();

        JSONObject node = new JSONObject();
        node.put("id", "123");
        node.put("title", "Test Job");
        node.put("description", "Test Description");
        node.put("ciphertext", "~0123456789abcdef");
        node.put("publishedDateTime", "2023-10-27T10:00:00Z");

        JSONObject amount = new JSONObject();
        amount.put("rawValue", 500.0);
        node.put("amount", amount);

        JSONObject client = new JSONObject();
        JSONObject location = new JSONObject();
        location.put("country", "United States");
        client.put("location", location);
        node.put("client", client);

        JSONArray skills = new JSONArray();
        JSONObject skill1 = new JSONObject();
        skill1.put("name", "Java");
        skills.put(skill1);
        node.put("skills", skills);

        JSONObject edge = new JSONObject();
        edge.put("node", node);
        edges.put(edge);

        search.put("edges", edges);
        data.put("marketplaceJobPostingsSearch", search);
        response.put("data", data);

        List<Job> jobs = parser.parseJobs(response);

        assertEquals(1, jobs.size());
        Job job = jobs.get(0);
        assertEquals("123", job.getId());
        assertEquals("Test Job", job.getTitle());
        assertEquals("Test Description", job.getDescription());
        assertEquals("https://www.upwork.com/jobs/~0123456789abcdef", job.getUrl());
        assertEquals("2023-10-27T10:00:00Z", job.getPublishedOn());
        assertEquals(500.0, job.getFixedPrice());
        assertEquals("United States", job.getClientCountry());
        assertEquals(1, job.getRequiredSkills().size());
        assertEquals("Java", job.getRequiredSkills().get(0));
    }

    @Test
    void testParseJobs_HourlyBudget() throws JSONException {
        JSONObject response = new JSONObject();
        JSONObject data = new JSONObject();
        JSONObject search = new JSONObject();
        JSONArray edges = new JSONArray();

        JSONObject node = new JSONObject();
        node.put("id", "456");

        JSONObject hourlyBudget = new JSONObject();
        hourlyBudget.put("rawValue", 25.0);
        node.put("hourlyBudgetMin", hourlyBudget);

        JSONObject edge = new JSONObject();
        edge.put("node", node);
        edges.put(edge);

        search.put("edges", edges);
        data.put("marketplaceJobPostingsSearch", search);
        response.put("data", data);

        List<Job> jobs = parser.parseJobs(response);

        assertEquals(1, jobs.size());
        Job job = jobs.get(0);
        assertEquals(25.0, job.getHourlyRate());
        assertNull(job.getFixedPrice());
    }

    @Test
    void testParseJobs_MissingOptionalFields() throws JSONException {
        JSONObject response = new JSONObject();
        JSONObject data = new JSONObject();
        JSONObject search = new JSONObject();
        JSONArray edges = new JSONArray();

        JSONObject node = new JSONObject();
        node.put("id", "789");
        // No title, description, ciphertext, amount, hourlyBudget, client, skills

        JSONObject edge = new JSONObject();
        edge.put("node", node);
        edges.put(edge);

        search.put("edges", edges);
        data.put("marketplaceJobPostingsSearch", search);
        response.put("data", data);

        List<Job> jobs = parser.parseJobs(response);

        assertEquals(1, jobs.size());
        Job job = jobs.get(0);
        assertEquals("789", job.getId());
        assertEquals("", job.getTitle()); // optString returns "" by default
        assertNull(job.getUrl());
        assertNull(job.getFixedPrice());
        assertNull(job.getHourlyRate());
        assertNull(job.getClientCountry());
        assertNull(job.getRequiredSkills());
    }
}
