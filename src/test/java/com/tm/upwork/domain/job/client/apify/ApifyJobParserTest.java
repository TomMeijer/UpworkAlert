package com.tm.upwork.domain.job.client.apify;

import com.tm.upwork.domain.job.JobDto;
import com.tm.upwork.domain.job.JobType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        apifyJob.setTitle("Fixed JobDto");
        apifyJob.setDescription("Description");
        apifyJob.setUrl("http://example.com");
        apifyJob.setAbsoluteDate("2023-10-27T10:00:00Z");
        apifyJob.setClientLocation("United States");
        apifyJob.setTags(List.of("Java", "Spring"));
        apifyJob.setJobType("Fixed");
        apifyJob.setBudget("$500");
        apifyJob.setExperienceLevel("Expert");
        apifyJob.setPaymentVerified(true);
        apifyJob.setClientRating(4.5);
        apifyJob.setClientTotalSpent(1000.0);

        JobDto job = parser.mapToJob(apifyJob);

        assertEquals("123", job.getId());
        assertEquals("Fixed JobDto", job.getTitle());
        assertEquals("Description", job.getDescription());
        assertEquals("http://example.com", job.getUrl());
        assertEquals(LocalDateTime.parse("2023-10-27T10:00:00Z", DateTimeFormatter.ISO_DATE_TIME), job.getPublishedOn());
        assertEquals("United States", job.getClientCountry());
        assertEquals(List.of("Java", "Spring"), job.getRequiredSkills());
        assertEquals(JobType.FIXED, job.getType());
        assertEquals(500.0, job.getFixedPrice());
        assertEquals("Expert", job.getExperienceLevel());
        assertTrue(job.isPaymentVerified());
        assertEquals(4.5, job.getClientRating());
        assertEquals(1000.0, job.getClientTotalSpent());
        assertNull(job.getHourlyRateMin());
    }

    @Test
    void testMapToJob_PaymentVerifiedString() {
        ApifyJob apifyJob = new ApifyJob();
        apifyJob.setPaymentVerified("VERIFIED");

        JobDto job = parser.mapToJob(apifyJob);

        assertTrue(job.isPaymentVerified());

        apifyJob.setPaymentVerified("NOT_VERIFIED");
        job = parser.mapToJob(apifyJob);
        assertFalse(job.isPaymentVerified());
    }

    @Test
    void testMapToJob_HourlyRate() {
        ApifyJob apifyJob = new ApifyJob();
        apifyJob.setId("456");
        apifyJob.setJobType("Hourly");
        apifyJob.setBudget("$25 - $50");

        JobDto job = parser.mapToJob(apifyJob);

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

        JobDto job = parser.mapToJob(apifyJob);

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

        List<JobDto> jobs = parser.parseJobs(List.of(job1, job2));

        assertEquals(2, jobs.size());
        assertEquals("1", jobs.get(0).getId());
        assertEquals("2", jobs.get(1).getId());
    }
}
