package web.ntuc.eshop.reports.portlet;

import com.liferay.commerce.order.status.CommerceOrderStatus;
import com.liferay.commerce.order.status.CommerceOrderStatusRegistry;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import web.ntuc.eshop.reports.constants.ReportPortletKeys;
import web.ntuc.eshop.reports.util.ReportUtil;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

@Component(
        immediate = true,
        property = {
                "com.liferay.portlet.display-category=category.eshop",
                "com.liferay.portlet.header-portlet-css=/css/main.css",
                "com.liferay.portlet.instanceable=true",
                "javax.portlet.display-name=Order Detail Report",
                "javax.portlet.init-param.template-path=/",
                "javax.portlet.init-param.view-template=/order-detail-report/view.jsp",
                "javax.portlet.name=" + ReportPortletKeys.ORDER_DETAIL_REPORT_PORTLET,
                "javax.portlet.resource-bundle=content.Language",
                "javax.portlet.version=3.0",
                "javax.portlet.security-role-ref=power-user,user"
        },
        service = Portlet.class
)
public class OrderDetailReportPortlet extends MVCPortlet {
	private static Log log = LogFactoryUtil.getLog(OrderDetailReportPortlet.class);
	
	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
			Map<Integer, String> statuses = ReportUtil.getStatusList(themeDisplay, _commerceOrderStatusRegistry);
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
		}catch (Exception e) {
			log.error("Failed when render Order detail report, error : "+e.getMessage());
		}
		super.render(renderRequest, renderResponse);
	}
	
	 @Reference
		private CommerceOrderStatusRegistry _commerceOrderStatusRegistry;
}
