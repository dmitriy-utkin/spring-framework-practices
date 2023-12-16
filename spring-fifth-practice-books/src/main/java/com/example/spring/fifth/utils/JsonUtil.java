package com.example.spring.fifth.utils;

import com.example.spring.fifth.utils.model.JsonBookModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

@UtilityClass
public class JsonUtil {

    @SneakyThrows
    public List<JsonBookModel> prepareJsonBookList(File dataSetFile) {
        List<JsonBookModel> books;
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(dataSetFile)) {
            Type jsonBookType = new TypeToken<List<JsonBookModel>>() {}.getType();
            books = gson.fromJson(reader, jsonBookType);
        } catch (Exception e) {
            e.printStackTrace();
            books = null;
        }
        return books;
    }
}
