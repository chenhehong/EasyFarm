package com.scau.easyfarm.util;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * Created by chenhehong on 2016/7/22.
 */
public class JsonUtils {

    /**Json转JavaBean**/
    public static final int JSON_JAVABEAN=0x10001;
    /**Json转List<T>**/
    public static final int JSON_LIST=0x10002;
    /**Json转Map<T>**/
    public static final int JSON_MAP=0x10004;

    public static <T> T toBean(Class<T> type, byte[] bytes) {
        if (bytes == null) return null;
        return toBean(type, new ByteArrayInputStream(bytes));
    }

    /**
     * 将一个xml流转换为bean实体类
     *
     * @param type
     * @param is
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T toBean(Class<T> type, InputStream is) {
        Gson gson = new Gson();
        T obj = null;
        BufferedReader reader=intputStream2BufferedReader(is);
        obj = (T)gson.fromJson(reader,type);
        return obj;
    }

    /**
     * 将InputStream封装成BufferedReader
     * @param inputStream
     * @return
     */
    private static BufferedReader intputStream2BufferedReader(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream));
    }

}
