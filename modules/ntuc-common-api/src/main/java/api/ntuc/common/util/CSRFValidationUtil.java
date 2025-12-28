package api.ntuc.common.util;

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.security.auth.AuthTokenUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;

public class CSRFValidationUtil {

	private CSRFValidationUtil() {}
	public static String authToken(PortletRequest request) {

		return AuthTokenUtil.getToken(PortalUtil.getHttpServletRequest(request));
	}

	public static boolean isValidRequest(PortletRequest request, String authToken) {
		boolean isValidRequest = true;

		if (Validator.isNotNull(authToken)) {
			String currentAuthToken = AuthTokenUtil.getToken(PortalUtil.getHttpServletRequest(request));

			if (!currentAuthToken.equals(authToken)) {
				isValidRequest = false;
			}
		} else {
			isValidRequest = false;
		}

		return isValidRequest;
	}

}
