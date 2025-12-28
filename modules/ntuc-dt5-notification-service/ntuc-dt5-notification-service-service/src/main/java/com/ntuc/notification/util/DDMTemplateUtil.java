package com.ntuc.notification.util;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Utility for resolving Liferay DDM Template and Structure metadata for a site.
 *
 * Business purpose:
 * Enables downstream workflows to select the correct structure/template pair by human-friendly names
 * and persist stable identifiers/keys for content creation and updates.
 *
 * Technical purpose:
 * Wraps DDMTemplateLocalService / DDMStructureLocalService lookups and shields callers from
 * dynamic-query raw result lists and defensive casting concerns.
 *
 * Notes:
 * - DDMTemplate names are localized; this class uses getNameCurrentValue() for comparison and logging.
 * - DynamicQuery returns raw List<?>; this class casts element-by-element (never List cast).
 *
 * @author @akshaygawande
 */
@Component(service = DDMTemplateUtil.class)
public class DDMTemplateUtil {

    private static final Log _log = LogFactoryUtil.getLog(DDMTemplateUtil.class);

    @Reference
    private DDMTemplateLocalService ddmTemplateLocalService;

    @Reference
    private DDMStructureLocalService ddmStructureLocalService;

    /**
     * Test-only constructor to allow plain JUnit execution without an OSGi container.
     *
     * Business purpose:
     * Enables deterministic verification of template/structure resolution behavior.
     *
     * Technical purpose:
     * Allows injecting mocked Liferay services for unit tests.
     *
     * Inputs/Invariants:
     * - Services must be non-null for meaningful execution.
     *
     * Side effects:
     * - None (no DB writes; read-only lookups via injected services).
     *
     * @param ddmTemplateLocalService injected template service (usually a Mockito mock in tests)
     * @param ddmStructureLocalService injected structure service (usually a Mockito mock in tests)
     */
    DDMTemplateUtil(
            DDMTemplateLocalService ddmTemplateLocalService,
            DDMStructureLocalService ddmStructureLocalService) {

        this.ddmTemplateLocalService = ddmTemplateLocalService;
        this.ddmStructureLocalService = ddmStructureLocalService;
    }

    /**
     * Resolves template + structure metadata by template name (case-insensitive) for a site.
     *
     * Business purpose:
     * Finds the intended course/content template by name and returns stable keys/ids for publishing.
     *
     * Technical purpose:
     * Iterates group templates and, on match, resolves the backing structure via template classPK.
     *
     * Inputs/Invariants:
     * - groupId must belong to the site whose templates should be searched.
     * - templateName must be non-blank; comparison is case-insensitive against current locale value.
     *
     * Side effects:
     * - Reads from Liferay services (template list + structure fetch).
     * - Logs warnings/errors for blank input and exceptions.
     *
     * Return semantics:
     * - Returns Optional.empty() when input is blank, no match is found, or an exception occurs.
     *
     * @param groupId site/group id
     * @param templateName human-friendly template name to match (case-insensitive)
     * @return resolved template + structure info, or empty if not found / error
     */
    public Optional<TemplateStructureInfo> getTemplateStructureInfo(long groupId, String templateName) {
        if (templateName == null || templateName.trim().isEmpty()) {
            _log.warn("Template name is blank");
            return Optional.empty();
        }

        try {
            List<DDMTemplate> templates = ddmTemplateLocalService.getTemplates(groupId);

            // Linear scan keeps behavior explicit and avoids assumptions about name indexing/localization.
            for (DDMTemplate template : templates) {
                String currentName = template.getNameCurrentValue();

                if (currentName != null && currentName.equalsIgnoreCase(templateName)) {
                    long structureId = template.getClassPK();

                    // classPK is expected to point to the related DDMStructure for structure-backed templates.
                    DDMStructure structure = ddmStructureLocalService.getDDMStructure(structureId);

                    return Optional.of(
                            new TemplateStructureInfo(
                                    structure.getStructureId(),
                                    structure.getStructureKey(),
                                    structure.getNameCurrentValue(),
                                    template.getTemplateId(),
                                    template.getTemplateKey(),
                                    template.getNameCurrentValue()
                            )
                    );
                }
            }
        }
        catch (PortalException e) {
            _log.error("Error resolving template/structure for groupId=" + groupId + ", templateName=" + templateName, e);
        }
        catch (RuntimeException e) {
            // Defensive: keep callers stable; this class is frequently used by orchestrations.
            _log.error("Unexpected error resolving template/structure for groupId=" + groupId + ", templateName=" + templateName, e);
        }

        return Optional.empty();
    }

