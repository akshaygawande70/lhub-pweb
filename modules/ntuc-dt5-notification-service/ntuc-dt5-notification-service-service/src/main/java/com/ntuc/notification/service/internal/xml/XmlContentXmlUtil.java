package com.ntuc.notification.service.internal.xml;

import org.dom4j.Element;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Pure XML builder/upserter for JournalArticle "content" payloads.
 *
 * IMPORTANT:
 * - Uses dom4j directly (no Liferay SAXReaderUtil / PortalUtil).
 * - Deterministic tree creation for nested paths.
 * - Idempotent upsert: calling twice doesn't duplicate nodes.
 *
 * NOTE:
 * This class is intentionally static to match existing call sites/tests.
 */
public final class XmlContentXmlUtil {

    private static final String DYNAMIC_ELEMENT = "dynamic-element";
    private static final String DYNAMIC_CONTENT = "dynamic-content";

    private XmlContentXmlUtil() {
        // util
    }

    /**
     * Upsert a field path into the JournalArticle content tree.
     *
     * Behavior (aligned to unit tests):
     * - Creates missing dynamic-element nodes for a dotted field path.
     * - Sets:
     *   - type = ddmType
     *   - index-type = keyword
     *   - instance-id = deterministic from full path + index
     * - Ensures exactly one dynamic-content per language-id.
     *
     * @param root root element ("root")
     * @param fieldPath dotted path, e.g. "schedule.duration.hours" or direct "courseTitle"
     * @param ddmType ddm type, e.g. "text"
     * @param langs language ids, e.g. ["en_US"]
     * @param value value text (null -> "")
     */
    public static void upsertFieldElement(
            Element root,
            String fieldPath,
            String ddmType,
            String[] langs,
            String value
    ) {
        Objects.requireNonNull(root, "root");
        Objects.requireNonNull(fieldPath, "fieldPath");
        Objects.requireNonNull(ddmType, "ddmType");
        Objects.requireNonNull(langs, "langs");

        String cleanedPath = normalizePath(fieldPath);
        String[] parts = cleanedPath.split("\\.");

        Element current = root;

        // create path: schedule -> duration -> hours
        for (int i = 0; i < parts.length; i++) {
            String name = parts[i];
            Element next = findDirectChildByName(current, name);

            if (next == null) {
                next = current.addElement(DYNAMIC_ELEMENT);
                next.addAttribute("name", name);

                // leaf-level attributes are asserted by tests; keep them on all nodes for stability.
                next.addAttribute("type", ddmType);
                next.addAttribute("index-type", "keyword");

                // instance-id deterministic:
                // - leaf uses full dotted path
                // - intermediate nodes use their own cumulative path
                String cumulativePath = joinParts(parts, i);
                next.addAttribute("instance-id", instanceId(cumulativePath, 0));
            } else {
                // Ensure attributes exist / stay consistent (idempotency).
                if (next.attribute("type") == null) {
                    next.addAttribute("type", ddmType);
                }
                if (next.attribute("index-type") == null) {
                    next.addAttribute("index-type", "keyword");
                }
                if (next.attribute("instance-id") == null) {
                    String cumulativePath = joinParts(parts, i);
                    next.addAttribute("instance-id", instanceId(cumulativePath, 0));
                }
            }

            current = next;
        }

        // current is leaf
        upsertDynamicContent(current, langs, value == null ? "" : value);
    }

    /**
     * Find a direct child dynamic-element by name attribute.
     */
    public static Element findDirectChildByName(Element parent, String name) {
        if (parent == null || name == null) {
            return null;
        }

        List<Element> children = parent.elements(DYNAMIC_ELEMENT);

        for (Element e : children) {
            if (name.equals(e.attributeValue("name"))) {
                return e;
            }
        }
        return null;
    }

    /**
     * Deterministic, stable instance-id generator aligned with tests.
     *
     * @param fullPath dotted path (e.g. courseTitle or schedule.duration.hours)
     * @param index repeatable index (0-based)
     */
    public static String instanceId(String fullPath, int index) {
        String input = normalizePath(fullPath) + "#" + index;
        // 8 chars expected in your earlier test logic; keep stable & short.
        return UUID.nameUUIDFromBytes(input.getBytes(StandardCharsets.UTF_8))
                .toString()
                .replace("-", "")
                .substring(0, 8);
    }

    private static void upsertDynamicContent(Element fieldElement, String[] langs, String value) {
        for (String lang : langs) {
            if (lang == null || lang.trim().isEmpty()) {
                continue;
            }
            Element content = findDynamicContent(fieldElement, lang);

            if (content == null) {
                content = fieldElement.addElement(DYNAMIC_CONTENT);
                content.addAttribute("language-id", lang);
            }

            // Idempotent: overwrite same language node text; do not add duplicates.
            content.setText(value);
        }
    }

    private static Element findDynamicContent(Element fieldElement, String lang) {
        List<Element> contents = fieldElement.elements(DYNAMIC_CONTENT);

        for (Element e : contents) {
            if (lang.equals(e.attributeValue("language-id"))) {
                return e;
            }
        }
        return null;
    }

    private static String normalizePath(String path) {
        String cleaned = path.trim();
        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("fieldPath must not be blank");
        }
        // unify separators if someone passes slash by mistake
        cleaned = cleaned.replace('/', '.').replace('\\', '.');
        while (cleaned.startsWith(".")) cleaned = cleaned.substring(1);
        while (cleaned.endsWith(".")) cleaned = cleaned.substring(0, cleaned.length() - 1);
        return cleaned;
    }

    private static String joinParts(String[] parts, int uptoIndexInclusive) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= uptoIndexInclusive; i++) {
            if (i > 0) {
                sb.append('.');
            }
            sb.append(parts[i]);
        }
        return sb.toString();
    }
}
