package web.ntuc.eshop.reports.portlet;

import com.liferay.commerce.order.status.CommerceOrderStatus;
import com.liferay.commerce.order.status.CommerceOrderStatusRegistry;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.util.CSRFValidationUtil;
import web.ntuc.eshop.reports.constants.ReportPortletKeys;
import web.ntuc.eshop.reports.util.ReportUtil;

/**
 * @author muhamadpangestu
 */
@Component(immediate = true, property = { "com.liferay.portlet.display-category=category.eshop",
		"com.liferay.portlet.header-portlet-css=/css/main.css", "com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=Report", "javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + ReportPortletKeys.SALES_AND_SUMMARY_REPORT_PORTLET,
		"javax.portlet.resource-bundle=content.Language", "javax.portlet.version=3.0",
		"javax.portlet.security-role-ref=power-user,user" }, service = Portlet.class)
public class ReportPortlet extends MVCPortlet {

	private Log log = LogFactoryUtil.getLog(ReportPortlet.class);

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		log.info("Sales and Summary Report - Start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
			Map<Integer, String> statuses = ReportUtil.getStatusList(themeDisplay,_commerceOrderStatusRegistry);
//			statuses.put(-1, "Choose Order Status");
//			for(CommerceOrderStatus status : _commerceOrderStatusRegistry.getCommerceOrderStatuses()) {
//				if(status.getLabel(themeDisplay.getLocale()).equals("Pending") 
//						|| status.getLabel(themeDisplay.getLocale()).equals("Processing")
//						|| status.getLabel(themeDisplay.getLocale()).equals("Completed")
//						|| status.getLabel(themeDisplay.getLocale()).equals("In Progress")
//						|| status.getLabel(themeDisplay.getLocale()).equals("On Hold")
//						|| status.getLabel(themeDisplay.getLocale()).equals("Cancelled"))
//				statuses.put(status.getKey(), status.getLabel(themeDisplay.getLocale()));
//			}
			
			renderRequest.setAttribute("statuses", statuses);
			String authToken = CSRFValidationUtil.authToken(renderRequest);
			renderRequest.setAttribute("authToken", authToken);

		} catch (Exception e) {
			log.error("Failed when render result, error:" + e.getMessage());
		}
		log.info("Sales and Summary Report - End");
		super.render(renderRequest, renderResponse);
	}
	
	@Reference
	private CommerceOrderStatusRegistry _commerceOrderStatusRegistry;
}