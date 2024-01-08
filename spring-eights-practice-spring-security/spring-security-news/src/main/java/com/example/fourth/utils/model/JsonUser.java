package com.example.fourth.utils.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JsonUser {

    private String name;
    private String email;
}
