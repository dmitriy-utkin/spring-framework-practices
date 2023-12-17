package com.example.spring.fifth.web.model.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Builder
public class UpsertBookRequest {
    private String author;
    private String name;
    private String category;
}
