package com.tm.upwork.domain.job.apify;

import com.tm.upwork.domain.job.Job;
import com.tm.upwork.domain.job.JobType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApifyJobParserTest {

    private ApifyJobParser parser;

    @BeforeEach
    void setUp() {
        parser = new ApifyJobParser();
    }

    @Test
    void testMapToJob_FixedPrice() {
        ApifyJob apifyJob = new ApifyJob();
        apifyJob.setId("123");
        apifyJob.setTitle("Fixed Job");
        apifyJob.setDescription("Description");
        apifyJob.setUrl("http://example.com");
        apifyJob.setAbsoluteDate("2023-10-27T10:00:00Z");
        apifyJob.setClientLocation("United States");
        apifyJob.setTags(List.of("Java", "Spring"));
        apifyJob.setJobType("Fixed");
        apifyJob.setBudget("$500");

        Job job = parser.mapToJob(apifyJob);

        assertEquals("123", job.getId());
        assertEquals("Fixed Job", job.getTitle());
        assertEquals("Description", job.getDescription());
        assertEquals("http://example.com", job.getUrl());
        assertEquals("2023-10-27T10:00:00Z", job.getPublishedOn());
        assertEquals("United States", job.getClientCountry());
        assertEquals(List.of("Java", "Spring"), job.getRequiredSkills());
        assertEquals(JobType.FIXED, job.getType());
        assertEquals(500.0, job.getFixedPrice());
        assertNull(job.getHourlyRateMin());
    }

    @Test
    void testMapToJob_HourlyRate() {
        ApifyJob apifyJob = new ApifyJob();
        apifyJob.setId("456");
        apifyJob.setJobType("Hourly");
        apifyJob.setBudget("$25 - $50");

        Job job = parser.mapToJob(apifyJob);

        assertEquals("456", job.getId());
        assertEquals(JobType.HOURLY, job.getType());
        assertEquals(25.0, job.getHourlyRateMin());
        assertEquals(50.0, job.getHourlyRateMax());
        assertNull(job.getFixedPrice());
    }

    @Test
    void testMapToJob_HourlyRateSingleValue() {
        ApifyJob apifyJob = new ApifyJob();
        apifyJob.setJobType("Hourly");
        apifyJob.setBudget("$30");

        Job job = parser.mapToJob(apifyJob);

        assertEquals(JobType.HOURLY, job.getType());
        assertEquals(30.0, job.getHourlyRateMin());
        assertNull(job.getHourlyRateMax());
    }

    @Test
    void testParseJobs() {
        ApifyJob job1 = new ApifyJob();
        job1.setId("1");
        ApifyJob job2 = new ApifyJob();
        job2.setId("2");

        List<Job> jobs = parser.parseJobs(List.of(job1, job2));

        assertEquals(2, jobs.size());
        assertEquals("1", jobs.get(0).getId());
        assertEquals("2", jobs.get(1).getId());
    }
}
