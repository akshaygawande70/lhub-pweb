package com.ntuc.notification.service.internal.journal;

import com.ntuc.notification.journal.api.DynamicElementValueExtractor;
import com.ntuc.notification.service.impl.XmlDynamicElementExtractor;

/**
 * Default implementation that delegates to existing XmlDynamicElementExtractor.
 *
 * NOTE:
 * - Keeps backward compatibility while allowing call sites to depend on an interface.
 * - Later you can replace internals with a faster parser without changing callers.
 */
public class XmlDynamicElementValueExtractor implements DynamicElementValueExtractor {

    @Override
    public String extract(String xmlContent, String fieldName) {
        if (xmlContent == null || xmlContent.trim().isEmpty()) {
            return "";
        }
        if (fieldName == null || fieldName.trim().isEmpty()) {
            return "";
        }

        String v = XmlDynamicElementExtractor.extractDynamicElementValue(xmlContent, fieldName);
        return v == null ? "" : v.trim();
    }
}
