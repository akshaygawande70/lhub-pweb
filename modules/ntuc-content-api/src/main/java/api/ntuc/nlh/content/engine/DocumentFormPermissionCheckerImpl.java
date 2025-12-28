package api.ntuc.nlh.content.engine;

import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.theme.ThemeDisplay;

public class DocumentFormPermissionCheckerImpl implements DocumentFormPermissionChecker {
	
	private final ThemeDisplay themeDisplay;

	public DocumentFormPermissionCheckerImpl(ThemeDisplay themeDisplay) {
		this.themeDisplay = themeDisplay;
	}

	public boolean hasPermission() {
		PermissionChecker permissionChecker = this.themeDisplay.getPermissionChecker();
		if (permissionChecker.isCompanyAdmin()) {
			return true;
		}else {
		return false;
		}
	}
}
