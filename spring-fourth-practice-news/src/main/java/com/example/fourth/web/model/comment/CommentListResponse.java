package com.example.fourth.web.model.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentListResponse {

    @Builder.Default
    List<CommentResponse> comments = new ArrayList<>();
}