    /**
     * Searches for a DDMTemplate by site/groupId and a partial template name match.
     *
     * Business purpose:
     * Supports flexible lookups when the caller has only a partial template name (e.g., configured label).
     *
     * Technical purpose:
     * Executes a DynamicQuery filtered by groupId and a LIKE on the persisted name column, then
     * returns the first DDMTemplate instance from the raw results list.
     *
     * Inputs/Invariants:
     * - groupId must be the target site.
     * - templateName must be non-blank.
     *
     * Side effects:
     * - Reads from Liferay persistence via dynamic query.
     * - Logs an info line on first match; logs warnings for blank input/no results; logs errors on exceptions.
     *
     * Return semantics:
     * - Returns null when input is blank, no match is found, or an exception occurs.
     *
     * @param groupId site/group id
     * @param templateName partial template name used for LIKE matching
     * @return first matching DDMTemplate, or null when not found/error
     */
    public DDMTemplate getTemplateByNameAndGroupId(long groupId, String templateName) {
        if (templateName == null || templateName.trim().isEmpty()) {
            _log.warn("Template name is blank");
            return null;
        }

        try {
            DynamicQuery dq = ddmTemplateLocalService.dynamicQuery();

            dq.add(PropertyFactoryUtil.forName("groupId").eq(groupId));

            // Note: This relies on how the DDMTemplate "name" field is persisted/indexed in the target Liferay version.
            // Localized names may not behave as expected with LIKE depending on DB/schema; caller may prefer exact match
            // via getTemplateStructureInfo() when names are known precisely.
            dq.add(PropertyFactoryUtil.forName("name").like("%" + templateName + "%"));

            List<?> results = ddmTemplateLocalService.dynamicQuery(dq);

            for (Object obj : results) {
                // DynamicQuery returns raw List<?>; cast element-by-element only.
                if (obj instanceof DDMTemplate) {
                    DDMTemplate template = (DDMTemplate) obj;

                    _log.info(
                            "Found template: " + template.getNameCurrentValue() +
                                    " (templateKey=" + template.getTemplateKey() + ")"
                    );
                    return template;
                }
            }

            _log.warn("No DDMTemplate found for name='" + templateName + "' in groupId=" + groupId);
            return null;
        }
        catch (Exception e) {
            _log.error("Error while searching for DDMTemplate for groupId=" + groupId + ", templateName=" + templateName, e);
            return null;
        }
    }

    /**
     * Immutable DTO holding the resolved structure + template identity and display metadata.
     *
     * Business purpose:
     * Provides a stable, serializable metadata payload for orchestration layers that need to
     * persist and report which structure/template were used.
     *
     * Technical purpose:
     * Encapsulates ids/keys/names without exposing Liferay model objects to callers.
     */
    public static class TemplateStructureInfo implements Serializable {

        private static final long serialVersionUID = 1L;

        private final long structureId;
        private final String structureKey;
        private final String structureName;
        private final long templateId;
        private final String templateKey;
        private final String templateName;

        /**
         * Creates an immutable structure/template metadata object.
         *
         * Business purpose:
         * Captures the exact structure/template selection used for content operations.
         *
         * Technical purpose:
         * Stores both ids (DB identity) and keys/names (stable references and display values).
         *
         * Inputs/Invariants:
         * - Ids are expected to be valid Liferay ids when constructed from service lookups.
         *
         * Side effects:
         * - None.
         *
         * @param structureId resolved DDMStructure id
         * @param structureKey resolved DDMStructure key
         * @param structureName resolved DDMStructure display name (current locale)
         * @param templateId resolved DDMTemplate id
         * @param templateKey resolved DDMTemplate key
         * @param templateName resolved DDMTemplate display name (current locale)
         */
        public TemplateStructureInfo(
                long structureId,
                String structureKey,
                String structureName,
                long templateId,
                String templateKey,
                String templateName) {

            this.structureId = structureId;
            this.structureKey = structureKey;
            this.structureName = structureName;
            this.templateId = templateId;
            this.templateKey = templateKey;
            this.templateName = templateName;
        }

        /**
         * Business purpose:
         * Identifies which structure was used for content fields.
         *
         * Technical purpose:
         * Returns the DDMStructure primary key.
         *
         * @return structure id
         */
        public long getStructureId() {
            return structureId;
        }

        /**
         * Business purpose:
         * Provides a stable, human-configurable structure identifier.
         *
         * Technical purpose:
         * Returns the DDMStructure key.
         *
         * @return structure key
         */
        public String getStructureKey() {
            return structureKey;
        }

        /**
         * Business purpose:
         * Provides a display-friendly structure name for logs/reports.
         *
         * Technical purpose:
         * Returns the localized structure name in the current value.
         *
         * @return structure display name
         */
        public String getStructureName() {
            return structureName;
        }

        /**
         * Business purpose:
         * Identifies which template was used to render/display content.
         *
         * Technical purpose:
         * Returns the DDMTemplate primary key.
         *
         * @return template id
         */
        public long getTemplateId() {
            return templateId;
        }

        /**
         * Business purpose:
         * Provides a stable, human-configurable template identifier.
         *
         * Technical purpose:
         * Returns the DDMTemplate key.
         *
         * @return template key
         */
        public String getTemplateKey() {
            return templateKey;
        }

        /**
         * Business purpose:
         * Provides a display-friendly template name for logs/reports.
         *
         * Technical purpose:
         * Returns the localized template name in the current value.
         *
         * @return template display name
         */
        public String getTemplateName() {
            return templateName;
        }

        /**
         * Business purpose:
         * Provides a readable representation for troubleshooting and audit trails.
         *
         * Technical purpose:
         * Returns a deterministic string containing ids/keys/names.
         *
         * @return string representation
         */
        @Override
        public String toString() {
            return "TemplateStructureInfo{" +
                    "structureId=" + structureId +
                    ", structureKey='" + structureKey + '\'' +
                    ", structureName='" + structureName + '\'' +
                    ", templateId=" + templateId +
                    ", templateKey='" + templateKey + '\'' +
                    ", templateName='" + templateName + '\'' +
                    '}';
        }
    }
}
