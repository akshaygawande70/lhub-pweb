package web.ntuc.login.portlet;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

import web.ntuc.login.constants.LoginPortletKeys;

@Component(property = { "javax.portlet.name=" + LoginPortletKeys.FAST_LOGIN,
		"javax.portlet.name=" + LoginPortletKeys.LOGIN,
		"mvc.command.name=/forgot/thank_you" }, service = MVCRenderCommand.class)
public class ForgotThankYouRenderCommand implements MVCRenderCommand {
	Log log = LogFactoryUtil.getLog(ForgotThankYouRenderCommand.class);

	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
//		log.info("forgot thank you");
		try {
			HttpServletRequest httpServletRequest = PortalUtil.getHttpServletRequest(renderRequest);
			HttpServletResponse httpServletResponse = PortalUtil.getHttpServletResponse(renderResponse);
//			String redirect = StringPool.BLANK;
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
			// log.info(themeDisplay.getURLCurrent());
//			String currentUrl = themeDisplay.getURLCurrent();
			
			PortletSession portletSession = renderRequest.getPortletSession();

			String login = (String) portletSession.getAttribute("forgot_email");
//			log.info("login = "+login);
			if(Validator.isNotNull(login)) {
				renderRequest.setAttribute("login", login);
				portletSession.removeAttribute("forgot_email");
			} else {
				httpServletResponse.sendRedirect("/home");
			}
			
//			log.info("currenturl = "+currentUrl);
		
//			String email = ParamUtil.getString(httpServletRequest, "email");
//			String escapedMail = email.replaceAll("[^a-zA-Z0-9\\@]", "_");
//			log.info("email = "+email);
//			log.info("escaped email = "+escapedMail);
//			String emailArgs = currentUrl.substring(currentUrl.lastIndexOf("=") + 1);
//			String finalEscUrl = currentUrl.replace(emailArgs, escapedMail);
//			log.info("final esc url = "+finalEscUrl);
			
			/*String extractedMail = StringPool.BLANK;
			String extractedParam = StringPool.BLANK;
			Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(email);
			 while (m.find()) 
		      {
		          extractedMail = m.group();
		      }
			 log.info("extractedMail = "+extractedMail);
			if(!Validator.isBlank(extractedMail)) {
				String[] splitEmail = email.split(extractedMail);
				if(splitEmail.length > 1) {
					for(int i=1; i<splitEmail.length; i++) {
						String esc = HtmlUtil.escapeXPath(splitEmail[i]);
						extractedParam += esc;
					}
					String finalEmail = extractedMail+extractedParam;
					log.info("final email = "+finalEmail);
//					String emailArgs = currentUrl.substring(currentUrl.lastIndexOf("=") + 1);
//					log.info("email args = "+emailArgs);
					String finalEscUrl = currentUrl.replace(email, finalEmail);
					log.info("final Url = "+finalEscUrl);
					String fullUrl = themeDisplay.getPortalURL()+"/home";
//					log.info(fullUrl);
//					httpServletResponse.sendRedirect(fullUrl);
				}
			} */
			
//			String email1 = HtmlUtil.escapeXPathAttribute(email);
//			String email2 = HtmlUtil.escapeXPath(email);
//			String email3 = HtmlUtil.escapeJS(email);
//			String email4 = HtmlUtil.escapeJSLink(email);
//			String email5 = HtmlUtil.escapeURL(email);
//			String escapedEmail = HtmlUtil.escapeAttribute(email);
//			log.info("email1 = "+email1);
//			log.info("email2 = "+email2);
//			log.info("email3 = "+email3);
//			log.info("email4 = "+email4);
//			log.info("email5 = "+email5);
			
//			httpServletRequest.setAttribute("escapedEmail", escapedEmail);
//			renderRequest.setAttribute("cok2", escapedEmail);
			
//			String email7 = HtmlUtil.escape(email);
//			log.info("email = "+email);
//			log.info("email1 = "+email1);
//			log.info("email2 = "+email2);
//			log.info("email3 = "+email3);
//			log.info("email4 = "+email4);
//			log.info("email5 = "+email5);
//			log.info("email6 = "+email6);
//			log.info("email7 = "+email7);
			if (themeDisplay.isSignedIn()) {
//				String finalUrl = themeDisplay.getPortalURL() + "/home";
				httpServletResponse.sendRedirect("/home");
			} 
//			else {
//				httpServletResponse.sendRedirect("/home");
//			}
//			log.info("full url = "+themeDisplay.getPortalURL()+currentUrl);
//			String escCurrentUrl = HtmlUtil.escapeXPath(currentUrl);
			
		} catch (IOException e) {
			log.error(e.getMessage());

		}
		return "/thank_you.jsp";
	}
}
