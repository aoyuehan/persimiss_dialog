package com.aoyuehan.permission.utils;

import android.content.Context;
import android.content.SharedPreferences;


import com.aoyuehan.permission.bean.CacheElement;

import java.util.List;

public class SpUtils {

    private static final String TAG =  "hdh";
    private static SpUtils instance;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public SpUtils(Context context) {
        sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static SpUtils getInstance(Context context) {
        if(instance == null) {
            instance = new SpUtils(context);
        }
        return instance;
    }

    /**
     * 设置缓存
     * @param key
     * @param value
     * @param expireTime
     */
    public void set(String key, Object value, int expireTime) {
        CacheElement element = new CacheElement(JsonUtils.toJson(value), expireTime);
        editor.putString(key, JsonUtils.toJson(element));
        editor.commit();
    }

    /**
     * 设置缓存
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        set(key, value, -1);
    }

    /**
     * 删除缓存
     * @param key
     */
    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清空缓存
     */
    public void clear() {
        editor.clear();
        editor.commit();
    }

    /**
     * 获取并删除缓存
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getAndRemove(String key, Class<T> clazz) {
        String value = sp.getString(key, "");
        if(value == null || value.length() == 0) {
            return null;
        }
        editor.remove(key);
        editor.commit();
        CacheElement element = JsonUtils.fromJson(value, CacheElement.class);
        if(element == null) {
            return null;
        } else if(element.hasExpired()) {
            return null;
        }
        return JsonUtils.fromJson(element.getValue(), clazz);
    }

    /**
     * 获取列表并删除缓存
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> getListAndRemove(String key, Class<T> clazz) {
        String value = sp.getString(key, "");
        if(value == null || value.length() == 0) {
            return null;
        }
        editor.remove(key);
        editor.commit();
        CacheElement element = JsonUtils.fromJson(value, CacheElement.class);
        if(element == null) {
            return null;
        } else if(element.hasExpired()) {
            return null;
        }
        return JsonUtils.fromJsonList(element.getValue(), clazz);
    }

    /**
     * 获取单个值
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(String key, Class<T> clazz) {
        String value = sp.getString(key, "");
        if(value == null || value.length() == 0) {
            return null;
        }
        CacheElement element = JsonUtils.fromJson(value, CacheElement.class);
        if(element == null) {
            return null;
        } else if(element.hasExpired()) {
            editor.remove(key);
            editor.commit();
            return null;
        }
        return JsonUtils.fromJson(element.getValue(), clazz);
    }

    /**
     * 获取列表
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> getList(String key, Class<T> clazz) {
        String value = sp.getString(key, "");
        if(value == null || value.length() == 0) {
            return null;
        }
        CacheElement element = JsonUtils.fromJson(value, CacheElement.class);
        if(element == null) {
            return null;
        } else if(element.hasExpired()) {
            editor.remove(key);
            editor.commit();
            return null;
        }
        return JsonUtils.fromJsonList(element.getValue(), clazz);
    }


}
