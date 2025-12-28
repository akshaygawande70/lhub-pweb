package web.ntuc.nlh.audit.trail.listener;

import static web.ntuc.nlh.audit.trail.constants.JournalFolderAuditContants.AUDIT_CLASSNAME;
import static web.ntuc.nlh.audit.trail.constants.JournalFolderAuditContants.ID_ATTR;
import static web.ntuc.nlh.audit.trail.constants.JournalFolderAuditContants.GROUP_ID_ATTR;
import static web.ntuc.nlh.audit.trail.constants.JournalFolderAuditContants.COMPANY_ID_ATTR;
import static web.ntuc.nlh.audit.trail.constants.JournalFolderAuditContants.IN_TRASH_ATTR;
import static web.ntuc.nlh.audit.trail.constants.JournalFolderAuditContants.FOLDER_NAME_ATTR;
import static web.ntuc.nlh.audit.trail.constants.JournalFolderAuditContants.FOLDER_DESC_ATTR;

import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.service.JournalFolderLocalService;
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
public class JournalFolderListener extends BaseModelListener<JournalFolder> {
	private static Log log = LogFactoryUtil.getLog(JournalFolderListener.class);

	@Override
	public void onBeforeCreate(JournalFolder model) throws ModelListenerException {
		this.auditOnCreateOrRemove(EventTypes.ADD, model);
		super.onBeforeCreate(model);
	}

	@Override
	public void onBeforeRemove(JournalFolder model) throws ModelListenerException {
		this.auditOnCreateOrRemove(EventTypes.DELETE, model);
		super.onBeforeRemove(model);
	}

	@Override
	public void onBeforeUpdate(JournalFolder newJournalFolder) throws ModelListenerException {
		try {
			JournalFolder currentJournalFolder = journalFolderLocalService
					.getJournalFolder(newJournalFolder.getFolderId());
			List<Attribute> attributes = getModifiedAttributes(newJournalFolder, currentJournalFolder);
			if (!attributes.isEmpty()) {
				AuditMessage auditMessage = AuditMessageBuilder.buildAuditMessage(EventTypes.UPDATE, AUDIT_CLASSNAME,
						newJournalFolder.getFolderId(), attributes);

				auditRouter.route(auditMessage);
			}
		} catch (Exception e) {
			log.error("Failed to update journal folder, error : " + e.getMessage());
		}
		super.onBeforeUpdate(newJournalFolder);
	}

	protected List<Attribute> getModifiedAttributes(JournalFolder newJournalFolder,
			JournalFolder currentJournalFolder) {
		AttributesBuilder attributesBuilder = new AttributesBuilder(newJournalFolder, currentJournalFolder);
		attributesBuilder.add(FOLDER_NAME_ATTR);
		attributesBuilder.add(FOLDER_DESC_ATTR);
		attributesBuilder.add(IN_TRASH_ATTR);

		return attributesBuilder.getAttributes();
	}

	protected void auditOnCreateOrRemove(String eventType, JournalFolder journalFolder) throws ModelListenerException {

		try {
			AuditMessage auditMessage = AuditMessageBuilder.buildAuditMessage(eventType, AUDIT_CLASSNAME,
					journalFolder.getFolderId(), null);

			JSONObject additionalInfoJSONObject = auditMessage.getAdditionalInfo();

			additionalInfoJSONObject.put(FOLDER_NAME_ATTR, journalFolder.getName())
					.put(FOLDER_DESC_ATTR, journalFolder.getDescription()).put(ID_ATTR, journalFolder.getFolderId())
					.put(GROUP_ID_ATTR, journalFolder.getGroupId()).put(COMPANY_ID_ATTR, journalFolder.getCompanyId())
					.put(IN_TRASH_ATTR, journalFolder.isInTrash());

			auditRouter.route(auditMessage);
		} catch (Exception e) {
			throw new ModelListenerException(e);
		}
	}

	@Reference
	private AuditRouter auditRouter;

	@Reference
	private JournalFolderLocalService journalFolderLocalService;
}
