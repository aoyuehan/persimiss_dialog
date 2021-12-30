package com.aoyuehan.permission.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;
import java.util.List;

/**
 * JSON工具类
 *
 * @author fqh
 * @create 2016-12-12 14:53
 */
public class JsonUtils {

    public static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static boolean PRETTY_FORMAT = false;

    private static List<SerializeFilter> filters = new ArrayList<>();
    private static SerializeConfig config = SerializeConfig.globalInstance;
    private static List<SerializerFeature> toJsonFeatures = new ArrayList<>();
    private static List<Feature> fromJsonFeatures = new ArrayList<>();





    /**
     * 把对象变成json字符串。
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return toJson(obj, DEFAULT_DATE_FORMAT);
    }

    public static String toJson(Object obj, SerializeFilter[] filters, String dateFormat, SerializerFeature ... features) {
        if(obj == null) {
            return null;
        }
        if(obj instanceof String) {
            return (String) obj;
        }

        filters = ArrayUtils.getArray(JsonUtils.filters, filters);
        if(filters == null) {
            filters = new SerializeFilter[0];
        }

        features = ArrayUtils.getArray(JsonUtils.toJsonFeatures, features);
        if(features == null) {
            features = new SerializerFeature[0];
        }

        if(PRETTY_FORMAT) {
            features = ArrayUtils.getArray(features, SerializerFeature.PrettyFormat);
        }
        if(config == null) {
            config = SerializeConfig.globalInstance;
        }
        return JSON.toJSONString(obj, config, filters, dateFormat, JSON.DEFAULT_GENERATE_FEATURE, features);
    }



    /**
     * 把对象变成json字符串
     * @param obj
     * @param dateFormat 字符串，默认为：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String toJson(Object obj, String dateFormat) {
        if(obj == null) {
            return null;
        }
        if(obj instanceof String) {
            return (String) obj;
        }

        return toJson(obj, null, dateFormat);
    }

    /**
     * 把json字符串转化为对象列表
     * @param value
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> fromJsonList(String value, Class<T> clazz) {
        return JSON.parseArray(value, clazz);
    }

    /**
     * 把json字符串转化为对象列表
     * <p>
     *     可指定json路径，例如：json字符串为：{total: 2, rows: [{}, {}]}，则获取rows的路径为$.rows
     * </p>
     * @param value
     * @param path
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> fromJsonList(String value, String path, Class<T> clazz) {
        if(path == null) {
            return fromJsonList(value, clazz);
        }
        Object pathValue = JSONPath.read(value, path);
        if(pathValue == null) {
            return null;
        }
        return fromJsonList(pathValue.toString(), clazz);
    }

    /**
     * 把json字符串转化为对象
     * @param value
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String value, Class<T> clazz, Feature... features) {
        if(String.class.isAssignableFrom(clazz)) {
            return (T) value;
        }
        features = ArrayUtils.getArray(fromJsonFeatures, features);
        if(features == null) {
            features = new Feature[0];
        }
        return JSON.parseObject(value, clazz, features);
    }

    public static <T> T fromJson(String value, TypeReference<T> type, Feature... features) {
        if(type.equals(String.class)) {
            return (T) value;
        }
        features = ArrayUtils.getArray(fromJsonFeatures, features);
        if(features == null) {
            features = new Feature[0];
        }
        return JSON.parseObject(value, type, features);
    }

    /**
     * 把json字符串转化为对象
     * <p>
     *     可指定json路径，例如：json字符串为：{total: 2, rows: [{}, {}]}，则获取total的路径为$.total
     * </p>
     * @param value
     * @param path
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String value, String path, Class<T> clazz, Feature... features) {
        if(path == null) {
            return fromJson(value, clazz);
        }
        Object pathValue = JSONPath.read(value, path);
        if(pathValue == null) {
            return null;
        }
        return fromJson(pathValue.toString(), clazz, features);
    }

    public static List<SerializeFilter> getFilters() {
        return filters;
    }

    public static void addFilter(SerializeFilter filter) {
        if(JsonUtils.filters == null) {
            JsonUtils.filters = new ArrayList<>();
        }
        JsonUtils.filters.add(filter);
    }

    public static void setFilters(List<SerializeFilter> filters) {
        JsonUtils.filters = filters;
    }

    public static SerializeConfig getConfig() {
        return config;
    }

    public static void setConfig(SerializeConfig config) {
        JsonUtils.config = config;
    }

    public static List<SerializerFeature> getToJsonFeatures() {
        return toJsonFeatures;
    }

    public static void addToJsonFeature(SerializerFeature feature) {
        if(JsonUtils.toJsonFeatures == null) {
            JsonUtils.toJsonFeatures = new ArrayList<>();
        }
        JsonUtils.toJsonFeatures.add(feature);
    }

    public static void setToJsonFeatures(List<SerializerFeature> toJsonFeatures) {
        JsonUtils.toJsonFeatures = toJsonFeatures;
    }

    public static List<Feature> getFromJsonFeatures() {
        return fromJsonFeatures;
    }

    public static void addFromJsonFeature(Feature feature) {
        if(JsonUtils.fromJsonFeatures == null) {
            JsonUtils.fromJsonFeatures = new ArrayList<>();
        }
        JsonUtils.fromJsonFeatures.add(feature);
    }

    public static void setFromJsonFeatures(List<Feature> fromJsonFeatures) {
        JsonUtils.fromJsonFeatures = fromJsonFeatures;
    }

}
