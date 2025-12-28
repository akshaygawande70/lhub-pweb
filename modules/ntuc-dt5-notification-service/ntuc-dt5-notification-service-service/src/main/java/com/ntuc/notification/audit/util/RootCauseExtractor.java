package com.ntuc.notification.audit.util;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Extracts root cause from a live Throwable chain.
 *
 * IMPORTANT:
 * - Audit fingerprinting must not depend on Throwable instances.
 * - This utility is for runtime-only scenarios.
 */
public class RootCauseExtractor {

    private static final int MAX_CAUSE_DEPTH = 25;

    public Throwable extract(Throwable t) {
        if (t == null) {
            return null;
        }

        Throwable cur = t;
        Throwable last = t;

        Map<Throwable, Boolean> seen =
            Collections.synchronizedMap(new IdentityHashMap<Throwable, Boolean>());

        int depth = 0;
        while (cur != null && depth++ < MAX_CAUSE_DEPTH) {
            if (seen.put(cur, Boolean.TRUE) != null) {
                break; // cycle detected
            }
            last = cur;
            cur = cur.getCause();
        }

        return last;
    }
}
