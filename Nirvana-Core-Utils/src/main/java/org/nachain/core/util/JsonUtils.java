package org.nachain.core.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class JsonUtils {


    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    public static String objectToJson(Object data) {
        try {
            String string = mapper.writeValueAsString(data);
            return string;
        } catch (JsonProcessingException e) {
            log.error("Object to json error.", e);
        }
        return null;
    }


    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            T t = mapper.readValue(jsonData, beanType);
            return t;
        } catch (Exception e) {
            log.error(String.format("jsonData=%s\nClass beanType=%s", jsonData, beanType), e);
        }
        return null;
    }


    public static <T> T jsonToPojo(String jsonData, TypeReference<T> typeReference) {
        try {
            T t = mapper.readValue(jsonData, typeReference);
            return t;
        } catch (Exception e) {
            log.error("jsonData:" + jsonData, e);
        }
        return null;
    }


    public static <T> T tryJsonToPojo(String jsonData, Class<T> beanType) {
        try {
            T t = mapper.readValue(jsonData, beanType);
            return t;
        } catch (Exception e) {
        }
        return null;
    }


    public static <T> T tryJsonToPojo(String jsonData, TypeReference<T> typeReference) {
        try {
            T t = mapper.readValue(jsonData, typeReference);
            return t;
        } catch (Exception e) {
        }
        return null;
    }


    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            List<T> list = mapper.readValue(jsonData, javaType);
            return list;
        } catch (Exception e) {
            log.error("jsonData:" + jsonData, e);
        }
        return null;
    }
}