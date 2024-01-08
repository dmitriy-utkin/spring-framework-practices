package com.example.fourth.utils;

import com.example.fourth.configuration.GenericDataConfig;
import com.example.fourth.utils.model.JsonComments;
import com.example.fourth.utils.model.JsonNews;
import com.example.fourth.utils.model.JsonUser;
import com.example.fourth.utils.model.PreparedJsonData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

public class JsonUtil {

    public static PreparedJsonData createDataFromResource(GenericDataConfig data) {
        Gson gson = new Gson();
        PreparedJsonData preparedJsonData = new PreparedJsonData();
        preparedJsonData.setJsonNews(createJsonNewsList(new File(data.getNewsDataPath()), gson));
        preparedJsonData.setJsonUsers(createJsonUserList(new File(data.getUsersDataPath()), gson));
        preparedJsonData.setJsonComments(createJsonCommentsList(new File(data.getCommentsDataPath()), gson));
        return preparedJsonData;
    }



    private static List<JsonNews> createJsonNewsList(File file, Gson gson) {
        List<JsonNews> list;
        try (FileReader reader = new FileReader(file)) {
            Type jNews = new TypeToken<List<JsonNews>>() {}.getType();
            list = gson.fromJson(reader, jNews);
        } catch (Exception e) {
            e.printStackTrace();
            list = null;
        }
        return list;
    }

    private static List<JsonUser> createJsonUserList(File file, Gson gson) {
        List<JsonUser> list;
        try (FileReader reader = new FileReader(file)) {
            Type jUser = new TypeToken<List<JsonUser>>() {}.getType();
            list = gson.fromJson(reader, jUser);
        } catch (Exception e) {
            e.printStackTrace();
            list = null;
        }
        return list;
    }

    private static List<JsonComments> createJsonCommentsList(File file, Gson gson) {
        List<JsonComments> list;
        try (FileReader reader = new FileReader(file)) {
            Type jComment = new TypeToken<List<JsonComments>>() {}.getType();
            list = gson.fromJson(reader, jComment);
        } catch (Exception e) {
            e.printStackTrace();
            list = null;
        }
        return list;
    }


}
