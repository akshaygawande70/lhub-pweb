package web.ntuc.eshop.invoice.resource;

import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.constants.CommerceOrderPaymentConstants;
import com.liferay.commerce.constants.CommerceWebKeys;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CProduct;
import com.liferay.commerce.product.service.CPDefinitionLocalServiceUtil;
import com.liferay.commerce.product.service.CProductLocalServiceUtil;
import com.liferay.commerce.product.type.simple.constants.SimpleCPTypeConstants;
import com.liferay.commerce.product.type.virtual.constants.VirtualCPTypeConstants;
import com.liferay.commerce.service.CommerceOrderItemLocalService;
import com.liferay.commerce.service.CommerceOrderItemLocalServiceUtil;
import com.liferay.commerce.service.CommerceOrderLocalServiceUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.CurrencyUtil;
import api.ntuc.common.util.PortletCommandUtil;
import web.ntuc.eshop.invoice.constants.InvoiceConstants;
import web.ntuc.eshop.invoice.constants.InvoicePortletKeys;
import web.ntuc.eshop.invoice.constants.MVCCommandNames;
import web.ntuc.eshop.invoice.util.GenerateInvoiceUtil;
import web.ntuc.eshop.invoice.util.InvoiceUtil;

@Component(immediate = true, property = {
		"mvc.command.name=" + web.ntuc.eshop.invoice.constants.MVCCommandNames.EXAM_ORDER_RESOURCE,
		"javax.portlet.name=" + InvoicePortletKeys.INVOICE_EXAM_PORTLET }, service = MVCResourceCommand.class)
public class InvoiceExamOrderResource extends BaseMVCResourceCommand {

	private Log log = LogFactoryUtil.getLog(InvoiceExamOrderResource.class);

	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
		log.info("Invoice Exam Data Resource - Start");
		ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
		String authToken = CSRFValidationUtil.authToken(resourceRequest);
		int accType = ParamUtil.getInteger(resourceRequest, "accType");
		String companyName = ParamUtil.getString(resourceRequest, "companyName");
		long accountId = ParamUtil.getLong(resourceRequest, "accountId");

		PortletCommandUtil.actionAndResourceCommand(resourceRequest, themeDisplay);
        HttpServletRequest httpRequest = PortalUtil
                .getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
        final int iDisplayStart = ParamUtil.getInteger(httpRequest, InvoiceConstants.I_DISPLAY_START);
        final int iDisplayLength = ParamUtil.getInteger(httpRequest, InvoiceConstants.I_DISPLAY_LENGTH);
        final boolean ascending = ParamUtil.getString(httpRequest, InvoiceConstants.S_SORT_DIR_0)
                .equalsIgnoreCase("asc");
        final String orderByColumn = getOrderByColumn(ParamUtil.getInteger(httpRequest, InvoiceConstants.I_SORT_COL_0));
        
        JSONObject result = InvoiceUtil.getExamOrderHistory(
                iDisplayStart,
                iDisplayLength,
                accountId,
                resourceRequest,
                resourceResponse);
//        log.info(result);
        JSONObject response = GenerateInvoiceUtil.createDataTableResponse(result.getLong(InvoiceConstants.TOTAL_COUNT),
                ParamUtil.getInteger(httpRequest, InvoiceConstants.S_ECHO),
                result.getJSONArray(InvoiceConstants.DATA));
        PrintWriter writer = resourceResponse.getWriter();
        writer.println(response);
        writer.close();
//        log.info(response);
    }



	private String getOrderByColumn(Integer iSortColumnIndex) {
		String orderBy = "";
		if (iSortColumnIndex == 0) {
			orderBy = "transactionId";
		}
		return orderBy;
	}

}