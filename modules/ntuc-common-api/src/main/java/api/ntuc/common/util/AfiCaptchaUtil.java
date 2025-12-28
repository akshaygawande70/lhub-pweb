package api.ntuc.common.util;

import java.util.Enumeration;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import com.liferay.portal.kernel.captcha.CaptchaException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

public class AfiCaptchaUtil {
	
	private AfiCaptchaUtil() {
		// Do nothing
	}

	public static void checkCaptcha(PortletRequest request) throws CaptchaException {
		String enteredCaptchaText = ParamUtil.getString(request, "captchaText");
		PortletSession session = request.getPortletSession();
		String captchaText = getCaptchaValueFromSession(session);
		if (Validator.isNull(captchaText)) {
			throw new CaptchaException("Internal Error! Captcha text not found in session");
		}
		if (captchaText != null && !captchaText.equals(enteredCaptchaText)) {
			throw new CaptchaException("Error captcha not valid.");
		}
		
	}

	
	private static String getCaptchaValueFromSession(PortletSession session) {
		Enumeration<String> atNames = session.getAttributeNames();
		while (atNames.hasMoreElements()) {
			String name = atNames.nextElement();
			if (name.contains(WebKeys.CAPTCHA_TEXT)) {
				return (String) session.getAttribute(name);
			}
		}
		return null;
	}
}
