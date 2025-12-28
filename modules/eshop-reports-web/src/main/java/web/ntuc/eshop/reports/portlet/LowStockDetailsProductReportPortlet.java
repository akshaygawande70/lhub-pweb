package web.ntuc.eshop.reports.portlet;

import com.liferay.commerce.order.status.CommerceOrderStatusRegistry;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import web.ntuc.eshop.reports.constants.ReportPortletKeys;
import web.ntuc.eshop.reports.dto.ListCategoryDto;
import web.ntuc.eshop.reports.util.ReportUtil;

@Component(
		immediate = true, 
		property = { 
			"com.liferay.portlet.display-category=category.eshop",
			"com.liferay.portlet.header-portlet-css=/css/main.css", 
			"com.liferay.portlet.instanceable=true",
			"javax.portlet.display-name=Low Stock Details Product Report", 
			"javax.portlet.init-param.template-path=/",
			"javax.portlet.init-param.view-template=/low-stock-product-details-report/view.jsp",
			"javax.portlet.name=" + ReportPortletKeys.LOW_STOCK_DETAILS_PRODUCT_REPORT_PORTLET,
			"javax.portlet.resource-bundle=content.Language", 
			"javax.portlet.version=3.0",
			"javax.portlet.security-role-ref=power-user,user" 
		}, 
		service = Portlet.class
)
public class LowStockDetailsProductReportPortlet extends MVCPortlet{
private static Log log = LogFactoryUtil.getLog(LowStockDetailsProductReportPortlet.class);
	
	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
			List<ListCategoryDto> categoryList = ReportUtil.getCategoryLists(themeDisplay);
			renderRequest.setAttribute("categoryList", categoryList);
			log.info(categoryList);
			
		}catch (Exception e) {
			log.error("Failed when render Product inventory details, error : "+e.getMessage());
		}
		super.render(renderRequest, renderResponse);
	}
}