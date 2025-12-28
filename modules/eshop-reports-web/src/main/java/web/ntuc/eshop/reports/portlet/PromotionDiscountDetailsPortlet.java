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
                "javax.portlet.display-name=Promotion Discount Details Report",
                "javax.portlet.init-param.template-path=/",
                "javax.portlet.init-param.view-template=/promotion-discount-details-report/view.jsp",
                "javax.portlet.name=" + ReportPortletKeys.PROMOTION_AND_DISCOUNT_DETAILS_REPORT_PORTLET,
                "javax.portlet.resource-bundle=content.Language",
                "javax.portlet.version=3.0",
                "javax.portlet.security-role-ref=power-user,user"
        },
        service = Portlet.class
)

public class PromotionDiscountDetailsPortlet extends MVCPortlet {
	private static Log log = LogFactoryUtil.getLog(PromotionDiscountDetailsPortlet.class);  
}
