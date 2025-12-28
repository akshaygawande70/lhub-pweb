package com.ntuc.notification.constants;

public class NotificationType {
	public static final String PUBLISHED = "published";
	public static final String UNPUBLISHED = "unpublished";
	public static final String CHANGED = "changed";
	public static final String INACTIVE = "inactive";
	
	// Prevent instantiation
    private NotificationType() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }
}
