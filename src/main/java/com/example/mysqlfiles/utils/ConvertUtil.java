package com.example.mysqlfiles.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertUtil {



    public   static <T> T convertObject(Object object,Class <T> clazz) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        T responseObject = null;
        String objectStr = objectMapper.writeValueAsString(object);
        responseObject = (T) objectMapper.readValue(objectStr, clazz);
        return responseObject;
    }

}
