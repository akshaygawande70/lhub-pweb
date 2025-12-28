package com.ntuc.notification.service.mapper;

import com.ntuc.notification.model.CourseEvent;

public interface CourseEventDbMapper {

    String toChangeFromDbString(CourseEvent event);

    void fromChangeFromDbString(CourseEvent event, String dbValue);
}
