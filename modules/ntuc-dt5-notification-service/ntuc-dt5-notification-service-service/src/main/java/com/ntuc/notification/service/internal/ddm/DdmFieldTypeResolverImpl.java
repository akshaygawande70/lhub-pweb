package com.ntuc.notification.service.internal.ddm;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.ntuc.notification.service.DdmFieldTypeResolver;

import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Cached resolver for DDM field types.
 *
 * Cache key: groupId + ":" + ddmStructureKey
 * Invalidation: TTL + modifiedDate change
 */
@Component(service = DdmFieldTypeResolver.class)
public class DdmFieldTypeResolverImpl implements DdmFieldTypeResolver {

    private static final Log _log = LogFactoryUtil.getLog(DdmFieldTypeResolverImpl.class);

    // Safe defaults
    private static final long TTL_MS = 15L * 60L * 1000L; // 15 minutes

    private final ConcurrentHashMap<String, CacheEntry> _cache = new ConcurrentHashMap<>();

    @Reference
    private DDMStructureLocalService _ddmStructureLocalService;

    @Override
    public Map<String, String> resolve(long groupId, String ddmStructureKey) {
        if (groupId <= 0 || isBlank(ddmStructureKey)) {
            return Collections.emptyMap();
        }

        final String key = groupId + ":" + ddmStructureKey.trim();

        try {
            CacheEntry cached = _cache.get(key);
            long now = System.currentTimeMillis();

            if (cached != null && !cached.isExpired(now)) {
                // If we can detect the structure modified date cheaply, validate it.
                Date currentModified = fetchModifiedDate(groupId, ddmStructureKey);
                if (currentModified != null && cached.structureModified != null &&
                        currentModified.getTime() == cached.structureModified.getTime()) {
                    return cached.map;
                }

                // If modifiedDate differs (or couldn't be fetched), refresh when TTL expired only.
                if (!cached.isExpired(now)) {
                    return cached.map;
                }
            }

            Map<String, String> resolved = buildMap(groupId, ddmStructureKey);
            Date modified = fetchModifiedDate(groupId, ddmStructureKey);

            CacheEntry entry = new CacheEntry(
                    now,
                    modified,
                    resolved
            );

            _cache.put(key, entry);
            return resolved;
        }
        catch (Throwable t) {
            if (_log.isWarnEnabled()) {
                _log.warn("DDM field type resolve failed for groupId=" + groupId +
                        " structureKey=" + ddmStructureKey + " : " + t.getMessage(), t);
            }
            return Collections.emptyMap();
        }
    }

    private Map<String, String> buildMap(long groupId, String ddmStructureKey) throws Exception {
        long classNameId = PortalUtil.getClassNameId(JournalArticle.class);

        DDMStructure structure = _ddmStructureLocalService.fetchStructure(groupId, classNameId, ddmStructureKey);
        if (structure == null) {
            if (_log.isWarnEnabled()) {
                _log.warn("DDMStructure not found for groupId=" + groupId +
                        ", classNameId=" + classNameId +
                        ", structureKey=" + ddmStructureKey);
            }
            return Collections.emptyMap();
        }

        DDMForm form = structure.getDDMForm();
        if (form == null || form.getDDMFormFields() == null) {
            return Collections.emptyMap();
        }

        Map<String, String> out = new LinkedHashMap<>();
        for (DDMFormField f : form.getDDMFormFields()) {
            walk(out, /*prefix*/ null, f);
        }

        return Collections.unmodifiableMap(out);
    }

    private void walk(Map<String, String> out, String prefix, DDMFormField f) {
        if (f == null) return;

        String name = safe(f.getName());
        if (name.isEmpty()) return;

        String path = (prefix == null || prefix.isEmpty()) ? name : (prefix + "." + name);

        // Liferay field "type" we need for XML is typically the DDMFormField type (e.g. "text", "textarea", "fieldset")
        // Your XML builder expects values like "text", "text_area", "fieldset".
        // Normalize here to keep downstream logic simple.
        out.put(path, normalizeType(safe(f.getType())));

        List<DDMFormField> nested = f.getNestedDDMFormFields();
        if (nested != null && !nested.isEmpty()) {
            for (DDMFormField nf : nested) {
                walk(out, path, nf);
            }
        }
    }

    private static String normalizeType(String ddmType) {
        if (ddmType == null) return "text_area";

        String t = ddmType.trim().toLowerCase();
        if (t.isEmpty()) return "text_area";

        // Normalize common DDM names into what your XML builder uses
        if ("textarea".equals(t)) return "text_area";
        if ("text".equals(t)) return "text";
        if ("fieldset".equals(t)) return "fieldset";

        // Accept already-normalized values
        if ("text_area".equals(t)) return "text_area";

        // Default safe behavior: treat unknown as text_area
        return "text_area";
    }

    private Date fetchModifiedDate(long groupId, String ddmStructureKey) {
        try {
            long classNameId = PortalUtil.getClassNameId(JournalArticle.class);
            DDMStructure s = _ddmStructureLocalService.fetchStructure(groupId, classNameId, ddmStructureKey);
            return (s != null) ? s.getModifiedDate() : null;
        }
        catch (Throwable ignore) {
            return null;
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String safe(String s) {
        return (s == null) ? "" : s.trim();
    }

    private static final class CacheEntry {
        final long createdAtMs;
        final Date structureModified;
        final Map<String, String> map;

        CacheEntry(long createdAtMs, Date structureModified, Map<String, String> map) {
            this.createdAtMs = createdAtMs;
            this.structureModified = structureModified;
            this.map = (map == null) ? Collections.<String, String>emptyMap() : map;
        }

        boolean isExpired(long nowMs) {
            return (nowMs - createdAtMs) > TTL_MS;
        }
    }
}
