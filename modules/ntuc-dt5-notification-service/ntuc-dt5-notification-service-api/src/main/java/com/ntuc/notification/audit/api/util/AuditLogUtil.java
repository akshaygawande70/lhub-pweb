package com.ntuc.notification.audit.api.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Helpers for consistent log prefixing, safe exception messages,
 * and shallow masking of sensitive fields before auditing/logging.
 */
public final class AuditLogUtil {
	private static final int MAX_MSG_LEN = 75;

    // Keys we never log in clear text
    private static final String[] SENSITIVE_KEYS = {
        "password","pass","token","accessToken","refreshToken",
        "apiKey","secret","clientSecret","authorization","authHeader","bearer","privateKey"
    };

    public static String prefix(String corrId, Class<?> cls) {
        return "[corrId=" + corrId + "][fqcn=" + cls.getName() + "] ";
    }

    public static String safeMsg(Throwable t) {
        if (t == null || t.getMessage() == null) return "";
        String m = t.getMessage();
        return m.length() > MAX_MSG_LEN ? m.substring(0, MAX_MSG_LEN) + "…" : m;
    }

    /** Shallow-mask meta map values for known sensitive keys. */
    public static Map<String,Object> mask(Map<String,?> meta) {
    	if (meta == null || meta.isEmpty()) return Collections.emptyMap();
        Map<String,Object> out = new HashMap<>(meta.size());
        meta.forEach((k,v) -> out.put(k, isSensitive(k) ? maskValue(v) : v));
        return out;
    }

    private static boolean isSensitive(String key) {
        if (key == null) return false;
        String k = key.toLowerCase();
        for (String s : SENSITIVE_KEYS) {
            if (k.contains(s.toLowerCase())) return true;
        }
        return false;
    }

    private static Object maskValue(Object v) {
        if (v == null) return null;
        String s = String.valueOf(v);
        if (s.length() <= 6) return "******";
        return s.substring(0, 2) + "******" + s.substring(s.length() - 2);
        // Alternative: return "****(masked)****";
    }

    private AuditLogUtil() {}
}
