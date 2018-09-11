package io.github.xiaoyureed.util;

import com.google.gson.Gson;

/**
 * @Auther: xiaoyu
 * @Date: 2018/9/11 19:48
 * @Description: json utils
 */
public class JsonUtils {

    private static final Gson gson = new Gson();

    private JsonUtils() {}

    public static String toJson(Object source) {
        return gson.toJson(source);
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }
}
