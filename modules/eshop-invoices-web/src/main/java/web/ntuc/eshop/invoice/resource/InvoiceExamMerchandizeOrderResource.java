package web.ntuc.eshop.invoice.resource;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil;
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
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

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
import web.ntuc.eshop.invoice.constants.InvoicePortletKeys;
import web.ntuc.eshop.invoice.constants.MVCCommandNames;
import web.ntuc.eshop.invoice.util.InvoiceUtil;

@Component(immediate = true, property = {
		"mvc.command.name=" + web.ntuc.eshop.invoice.constants.MVCCommandNames.EXAM_MERCHANDIZE_ORDER_RESOURCE,
		"javax.portlet.name=" + InvoicePortletKeys.INVOICE_EXAM_MERCHANDIZE_PORTLET }, service = MVCResourceCommand.class)
public class InvoiceExamMerchandizeOrderResource implements MVCResourceCommand {

	private Log log = LogFactoryUtil.getLog(InvoiceExamMerchandizeOrderResource.class);

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("Invoice Exam Merchandize Data Resource - Start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			String portletName = (String) resourceRequest.getAttribute(WebKeys.PORTLET_ID);

			String authToken = CSRFValidationUtil.authToken(resourceRequest);
			int accType = ParamUtil.getInteger(resourceRequest, "accType");
			String companyName = ParamUtil.getString(resourceRequest, "companyName");
			long accountId = ParamUtil.getLong(resourceRequest, "accountId");
			long accountIdofUser = 0;
			CommerceAccount account = CommerceAccountLocalServiceUtil.getPersonalCommerceAccount(themeDisplay.getUser().getUserId());
			if(account != null) {
				accountIdofUser = account.getCommerceAccountId();
			}
			if(accountIdofUser !=0 && accountId == accountIdofUser) {
				PortletCommandUtil.actionAndResourceCommand(resourceRequest, themeDisplay);

				HttpServletRequest httpRequest = PortalUtil
						.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
				Integer iDisplayStart = ParamUtil.getInteger(httpRequest, "iDisplayStart");
				Integer iDisplayLength = ParamUtil.getInteger(httpRequest, "iDisplayLength");
				if (iDisplayLength == 0) {
					iDisplayLength = 10;
				}

				String sEcho = ParamUtil.getString(httpRequest, "sEcho", "0");

				int start = iDisplayStart;
				int end = start + iDisplayLength;
				
				List<CommerceOrder> plainOrderListSize = CommerceOrderLocalServiceUtil
						.getCommerceOrdersByCommerceAccountId(accountId, -1, -1, null);
				List<CommerceOrder> filteredPlainOrderListSize = plainOrderListSize.stream().filter(x ->x.getOrderStatus() != CommerceOrderConstants.ORDER_STATUS_OPEN).collect(Collectors.toList());
				int orderListSize = filteredPlainOrderListSize.size();
				
				OrderByComparator<CommerceOrder> orderComparator = OrderByComparatorFactoryUtil.create("CommerceOrder",
						"createDate", "desc");
				
				log.info(accountId + " ini accountId");
				List<CommerceOrder> plainOrderList = CommerceOrderLocalServiceUtil
						.getCommerceOrdersByCommerceAccountId(accountId, start, end, orderComparator);
				List<CommerceOrder> orderList = plainOrderList.stream().filter(x ->x.getOrderStatus() != CommerceOrderConstants.ORDER_STATUS_OPEN).collect(Collectors.toList());
				
				if (!Validator.isNull(orderList)) {
					JSONArray parameterJsonArray = JSONFactoryUtil.createJSONArray();
					for(CommerceOrder order : orderList) {
					JSONObject jsonBranch = JSONFactoryUtil.createJSONObject();
					jsonBranch.put("order_id", order.getCommerceOrderId());
					jsonBranch.put("amount", CurrencyUtil.roundUpDollarAmount(String.valueOf(order.getTotal())));
					Date tempDate = order.getOrderDate();

					jsonBranch.put("order_date", InvoiceUtil.parseDate("dd MMMM yyyy HH:mm:ss", tempDate, log));
					jsonBranch.put("order_status", InvoiceUtil.getOrderStatus(order.getOrderStatus()));
					jsonBranch.put("payment_status", CommerceOrderPaymentConstants.getOrderPaymentStatusLabel(order.getPaymentStatus()).toUpperCase());
					PortletURL detailUrl = PortletURLFactoryUtil.create(
								PortalUtil.getHttpServletRequest(resourceRequest), portletName,
								themeDisplay.getLayout().getPlid(), PortletRequest.RENDER_PHASE);
						detailUrl.getRenderParameters().setValue("mvcRenderCommandName",
								MVCCommandNames.EXAM_MERCHANDIZE_ORDER_DETAIL_RENDER);
						detailUrl.getRenderParameters().setValue("companyName", companyName);
						detailUrl.getRenderParameters().setValue("accType", String.valueOf(accType));
						detailUrl.getRenderParameters().setValue("orderId", String.valueOf(order.getCommerceOrderId()));
						detailUrl.getRenderParameters().setValue("authToken", authToken);
						jsonBranch.put("detailUrl", detailUrl);
						parameterJsonArray.put(jsonBranch);
//							log.info(jsonBranch + " ini jsonBranch");
					}

					int allCount = orderListSize;
					JSONObject tableData = JSONFactoryUtil.createJSONObject();
					tableData.put("iTotalRecords", allCount);
					tableData.put("iTotalDisplayRecords", allCount);
					tableData.put("sEcho", Integer.parseInt(sEcho));
					tableData.put("aaData", parameterJsonArray);
					resourceResponse.getWriter().println(tableData.toString());
				} else {
					JSONArray emptyArr = JSONFactoryUtil.createJSONArray();
					JSONObject tableData = JSONFactoryUtil.createJSONObject();
					tableData.put("iTotalRecords", 0);
					tableData.put("iTotalDisplayRecords", 10);
					tableData.put("sEcho", 1);
					tableData.put("aaData", emptyArr);
					resourceResponse.getWriter().println(tableData.toString());
				}

				
			}else {
				
				log.info("else not matched:::Invoice Exam Merchandizee Data Resource accountId of request"+accountId);
				log.info("else not matched:::Invoice Exam Merchandizee Data Resource accountId of User"+accountIdofUser);
				
			}
		} catch (Exception e) {
			log.error("Found error at " + e.getMessage());
		}

		log.info("Invoice Exam Merchandize Data Resource - End");
		return false;
	}
}