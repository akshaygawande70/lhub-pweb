package api.ntuc.common.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;

public class PermissionUtil {

	/**
	 * Check Methods
	 * 
	 * @param permissionChecker, the permission checker
	 * @param groupId,           the group Id
	 * @param actionId,          the action Id
	 * @throws PrincipalException
	 * @throws PortalException
	 */
	public static void check(PermissionChecker permissionChecker, long groupId, String actionId, String resourcesName)
			throws PrincipalException, PortalException {
		if (!contains(permissionChecker, groupId, actionId, resourcesName))
			throw new PrincipalException();
	}

	/**
	 * Contains Methods
	 * 
	 * @param permissionChecker
	 * @param groupId
	 * @param actionId
	 * 
	 * @return
	 * @throws PortalException
	 */
	public static boolean contains(PermissionChecker permissionChecker, long groupId, String actionId,
			String resourcesName) {
		boolean result = false;
		try {
			result = permissionChecker.hasPermission(groupId, resourcesName, groupId, actionId);
		} catch (Exception e) {
		}
		return result;
	}
}
