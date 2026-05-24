package com.tm.upwork.domain.job;

import com.tm.upwork.util.StringListConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "job")
@Data
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String upworkId;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private JobType type;

    private Double hourlyRateMin;
    private Double hourlyRateMax;
    private Double fixedPrice;
    private String clientCountry;

    @Convert(converter = StringListConverter.class)
    private List<String> requiredSkills;

    private String url;
    private LocalDateTime publishedOn;
    private String experienceLevel;
    private boolean paymentVerified;
    private Double clientRating;
    private Double clientTotalSpent;
}
