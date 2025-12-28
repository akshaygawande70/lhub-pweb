package com.ntuc.notification.audit.internal;

import com.ntuc.notification.constants.ParameterKeyEnum;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Pure Java (no OSGi). Unit-test this class.
 *
 * Rules:
 * - If config contains "NONE": never log.
 * - Else if config contains "ALL" or the config list is empty: log everything.
 * - Else: only log if normalized action is present in configured set.
 */
public class AuditActionFilter {

    private final boolean logAll;
    private final boolean logNone;
    private final Set<String> loggableActions; // normalized upper-case

    public AuditActionFilter(boolean logAll, boolean logNone, Set<String> loggableActions) {
        this.logAll = logAll;
        this.logNone = logNone;
        this.loggableActions = Collections.unmodifiableSet(new LinkedHashSet<>(loggableActions));
    }

    public boolean isLoggable(String action) {
        if (logNone) return false;
        if (logAll) return true;
        return loggableActions.contains(normalize(action));
    }

    public String[] getConfiguredActions() {
        return loggableActions.toArray(new String[0]);
    }

    public boolean isLogAll() {
        return logAll;
    }

    public boolean isLogNone() {
        return logNone;
    }

    public static AuditActionFilter fromParameterValues(
            Map<ParameterKeyEnum, Object> paramValues,
            List<ParameterKeyEnum> actionKeys) {

        if (paramValues == null || actionKeys == null || actionKeys.isEmpty()) {
            return new AuditActionFilter(true, false, Collections.emptySet());
        }

        List<String> combined = new ArrayList<>();
        for (ParameterKeyEnum key : actionKeys) {
            combined.addAll(readActions(paramValues.get(key)));
        }

        Set<String> normalized = combined.stream()
                .filter(Objects::nonNull)
                .map(AuditActionFilter::normalize)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        boolean logAll = normalized.isEmpty() || normalized.contains("ALL");
        boolean logNone = normalized.contains("NONE");

        return new AuditActionFilter(logAll, logNone, normalized);
    }

    private static List<String> readActions(Object val) {
        if (val == null) return Collections.emptyList();

        if (val instanceof String[]) {
            List<String> out = new ArrayList<>();
            for (String s : (String[]) val) {
                out.addAll(splitCsvOrSingleton(s));
            }
            return out;
        }

        if (val instanceof String) {
            return splitCsvOrSingleton((String) val);
        }

        return Collections.emptyList();
    }

    private static List<String> splitCsvOrSingleton(String s) {
        if (s == null) return Collections.emptyList();
        String trimmed = s.trim();
        if (trimmed.isEmpty()) return Collections.emptyList();

        if (trimmed.contains(",")) {
            String[] parts = trimmed.split(",");
            List<String> out = new ArrayList<>();
            for (String p : parts) {
                String t = p.trim();
                if (!t.isEmpty()) out.add(t);
            }
            return out;
        }

        return Collections.singletonList(trimmed);
    }

    private static String normalize(String s) {
        return s == null ? "" : s.trim().toUpperCase(Locale.ROOT);
    }
}
