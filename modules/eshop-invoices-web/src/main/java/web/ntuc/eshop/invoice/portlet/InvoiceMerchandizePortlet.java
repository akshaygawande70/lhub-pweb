package web.ntuc.eshop.invoice.portlet;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.RoleServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.RoleUtil;
import web.ntuc.eshop.invoice.constants.InvoicePortletKeys;

@Component(immediate = true, property = { 
		"com.liferay.portlet.display-category=category.eshop",
		"com.liferay.portlet.header-portlet-css=/css/main.css", 
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.display-name=Invoice Merchandize Order",
		"javax.portlet.init-param.view-template=/merchandize_result/view.jsp",
		"javax.portlet.name=" + InvoicePortletKeys.INVOICE_MERCHANDIZE_PORTLET,
		"javax.portlet.resource-bundle=content.Language", 
		"javax.portlet.version=3.0",
		"javax.portlet.init-param.check-auth-token=false",
		"com.liferay.portlet.action-url-redirect=true",
		"javax.portlet.security-role-ref=power-user,user" }, service = Portlet.class)

public class InvoiceMerchandizePortlet extends MVCPortlet {
	private static Log log = LogFactoryUtil.getLog(InvoiceMerchandizePortlet.class);
	
	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		log.info("Invoice Merchandize Result History - Start");
		try {
			
			String authToken = CSRFValidationUtil.authToken(renderRequest);
			long userId = 0;
			long accountId = 0;
			String companyName = "";
			int accType = 0;

			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
			User user = themeDisplay.getUser();
			userId = user.getUserId();

			CommerceAccount account = null;

			List<Role> roleList = new ArrayList<Role>();
			for (Long roleId : user.getRoleIds()) {
				Role newRole = RoleServiceUtil.getRole(roleId);
				roleList.add(newRole);
			}

			account = CommerceAccountLocalServiceUtil.getPersonalCommerceAccount(user.getUserId());

			if (RoleUtil.matchByFullRoleName(roleList, "Eshop_Individual_Role")) {
				accType = 1;
			} else if (RoleUtil.matchByFullRoleName(roleList, "Eshop_Corporate_Role")) {
				accType = 2;
				companyName = (String) account.getExpandoBridge().getAttribute("Company Name");
			}

			accountId = account.getCommerceAccountId();

			renderRequest.setAttribute("accountId", accountId);
			renderRequest.setAttribute("accType", accType);
			renderRequest.setAttribute("companyName", companyName);
			renderRequest.setAttribute("authToken", authToken);

		} catch (Exception e) {
			log.error("Failed when render result, error:" + e.getMessage());
		}
		log.info("Invoice Merchandize Result History - End");
		super.render(renderRequest, renderResponse);
	}
	
}
