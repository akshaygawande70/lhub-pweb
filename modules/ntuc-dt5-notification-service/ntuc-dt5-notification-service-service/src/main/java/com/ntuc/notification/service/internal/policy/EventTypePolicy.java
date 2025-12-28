package com.ntuc.notification.service.internal.policy;

import com.ntuc.notification.constants.NotificationType;

import java.util.Arrays;
import java.util.List;

/**
 * Determines whether an event type should be processed.
 * Pure Java for JUnit.
 */
public class EventTypePolicy {

    private static final List<String> REALTIME_SUPPORTED = Arrays.asList(
        NotificationType.PUBLISHED,
        NotificationType.UNPUBLISHED,
        NotificationType.CHANGED,
        NotificationType.INACTIVE
    );

    public boolean shouldProcess(String eventType, boolean isCron) {
        if (eventType == null) return false;

        if (isCron) {
            return NotificationType.CHANGED.equalsIgnoreCase(eventType);
        }

        for (String t : REALTIME_SUPPORTED) {
            if (t.equalsIgnoreCase(eventType)) {
                return true;
            }
        }

        return false;
    }
}
