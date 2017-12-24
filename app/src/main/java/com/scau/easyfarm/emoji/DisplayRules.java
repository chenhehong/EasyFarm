/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scau.easyfarm.emoji;

import com.scau.easyfarm.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Emoji在手机上的显示规则
 * 
 * @author kymjs (http://www.kymjs.com)
 */
public enum DisplayRules {
    // 注意：value不能从0开始，因为0会被库自动设置为删除按钮
    // int type, int value, int resId, String cls


    Nature114(2, 1, R.drawable.action_refresh, ":squirrel:", ":squirrel:");

    /********************************* 操作 **************************************/
    private String emojiStr;
    private String remote;
    private int value;
    private int resId;
    private int type;
    private static Map<String, Integer> sEmojiMap;

    private DisplayRules(int type, int value, int resId, String cls,
            String remote) {
        this.type = type;
        this.emojiStr = cls;
        this.value = value;
        this.resId = resId;
        this.remote = remote;
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public String getEmojiStr() {
        return emojiStr;
    }

    public int getValue() {
        return value;
    }

    public int getResId() {
        return resId;
    }

    public int getType() {
        return type;
    }

    private static Emojicon getEmojiFromEnum(DisplayRules data) {
        return new Emojicon(data.getResId(), data.getValue(),
                data.getEmojiStr(), data.getRemote());
    }

    public static Emojicon getEmojiFromRes(int resId) {
        for (DisplayRules data : values()) {
            if (data.getResId() == resId) {
                return getEmojiFromEnum(data);
            }
        }
        return null;
    }

    public static Emojicon getEmojiFromValue(int value) {
        for (DisplayRules data : values()) {
            if (data.getValue() == value) {
                return getEmojiFromEnum(data);
            }
        }
        return null;
    }

    public static Emojicon getEmojiFromName(String emojiStr) {
        for (DisplayRules data : values()) {
            if (data.getEmojiStr().equals(emojiStr)) {
                return getEmojiFromEnum(data);
            }
        }
        return null;
    }

    /**
     * 提高效率，忽略线程安全
     */
    public static Map<String, Integer> getMapAll() {
        if (sEmojiMap == null) {
            sEmojiMap = new HashMap<String, Integer>();
            for (DisplayRules data : values()) {
                sEmojiMap.put(data.getEmojiStr(), data.getResId());
                sEmojiMap.put(data.getRemote(), data.getResId());
            }
        }
        return sEmojiMap;
    }

    public static List<Emojicon> getAllByType(int type) {
        List<Emojicon> datas = new ArrayList<Emojicon>(values().length);
        for (DisplayRules data : values()) {
            if (data.getType() == type) {
                datas.add(getEmojiFromEnum(data));
            }
        }
        return datas;
    }
}
