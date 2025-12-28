package web.ntuc.eshop.otp.util;

import javax.portlet.PortletSession;

public class SessionUtil {
	
	public static void removeLocalSession(PortletSession portletSession) {
		portletSession.removeAttribute("otpId",PortletSession.APPLICATION_SCOPE);
		portletSession.removeAttribute("userOtpDto",PortletSession.APPLICATION_SCOPE);
	}
}
