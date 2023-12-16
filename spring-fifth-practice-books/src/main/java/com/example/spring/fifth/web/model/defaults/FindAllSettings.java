package com.example.spring.fifth.web.model.defaults;

import com.example.spring.fifth.web.model.book.BookFilter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindAllSettings {

    @Builder.Default
    private Integer pageSize = 20;

    @Builder.Default
    private Integer pageNum = 0;

    @Builder.Default
    private BookFilter filter = new BookFilter();
}
