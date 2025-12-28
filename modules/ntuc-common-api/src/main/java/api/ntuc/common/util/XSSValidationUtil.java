package api.ntuc.common.util;

import java.util.Enumeration;

import javax.portlet.RenderRequest;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.log.Log;
public class XSSValidationUtil {
	private static Log log = LogFactoryUtil.getLog(XSSValidationUtil.class);
	
	private static Whitelist whitelistRelaxed = Whitelist.relaxed().addAttributes(":all", "class", "style", "align")
			.addAttributes("td", "valign");

	public static boolean isValid(String input) {
		if (input == null || input.isEmpty()) {
			return true;
		}
		return Jsoup.isValid(input, Whitelist.basic());
	}

	public static boolean isValidRelaxed(String input) {
		if (input == null || input.isEmpty()) {
			return true;
		}
		return Jsoup.isValid(input, whitelistRelaxed);
	}
	
	/**
	 * Sanitize JS String
	 * : can be used to breakout of key value assignment
	 * ; can be used to break any code line
	 * = can be used to assign variable to variable
	 * ` can be used to break out of the template literal
	 * { and } can be used to evaluate code in template literal
	 * ' can be used to break out of the string literal starting with single quote
	 * " can be used to break out of the string literal starting with double quote
	 * ( and ) can be used to execute code function or eval
	 * / can be used to close the code with multiple / chars
	 * \ can be used to escape-unescape protected characters
	 *
	 * @param oriString The original unsanitized string
	 * @return The sanitized string.
	 **/
	public static String sanitizeJSString(String oriString) {
		if(oriString !=null && oriString!="") {
			oriString = oriString.replaceAll("[:;=`{}'\"()\\/\\\\]+", " ");
		}
		return oriString;
		
	}
	
	public static void removeSanitizeJSString(RenderRequest renderRequest) {
		  Enumeration<String> parameterNames = renderRequest.getParameterNames();
	        while (parameterNames.hasMoreElements()) {
	            String paramName = parameterNames.nextElement();
	            String[] paramValues = renderRequest.getParameterValues(paramName);
	            if (paramValues != null) {
	                for (int i = 0; i < paramValues.length; i++) {
	                	 log.info(paramName+"before removeSanitizeJSString RegisterIndividualRender render  method:param"+paramValues[i]);
	                    if(paramValues[i] !=null && paramValues[i]!="" && !paramValues[i].contains("popup.jsp")) {
	                    	paramValues[i] = sanitizeJSString(paramValues[i]);
	                    }
	                	 
//	                    paramValues[i] = removeAlert(paramValues[i]);
	                    log.info(paramName+"after removeSanitizeJSString  RegisterIndividualRender render  method:param"+paramValues[i]);
	                   // ParamUtil.getParameterValues(portletRequest, param)
	                }
	            }
	        }
		
	}

	public static String cleanRelaxed(String input) {
		if (input == null || input.isEmpty()) {
			return input;
		}
		return Jsoup.clean(input, whitelistRelaxed);
	}

}
