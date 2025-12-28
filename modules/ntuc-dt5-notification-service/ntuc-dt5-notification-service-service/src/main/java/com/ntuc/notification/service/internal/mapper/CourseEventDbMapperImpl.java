package com.ntuc.notification.service.internal.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntuc.notification.model.CourseEvent;
import com.ntuc.notification.service.mapper.CourseEventDbMapper;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import java.util.Collections;
import java.util.List;

@Component(service = CourseEventDbMapper.class)
public class CourseEventDbMapperImpl implements CourseEventDbMapper {

    private static final TypeReference<List<String>> LIST_STR =
            new TypeReference<List<String>>() {};

    private volatile ObjectMapper objectMapper;

    @Activate
    protected void activate() {
        // Single mapper instance per component lifecycle
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String toChangeFromDbString(CourseEvent event) {
        try {
            List<String> changeFrom = (event == null) ? null : event.getChangeFrom();
            return (changeFrom == null) ? "[]" : objectMapper.writeValueAsString(changeFrom);
        } catch (Exception e) {
            return "[]";
        }
    }

    @Override
    public void fromChangeFromDbString(CourseEvent event, String dbValue) {
        if (event == null) return;

        try {
            if (dbValue == null || dbValue.trim().isEmpty()) {
                event.setChangeFrom(Collections.<String>emptyList());
                return;
            }

            List<String> parsed = objectMapper.readValue(dbValue, LIST_STR);
            event.setChangeFrom((parsed != null) ? parsed : Collections.<String>emptyList());
        } catch (Exception e) {
            event.setChangeFrom(Collections.<String>emptyList());
        }
    }
}
