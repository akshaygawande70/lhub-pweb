package web.ntuc.eshop.invoice.portlet;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.RoleServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.AESEncryptUtil;
import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.HttpApiUtil;
import api.ntuc.common.util.RoleUtil;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.eshop.invoice.constants.InvoicePortletKeys;
import web.ntuc.eshop.invoice.dto.InvoiceDto;
import web.ntuc.eshop.invoice.util.InvoiceUtil;

/**
 * @author muhamadpangestu
 */
@Component(immediate = true, property = { "com.liferay.portlet.display-category=category.eshop",
		"com.liferay.portlet.header-portlet-css=/css/main.css", "com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=Invoice Course Order", "javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + InvoicePortletKeys.INVOICE_COURSE_PORTLET,
		"javax.portlet.resource-bundle=content.Language", "javax.portlet.version=3.0",
		"javax.portlet.security-role-ref=power-user,user" }, service = Portlet.class)
public class InvoicePortlet extends MVCPortlet {

	private Log log = LogFactoryUtil.getLog(InvoicePortlet.class);
	private InvoiceUtil invoiceUtil = new InvoiceUtil();

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		log.info("Invoice Course Result History - Start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

			User user = themeDisplay.getUser();

			String authToken = CSRFValidationUtil.authToken(renderRequest);
			String nric = "";
			String companyName = "";
			String companyCode = "";
			String uenNumber = "";
			int accType = 0;
			String birthDate = "";

			CommerceAccount account = null;

			List<Role> roleList = new ArrayList<>();
			for (Long roleId : user.getRoleIds()) {
				Role newRole = RoleServiceUtil.getRole(roleId);
				roleList.add(newRole);
			}

			account = CommerceAccountLocalServiceUtil.getPersonalCommerceAccount(user.getUserId());

			if (RoleUtil.matchByFullRoleName(roleList, "Eshop_Individual_Role")) {
				accType = 1;
				nric = (String) account.getExpandoBridge().getAttribute("NRIC");
				birthDate = invoiceUtil.parseDate("yyyy-MM-dd", user.getBirthday(), log);
			} else if (RoleUtil.matchByFullRoleName(roleList, "Eshop_Corporate_Role")) {
				accType = 2;
				uenNumber = (String) account.getExpandoBridge().getAttribute("UEN Number");
				companyCode = (String) account.getExpandoBridge().getAttribute("Company Code");
				companyName = (String) account.getExpandoBridge().getAttribute("Company Name");
			}

			renderRequest.setAttribute("accType", accType);
			renderRequest.setAttribute("nric", nric);
			renderRequest.setAttribute("birthDate", birthDate);
			renderRequest.setAttribute("uenNumber", uenNumber);
			renderRequest.setAttribute("companyCode", companyCode);
			renderRequest.setAttribute("companyName", companyName);
			renderRequest.setAttribute("authToken", authToken);

		} catch (Exception e) {
			log.error("Failed when render result, error:" + e.getMessage());
		}
		log.info("Invoice Course Result History - End");
		super.render(renderRequest, renderResponse);
	}

	public static String capitalizeString(String text) {
		return Character.toUpperCase(text.charAt(0)) + text.substring(1);
	}
}