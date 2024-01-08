package com.example.fourth.utils.model;

import lombok.Data;

import java.util.List;

@Data
public class PreparedJsonData {
    private List<JsonNews> jsonNews;
    private List<JsonUser> jsonUsers;
    private List<JsonComments> jsonComments;
}
