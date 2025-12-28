package web.ntuc.eshop.otp.resource;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.PortletCommandUtil;
import svc.ntuc.nlh.otp.model.Otp;
import svc.ntuc.nlh.otp.service.OtpLocalServiceUtil;
import web.ntuc.eshop.otp.constants.EshopOtpWebPortletKeys;
import web.ntuc.eshop.otp.constants.MVCCommandNames;


@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.OTP_VERIFY_RESOURCE,
		"javax.portlet.name=" + EshopOtpWebPortletKeys.OTP}, service = MVCResourceCommand.class)
public class VerifyOtpResource implements MVCResourceCommand{
	private static Log log = LogFactoryUtil.getLog(VerifyOtpResource.class);
	
	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("verify otp resources - start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			PortletCommandUtil.actionAndResourceCommand(resourceRequest,themeDisplay);
			PortletSession portletSession = resourceRequest.getPortletSession();
			long otpId = (long) portletSession.getAttribute("otpId", PortletSession.APPLICATION_SCOPE);
			String token = ParamUtil.getString(resourceRequest,"token");
			int otpCode = Integer.parseInt(token);
			PrintWriter printWriter = resourceResponse.getWriter();
			Otp otp = OtpLocalServiceUtil.getOtp(otpId);
			Date dateNow = new Date();
			 Instant instantNow = dateNow.toInstant();
			 LocalDateTime localDateTimeNow = instantNow.atZone(ZoneId.of("Asia/Singapore")).toLocalDateTime();
			 Instant instantOTPTableDate = otp.getModifiedDate().toInstant();
			 Instant instantOTPTableDatePlusTwoMinutes = instantOTPTableDate.plus(Duration.ofMinutes(2));
			 LocalDateTime localDateTimeOTPTableDatePlusTwoMinutes = instantOTPTableDatePlusTwoMinutes.atZone(ZoneId.of("Asia/Singapore")).toLocalDateTime();
			 log.info(" localDateTimeNow: " + localDateTimeNow);
			 log.info(" localDateTimeOTPTableDatePlusTwoMinutes: " + localDateTimeOTPTableDatePlusTwoMinutes);
			if(otp.getOtpCode() == otpCode) {
				if(localDateTimeOTPTableDatePlusTwoMinutes.isAfter(localDateTimeNow)) {
					log.info("Valid:::OTP is valid");
					otp.setOTPValidatedFlag(true);
					OtpLocalServiceUtil.updateOtp(otp);
					printWriter.write(String.valueOf(Boolean.TRUE));
				}else {
					log.info("Invalid::OTP is Expired");
					printWriter.write(String.valueOf(Boolean.FALSE));
				}
				
			} else {
				printWriter.write(String.valueOf(Boolean.FALSE));
			}
			printWriter.close();
		} catch (Exception e) {
			log.info("Error while verifying OTP : "+e.getMessage());
			return true;
		}
		log.info("verify otp resources - end");
		return false;
	}

}
