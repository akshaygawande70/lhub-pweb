package web.ntuc.eshop.reports.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import org.osgi.service.component.annotations.Component;
import web.ntuc.eshop.reports.constants.ReportPortletKeys;

import javax.portlet.Portlet;

@Component(
        immediate = true,
        property = {
                "com.liferay.portlet.display-category=category.eshop",
                "com.liferay.portlet.header-portlet-css=/css/main.css",
                "com.liferay.portlet.instanceable=true",
                "javax.portlet.display-name=Special Product Report",
                "javax.portlet.init-param.template-path=/",
                "javax.portlet.init-param.view-template=/special-product-report/view.jsp",
                "javax.portlet.name=" + ReportPortletKeys.SPECIAL_PRODUCT_REPORT_PORTLET,
                "javax.portlet.resource-bundle=content.Language",
                "javax.portlet.version=3.0",
                "javax.portlet.security-role-ref=power-user,user"
        },
        service = Portlet.class
)
public class SpecialProductReportPortlet extends MVCPortlet {
}
