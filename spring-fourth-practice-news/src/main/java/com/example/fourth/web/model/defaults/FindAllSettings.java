package com.example.fourth.web.model.defaults;

import com.example.fourth.web.model.news.NewsFilter;
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
    private Integer pageSize = 10;

    @Builder.Default
    private Integer pageNum = 0;

    @Builder.Default
    private NewsFilter filter = new NewsFilter();


}
