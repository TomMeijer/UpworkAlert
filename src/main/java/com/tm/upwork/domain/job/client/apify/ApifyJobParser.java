package com.tm.upwork.domain.job.client.apify;

import com.tm.upwork.domain.job.client.UpworkJob;
import com.tm.upwork.domain.job.entity.JobStatus;
import com.tm.upwork.domain.job.entity.JobType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ApifyJobParser {

    public List<UpworkJob> parseJobs(List<ApifyJob> apifyJobs) {
        return apifyJobs.stream()
                .map(this::mapToJob)
                .toList();
    }

    private UpworkJob mapToJob(ApifyJob apifyJob) {
        var builder = UpworkJob.builder()
                .id(Objects.requireNonNull(apifyJob.getId(), "id must not be null."))
                .title(Objects.requireNonNull(apifyJob.getTitle(), "title must not be null."))
                .description(apifyJob.getDescription())
                .status(JobStatus.NEW)
                .url(getUrl(apifyJob))
                .publishedOn(LocalDateTime.parse(
                        Objects.requireNonNull(apifyJob.getAbsoluteDate(), "publishedOn must not be null."),
                        DateTimeFormatter.ISO_DATE_TIME))
                .clientCountry(apifyJob.getClientLocation())
                .requiredSkills(apifyJob.getTags())
                .experienceLevel(apifyJob.getExperienceLevel())
                .clientRating(apifyJob.getClientRating())
                .clientTotalSpent(apifyJob.getClientTotalSpent());

        if (apifyJob.getPaymentVerified() instanceof Boolean paymentVerifiedBool) {
            builder.paymentVerified(paymentVerifiedBool);
        } else if (apifyJob.getPaymentVerified() instanceof String paymentVerifiedString) {
            builder.paymentVerified("VERIFIED".equalsIgnoreCase(paymentVerifiedString));
        }

        if ("FIXED".equalsIgnoreCase(apifyJob.getJobType())) {
            builder.type(JobType.FIXED)
                    .fixedPrice(parseAmount(apifyJob.getBudget()));
        } else if ("HOURLY".equalsIgnoreCase(apifyJob.getJobType())) {
            builder.type(JobType.HOURLY);
            if (apifyJob.getBudget() != null && !apifyJob.getBudget().isBlank()) {
                String[] range = apifyJob.getBudget().split("-");
                builder.hourlyRateMin(parseAmount(range[0]));
                if (range.length > 1) {
                    builder.hourlyRateMax(parseAmount(range[1]));
                }
            }
        }

        return builder.build();
    }

    private String getUrl(ApifyJob apifyJob) {
        String url = Objects.requireNonNull(apifyJob.getUrl(), "url must not be null.");
        Pattern pattern = Pattern.compile("~\\d+");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return "https://www.upwork.com/nx/proposals/job/" + matcher.group() + "/apply/";
        }
        return url;
    }

    private Double parseAmount(String input) {
        if (input == null || input.isBlank()) {
            return null;
        }
        String sanitized = input.replace(",", "");
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(sanitized);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group());
        }
        return null;
    }
}
