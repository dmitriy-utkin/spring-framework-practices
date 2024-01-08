package com.example.fourth.web.model.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpsertTopicRequest {

    @NotBlank(message = "Topic should be field")
    @Size(min = 2, max = 40, message = "Topic length should be between 2 and 40 characters")
    private String topic;
}
