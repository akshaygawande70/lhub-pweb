package web.ntuc.nlh.audit.trail.listener;

import static web.ntuc.nlh.audit.trail.constants.DlFileEntryAuditConstants.AUDIT_CLASSNAME;
import static web.ntuc.nlh.audit.trail.constants.DlFileEntryAuditConstants.COMPANY_ID_ATTR;
import static web.ntuc.nlh.audit.trail.constants.DlFileEntryAuditConstants.FILE_DESC_ATTR;
import static web.ntuc.nlh.audit.trail.constants.DlFileEntryAuditConstants.FILE_NAME_ATTR;
import static web.ntuc.nlh.audit.trail.constants.DlFileEntryAuditConstants.NAME_ATTR;
import static web.ntuc.nlh.audit.trail.constants.DlFileEntryAuditConstants.GROUP_ID_ATTR;
import static web.ntuc.nlh.audit.trail.constants.DlFileEntryAuditConstants.ID_ATTR;
import static web.ntuc.nlh.audit.trail.constants.DlFileEntryAuditConstants.IN_TRASH_ATTR;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.service.DLFileEntryLocalService;
import com.liferay.portal.kernel.audit.AuditMessage;
import com.liferay.portal.kernel.audit.AuditRouter;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.security.audit.event.generators.constants.EventTypes;
import com.liferay.portal.security.audit.event.generators.util.Attribute;
import com.liferay.portal.security.audit.event.generators.util.AttributesBuilder;
import com.liferay.portal.security.audit.event.generators.util.AuditMessageBuilder;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, service = ModelListener.class)
public class DlFileEntryListener extends BaseModelListener<DLFileEntry> {
	private static Log log = LogFactoryUtil.getLog(DlFileEntryListener.class);

	@Override
	public void onBeforeCreate(DLFileEntry model) throws ModelListenerException {
		this.auditOnCreateOrRemove(EventTypes.ADD, model);
		super.onBeforeCreate(model);
	}

	@Override
	public void onBeforeRemove(DLFileEntry model) throws ModelListenerException {
		this.auditOnCreateOrRemove(EventTypes.DELETE, model);
		super.onBeforeRemove(model);
	}

	@Override
	public void onBeforeUpdate(DLFileEntry newDlFileEntry) throws ModelListenerException {
		try {
			DLFileEntry currentDlFileEntry = dlFileEntryLocalService.getDLFileEntry(newDlFileEntry.getFileEntryId());
			List<Attribute> attributes = getModifiedAttributes(newDlFileEntry, currentDlFileEntry);
			if (!attributes.isEmpty()) {
				AuditMessage auditMessage = AuditMessageBuilder.buildAuditMessage(EventTypes.UPDATE, AUDIT_CLASSNAME,
						newDlFileEntry.getFileEntryId(), attributes);

				auditRouter.route(auditMessage);
			}
		} catch (Exception e) {
			log.error("Failed to update dl file entry, error : " + e.getMessage());
		}
		super.onBeforeUpdate(newDlFileEntry);
	}

	protected List<Attribute> getModifiedAttributes(DLFileEntry newDlFileEntry, DLFileEntry currentDlFileEntry) {

		AttributesBuilder attributesBuilder = new AttributesBuilder(newDlFileEntry, currentDlFileEntry);
		attributesBuilder.add(FILE_NAME_ATTR);
		attributesBuilder.add(NAME_ATTR);
		attributesBuilder.add(FILE_DESC_ATTR);
		attributesBuilder.add(IN_TRASH_ATTR);

		return attributesBuilder.getAttributes();
	}

	protected void auditOnCreateOrRemove(String eventType, DLFileEntry dlFileEntry) throws ModelListenerException {

		try {
			AuditMessage auditMessage = AuditMessageBuilder.buildAuditMessage(eventType, AUDIT_CLASSNAME,
					dlFileEntry.getFileEntryId(), null);

			JSONObject additionalInfoJSONObject = auditMessage.getAdditionalInfo();

			additionalInfoJSONObject.put(FILE_NAME_ATTR, dlFileEntry.getFileName())
					.put(NAME_ATTR, dlFileEntry.getName()).put(FILE_DESC_ATTR, dlFileEntry.getDescription())
					.put(ID_ATTR, dlFileEntry.getFileEntryId()).put(GROUP_ID_ATTR, dlFileEntry.getGroupId())
					.put(COMPANY_ID_ATTR, dlFileEntry.getCompanyId()).put(IN_TRASH_ATTR, dlFileEntry.isInTrash());

			auditRouter.route(auditMessage);
		} catch (Exception e) {
			throw new ModelListenerException(e);
		}
	}

	@Reference
	private AuditRouter auditRouter;

	@Reference
	private DLFileEntryLocalService dlFileEntryLocalService;
}
