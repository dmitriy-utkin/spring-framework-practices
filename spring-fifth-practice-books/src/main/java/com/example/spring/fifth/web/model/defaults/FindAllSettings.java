package com.example.spring.fifth.web.model.defaults;

import com.example.spring.fifth.web.model.book.BookFilter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldNameConstants
public class FindAllSettings {

    @Builder.Default
    private int pageSize = 15;

    @Builder.Default
    private int pageNum = 0;

    @Builder.Default
    private BookFilter filter = new BookFilter();
}
