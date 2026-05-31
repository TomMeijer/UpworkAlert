package com.tm.upwork.domain.job.entity;

import com.tm.upwork.domain.chat.entity.Chat;
import com.tm.upwork.util.StringListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String upworkId;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private JobType type;

    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.NEW;

    private Double hourlyRateMin;
    private Double hourlyRateMax;
    private Double fixedPrice;
    private String clientCountry;

    @Convert(converter = StringListConverter.class)
    private List<String> requiredSkills;

    private String url;
    private LocalDateTime publishedOn;
    private String experienceLevel;
    private Boolean paymentVerified;
    private Double clientRating;
    private Double clientTotalSpent;

    @ManyToOne(cascade = CascadeType.ALL)
    private Chat chat;
}
