package web.ntuc.nlh.audit.trail.constants;

import com.liferay.journal.model.JournalArticle;

public class JournalArticleAuditConstants {
	public static final String ARTICLE_TITLE_ATTR = "title";
	public static final String ARTICLE_CONTENT_ATTR= "content";
	public static final String ID_ATTR = "id";
	public static final String GROUP_ID_ATTR = "groupId";
	public static final String COMPANY_ID_ATTR = "companyId";
	public static final String ARTICLE_VERSION_ATTR = "version";
	public static final String IN_TRASH_ATTR = "inTrash";
	public static final String AUDIT_CLASSNAME = JournalArticle.class.getName();

	private JournalArticleAuditConstants() {}
}
