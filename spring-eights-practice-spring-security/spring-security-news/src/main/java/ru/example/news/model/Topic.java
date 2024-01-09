package ru.example.news.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "news_topics")
@FieldNameConstants
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "topic_name")
    private String topic;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<News> news = new ArrayList<>();

    @CreationTimestamp
    private Instant createAt;
}
