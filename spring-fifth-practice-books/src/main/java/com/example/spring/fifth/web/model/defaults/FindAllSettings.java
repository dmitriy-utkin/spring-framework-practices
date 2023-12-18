package com.example.spring.fifth.web.model.defaults;

import com.example.spring.fifth.web.model.book.BookFilter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindAllSettings implements Serializable {

    @Builder.Default
    private Integer pageSize = 20;

    @Builder.Default
    private Integer pageNum = 0;

    @Builder.Default
    private BookFilter filter = new BookFilter();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FindAllSettings that)) return false;
        return Objects.equals(pageSize, that.pageSize)
                && Objects.equals(pageNum, that.pageNum)
                && Objects.equals(filter, that.filter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageSize, pageNum, filter);
    }
}
