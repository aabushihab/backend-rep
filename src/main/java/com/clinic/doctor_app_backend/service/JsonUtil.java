package com.clinic.doctor_app_backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(List<Long> list) {
        try {
            return mapper.writeValueAsString(list);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Long> fromJson(String json) {
        try {
            return mapper.readValue(json, new TypeReference<List<Long>>() {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

