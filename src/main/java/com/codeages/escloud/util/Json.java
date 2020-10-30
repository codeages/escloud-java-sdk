package com.codeages.escloud.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public final class Json {
    public static <T> List<T> jsonToObjectList(String json, Class<T> classOfT) {
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(json).getAsJsonArray();

        List<T> list = new ArrayList<T>();

        for (JsonElement jsonElement : jsonArray) {
            T bean = new Gson().fromJson(jsonElement, classOfT);
            list.add(bean);
        }

        return list;
    }

    public static <T> T jsonToObject(String json, Class<T> classOfT) {
        return new Gson().fromJson(json, classOfT);
    }
}
