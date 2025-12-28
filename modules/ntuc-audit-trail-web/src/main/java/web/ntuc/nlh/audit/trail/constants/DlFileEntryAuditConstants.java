package web.ntuc.nlh.audit.trail.constants;

import com.liferay.document.library.kernel.model.DLFileEntry;

public class DlFileEntryAuditConstants {
	public static final String FILE_NAME_ATTR = "fileName";
	public static final String NAME_ATTR = "name";
	public static final String FILE_DESC_ATTR= "description";
	public static final String ID_ATTR = "id";
	public static final String GROUP_ID_ATTR = "groupId";
	public static final String COMPANY_ID_ATTR = "companyId";
	public static final String IN_TRASH_ATTR = "inTrash";
	public static final String AUDIT_CLASSNAME = DLFileEntry.class.getName();

	private DlFileEntryAuditConstants(){}
}
