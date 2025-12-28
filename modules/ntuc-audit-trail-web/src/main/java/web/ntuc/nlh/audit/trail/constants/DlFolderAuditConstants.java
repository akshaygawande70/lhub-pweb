package web.ntuc.nlh.audit.trail.constants;

import com.liferay.document.library.kernel.model.DLFolder;

public class DlFolderAuditConstants {
	public static final String FOLDER_NAME_ATTR = "name";
	public static final String FOLDER_DESC_ATTR= "description";
	public static final String ID_ATTR = "id";
	public static final String GROUP_ID_ATTR = "groupId";
	public static final String COMPANY_ID_ATTR = "companyId";
	public static final String IN_TRASH_ATTR = "inTrash";
	public static final String AUDIT_CLASSNAME = DLFolder.class.getName();
	
	private DlFolderAuditConstants() {}
}
