package com.example.spring.fifth.web.model.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookFilter implements Serializable {

    private String bookName;

    private String categoryName;

    private Long categoryId;

    private String author;
}
