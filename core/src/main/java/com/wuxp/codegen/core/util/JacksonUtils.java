package com.wuxp.codegen.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wuxp.codegen.core.exception.CodegenRuntimeException;

import java.util.Collection;

public final class JacksonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JacksonUtils() {
        throw new AssertionError();
    }

    public static <T> T parse(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException exception) {
            throw new CodegenRuntimeException("parse json failure", exception);
        }
    }

    public static <T> Collection<T> parseCollections(String json, Class<T> clazz) {
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(Collection.class, clazz);
        try {
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (JsonProcessingException exception) {
            throw new CodegenRuntimeException("parse json failure", exception);
        }
    }

    public static String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception exception) {
            throw new CodegenRuntimeException("object to json failure", exception);
        }
    }

}
