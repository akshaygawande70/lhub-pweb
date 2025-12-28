package web.ntuc.nlh.audit.trail.listener;

import static web.ntuc.nlh.audit.trail.constants.DlFolderAuditConstants.AUDIT_CLASSNAME;
import static web.ntuc.nlh.audit.trail.constants.DlFolderAuditConstants.COMPANY_ID_ATTR;
import static web.ntuc.nlh.audit.trail.constants.DlFolderAuditConstants.FOLDER_DESC_ATTR;
import static web.ntuc.nlh.audit.trail.constants.DlFolderAuditConstants.FOLDER_NAME_ATTR;
import static web.ntuc.nlh.audit.trail.constants.DlFolderAuditConstants.GROUP_ID_ATTR;
import static web.ntuc.nlh.audit.trail.constants.DlFolderAuditConstants.ID_ATTR;
import static web.ntuc.nlh.audit.trail.constants.DlFolderAuditConstants.IN_TRASH_ATTR;

import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.service.DLFolderLocalService;
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
public class DlFolderListener extends BaseModelListener<DLFolder> {
	private static Log log = LogFactoryUtil.getLog(DlFolderListener.class);

	@Override
	public void onBeforeCreate(DLFolder model) throws ModelListenerException {
		this.auditOnCreateOrRemove(EventTypes.ADD, model);
		super.onBeforeCreate(model);
	}

	@Override
	public void onBeforeRemove(DLFolder model) throws ModelListenerException {
		this.auditOnCreateOrRemove(EventTypes.DELETE, model);
		super.onBeforeRemove(model);
	}

	@Override
	public void onBeforeUpdate(DLFolder newDlFolder) throws ModelListenerException {
		try {
			DLFolder currentDlFolder = dlFolderLocalService.getDLFolder(newDlFolder.getFolderId());
			List<Attribute> attributes = getModifiedAttributes(newDlFolder, currentDlFolder);
			if (!attributes.isEmpty()) {
				AuditMessage auditMessage = AuditMessageBuilder.buildAuditMessage(EventTypes.UPDATE, AUDIT_CLASSNAME,
						newDlFolder.getFolderId(), attributes);

				auditRouter.route(auditMessage);
			}
		} catch (Exception e) {
			log.error("Failed to update DL folder, error : " + e.getMessage());
		}
		super.onBeforeUpdate(newDlFolder);
	}

	protected List<Attribute> getModifiedAttributes(DLFolder newFolder, DLFolder currentFolder) {
		AttributesBuilder attributesBuilder = new AttributesBuilder(newFolder, currentFolder);
		attributesBuilder.add(FOLDER_NAME_ATTR);
		attributesBuilder.add(FOLDER_DESC_ATTR);
		attributesBuilder.add(IN_TRASH_ATTR);

		return attributesBuilder.getAttributes();
	}

	protected void auditOnCreateOrRemove(String eventType, DLFolder dlFolder) throws ModelListenerException {

		try {
			AuditMessage auditMessage = AuditMessageBuilder.buildAuditMessage(eventType, AUDIT_CLASSNAME,
					dlFolder.getFolderId(), null);

			JSONObject additionalInfoJSONObject = auditMessage.getAdditionalInfo();

			additionalInfoJSONObject.put(FOLDER_NAME_ATTR, dlFolder.getName())
					.put(FOLDER_DESC_ATTR, dlFolder.getDescription()).put(ID_ATTR, dlFolder.getFolderId())
					.put(GROUP_ID_ATTR, dlFolder.getGroupId()).put(COMPANY_ID_ATTR, dlFolder.getCompanyId())
					.put(IN_TRASH_ATTR, dlFolder.isInTrash());

			auditRouter.route(auditMessage);
		} catch (Exception e) {
			throw new ModelListenerException(e);
		}
	}

	@Reference
	private AuditRouter auditRouter;

	@Reference
	private DLFolderLocalService dlFolderLocalService;
}
