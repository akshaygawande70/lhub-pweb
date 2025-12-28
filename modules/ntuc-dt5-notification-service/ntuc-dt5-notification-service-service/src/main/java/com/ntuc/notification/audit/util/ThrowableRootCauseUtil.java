package com.ntuc.notification.audit.util;

public final class ThrowableRootCauseUtil {
    
	private ThrowableRootCauseUtil() {}


    public static Throwable rootCause(Throwable t) {
        if (t == null) {
            return null;
        }
        Throwable cur = t;
        while (cur.getCause() != null && cur.getCause() != cur) {
            cur = cur.getCause();
        }
        return cur;
    }

    public static String safeMessage(Throwable t) {
        if (t == null) {
            return "";
        }
        String m = t.getMessage();
        if (m == null || m.trim().isEmpty()) {
            return t.getClass().getSimpleName();
        }
        return m.trim();
    }
}
