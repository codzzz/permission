package com.kuzan.permission.util;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by sui on 2019/3/21.
 */
@Slf4j
public class JsonMapper {
    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        //config
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    public static <T> String obj2String(T t){
        if (t == null){
            return null;
        }
        try {
            return t instanceof String ? (String) t : objectMapper.writeValueAsString(t);
        }catch (Exception ex){
            log.warn("parse object to string exception",ex);
            return null;
        }
    }

    public static <T> T string2Obj(String s, TypeReference<T> typeReference){
        if (s == null || typeReference == null){
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class) ?  s : objectMapper.readValue(s,typeReference));
        }catch (Exception ex){
            log.warn("parse string to object exception,String:{},TypeReference<T>:{},error:",s,typeReference.getType(),ex);
            return null;
        }
    }
}
