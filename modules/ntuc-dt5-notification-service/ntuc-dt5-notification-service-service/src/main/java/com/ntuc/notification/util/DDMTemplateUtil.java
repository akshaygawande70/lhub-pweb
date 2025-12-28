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
 * Utility class to retrieve DDM Template and Structure metadata.
 *
 * NOTE:
 * - Uses DynamicQuery → returns raw List<?>
 * - Casting handled defensively (element-level)
 */
@Component(service = DDMTemplateUtil.class)
public class DDMTemplateUtil {

    private static final Log _log = LogFactoryUtil.getLog(DDMTemplateUtil.class);

    @Reference
    private DDMTemplateLocalService ddmTemplateLocalService;

    @Reference
    private DDMStructureLocalService ddmStructureLocalService;

    /**
     * Returns structure + template info by template name (case-insensitive).
     */
    public Optional<TemplateStructureInfo> getTemplateStructureInfo(long groupId, String templateName) {
        if (templateName == null || templateName.trim().isEmpty()) {
            _log.warn("Template name is blank");
            return Optional.empty();
        }

        try {
            List<DDMTemplate> templates = ddmTemplateLocalService.getTemplates(groupId);

            for (DDMTemplate template : templates) {
                String name = template.getNameCurrentValue();
                if (name != null && name.equalsIgnoreCase(templateName)) {

                    long structureId = template.getClassPK();
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
        } catch (PortalException e) {
            _log.error("Error resolving template/structure", e);
        }

        return Optional.empty();
    }

    /**
     * Find template by groupId + partial name match.
     *
     * FIX:
     * - DynamicQuery returns List<?>
     * - Cast element-by-element (NOT List cast)
     */
    public DDMTemplate getTemplateByNameAndGroupId(long groupId, String templateName) {
        if (templateName == null || templateName.trim().isEmpty()) {
            _log.warn("Template name is blank");
            return null;
        }

        try {
            DynamicQuery dq = ddmTemplateLocalService.dynamicQuery();

            dq.add(PropertyFactoryUtil.forName("groupId").eq(groupId));
            dq.add(PropertyFactoryUtil.forName("name").like("%" + templateName + "%"));

            List<?> results = ddmTemplateLocalService.dynamicQuery(dq);

            for (Object obj : results) {
                if (obj instanceof DDMTemplate) {
                    DDMTemplate template = (DDMTemplate) obj;

                    _log.info(
                        "Found template: " + template.getNameCurrentValue() +
                        " (templateKey=" + template.getTemplateKey() + ")"
                    );
                    return template;
                }
            }

            _log.warn(
                "No DDMTemplate found for name='" + templateName +
                "' in groupId=" + groupId
            );
            return null;

        } catch (Exception e) {
            _log.error("Error while searching for DDMTemplate", e);
            return null;
        }
    }

    /**
     * DTO carrying structure + template metadata.
     */
    public static class TemplateStructureInfo implements Serializable {

        private static final long serialVersionUID = 1L;

        private final long structureId;
        private final String structureKey;
        private final String structureName;
        private final long templateId;
        private final String templateKey;
        private final String templateName;

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

        public long getStructureId() {
            return structureId;
        }

        public String getStructureKey() {
            return structureKey;
        }

        public String getStructureName() {
            return structureName;
        }

        public long getTemplateId() {
            return templateId;
        }

        public String getTemplateKey() {
            return templateKey;
        }

        public String getTemplateName() {
            return templateName;
        }

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
