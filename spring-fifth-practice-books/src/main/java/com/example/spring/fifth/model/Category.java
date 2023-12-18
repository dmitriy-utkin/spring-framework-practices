package com.example.spring.fifth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "categories")
@FieldNameConstants
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String name;

    @Builder.Default
    @OneToMany(mappedBy = Book.Fields.category, cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<Book> books = new ArrayList<>();
}
