package com.ntuc.notification.service.internal.payload;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntuc.notification.model.CourseResponse;

/**
 * Pure helper for converting CourseResponse <-> JSON snapshot.
 *
 * <p>Used to pass a payload snapshot from CRITICAL to NON-CRITICAL without a second CLS call.
 */
public class CoursePayloadJsonCodec {

    public String toJson(ObjectMapper mapper, CourseResponse response) {
        if (mapper == null || response == null) {
            return "";
        }
        try {
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            return "";
        }
    }

    public CourseResponse fromJson(ObjectMapper mapper, String json) {
        if (mapper == null || json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            return mapper.readValue(json, CourseResponse.class);
        } catch (Exception e) {
            return null;
        }
    }
}
