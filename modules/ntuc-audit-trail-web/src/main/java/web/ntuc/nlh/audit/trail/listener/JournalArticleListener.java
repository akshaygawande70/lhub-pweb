package web.ntuc.nlh.audit.trail.listener;

import static web.ntuc.nlh.audit.trail.constants.JournalArticleAuditConstants.ARTICLE_CONTENT_ATTR;
import static web.ntuc.nlh.audit.trail.constants.JournalArticleAuditConstants.ARTICLE_TITLE_ATTR;
import static web.ntuc.nlh.audit.trail.constants.JournalArticleAuditConstants.ARTICLE_VERSION_ATTR;
import static web.ntuc.nlh.audit.trail.constants.JournalArticleAuditConstants.AUDIT_CLASSNAME;
import static web.ntuc.nlh.audit.trail.constants.JournalArticleAuditConstants.COMPANY_ID_ATTR;
import static web.ntuc.nlh.audit.trail.constants.JournalArticleAuditConstants.GROUP_ID_ATTR;
import static web.ntuc.nlh.audit.trail.constants.JournalArticleAuditConstants.ID_ATTR;
import static web.ntuc.nlh.audit.trail.constants.JournalArticleAuditConstants.IN_TRASH_ATTR;

import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalService;
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
public class JournalArticleListener extends BaseModelListener<JournalArticle> {
	private static Log log = LogFactoryUtil.getLog(JournalArticleListener.class);

	@Override
	public void onBeforeCreate(JournalArticle model) throws ModelListenerException {
		this.auditOnCreateOrRemove(EventTypes.ADD, model);
		super.onBeforeCreate(model);
	}

	@Override
	public void onBeforeRemove(JournalArticle model) throws ModelListenerException {
		this.auditOnCreateOrRemove(EventTypes.DELETE, model);
		super.onBeforeRemove(model);
	}

	@Override
	public void onBeforeUpdate(JournalArticle newJournalArticle) throws ModelListenerException {
		log.info("onBeforeUpdate JournalArticle start");
		try {
			JournalArticle currentJournalArticle = journalArticleLocalService
					.getJournalArticle(newJournalArticle.getId());
			List<Attribute> attributes = getModifiedAttributes(newJournalArticle, currentJournalArticle);
			log.info(attributes);
			if (!attributes.isEmpty()) {
				AuditMessage auditMessage = AuditMessageBuilder.buildAuditMessage(EventTypes.UPDATE, AUDIT_CLASSNAME,
						newJournalArticle.getId(), attributes);

				auditRouter.route(auditMessage);
			}
		} catch (Exception e) {
			log.error("Failed to update journal article, error : " + e.getMessage());
		}
		log.info("onBeforeUpdate JournalArticle end");
		super.onBeforeUpdate(newJournalArticle);
	}

	protected List<Attribute> getModifiedAttributes(JournalArticle newJournalArticle,
			JournalArticle currentJournalArticle) {
		log.info("new = " + newJournalArticle.getId() + " current = " + currentJournalArticle.getId());
		AttributesBuilder attributesBuilder = new AttributesBuilder(newJournalArticle, currentJournalArticle);
		attributesBuilder.add(ARTICLE_TITLE_ATTR);
		attributesBuilder.add(ARTICLE_CONTENT_ATTR);
		attributesBuilder.add(ARTICLE_VERSION_ATTR);
		attributesBuilder.add(IN_TRASH_ATTR);

		return attributesBuilder.getAttributes();
	}

	protected void auditOnCreateOrRemove(String eventType, JournalArticle journalArticle)
			throws ModelListenerException {

		try {
			AuditMessage auditMessage = AuditMessageBuilder.buildAuditMessage(eventType, AUDIT_CLASSNAME,
					journalArticle.getId(), null);

			JSONObject additionalInfoJSONObject = auditMessage.getAdditionalInfo();

			additionalInfoJSONObject.put(ARTICLE_TITLE_ATTR, journalArticle.getTitle())
					.put(ARTICLE_CONTENT_ATTR, journalArticle.getContent())
					.put(ARTICLE_VERSION_ATTR, journalArticle.getVersion()).put(ID_ATTR, journalArticle.getId())
					.put(GROUP_ID_ATTR, journalArticle.getGroupId()).put(COMPANY_ID_ATTR, journalArticle.getCompanyId())
					.put(IN_TRASH_ATTR, journalArticle.isInTrash());

			auditRouter.route(auditMessage);
		} catch (Exception e) {
			throw new ModelListenerException(e);
		}
	}

	@Reference
	private AuditRouter auditRouter;

	@Reference
	private JournalArticleLocalService journalArticleLocalService;
}
