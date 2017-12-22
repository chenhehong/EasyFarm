package com.scau.easyfarm.util;

import com.alibaba.fastjson.JSON;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chenhehong on 2016/7/22.
 */
public class JsonUtils {


    public static <T> T toBean(Class<T> type, byte[] bytes) {
        if (bytes == null) return null;
        return toBean(type, new ByteArrayInputStream(bytes));
    }

    /**
     * 将一个json流转换为bean实体类
     *
     * @param type
     * @param is
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T toBean(Class<T> type, InputStream is) {
        T obj = null;
        try {
            obj = JSON.parseObject(is,type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }

}
