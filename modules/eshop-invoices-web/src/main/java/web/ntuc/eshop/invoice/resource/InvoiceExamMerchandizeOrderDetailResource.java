package web.ntuc.eshop.invoice.resource;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil;
import com.liferay.commerce.constants.CommerceOrderPaymentConstants;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.order.status.CommerceOrderStatusRegistry;
import com.liferay.commerce.service.CommerceOrderLocalServiceUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.PortletCommandUtil;
import web.ntuc.eshop.invoice.constants.InvoicePortletKeys;
import web.ntuc.eshop.invoice.dto.OrderDto;
import web.ntuc.eshop.invoice.util.InvoiceUtil;

@Component(immediate = true, property = {
		"mvc.command.name=" + web.ntuc.eshop.invoice.constants.MVCCommandNames.EXAM_MERCHANDIZE_ORDER_DETAIL_RESOURCE,
		"javax.portlet.name=" + InvoicePortletKeys.INVOICE_EXAM_MERCHANDIZE_PORTLET }, service = MVCResourceCommand.class)
public class InvoiceExamMerchandizeOrderDetailResource implements MVCResourceCommand {

	private Log log = LogFactoryUtil.getLog(InvoiceExamMerchandizeOrderDetailResource.class);

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("Invoice Exam Merchandize Detail Resource - Start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);

			User user = themeDisplay.getUser();
			long accountIdofUser = 0;
			CommerceAccount account = CommerceAccountLocalServiceUtil.getPersonalCommerceAccount(user.getUserId());
			if(account != null) {
				accountIdofUser = account.getCommerceAccountId();
			}
			List<CommerceOrder> plainOrderListSize = CommerceOrderLocalServiceUtil
					.getCommerceOrdersByCommerceAccountId(accountIdofUser, -1, -1, null);
			int commerceOrderIdTemp = ParamUtil.getInteger(resourceRequest, "orderId");
			Boolean isValidOrder = false;
			for(CommerceOrder order : plainOrderListSize) {
				if(plainOrderListSize != null && plainOrderListSize.size()>0) {
					if(order.getCommerceOrderId() ==commerceOrderIdTemp ) {
						isValidOrder =true;
					}
				}

			}
			if(isValidOrder) {
				PortletCommandUtil.actionAndResourceCommand(resourceRequest, themeDisplay);

				HttpServletRequest httpRequest = PortalUtil
						.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
				Integer iDisplayStart = ParamUtil.getInteger(httpRequest, "iDisplayStart");
				Integer iDisplayLength = ParamUtil.getInteger(httpRequest, "iDisplayLength");
				if (iDisplayLength == 0) {
					iDisplayLength = 10;
				}

				int sEcho = Integer.parseInt(ParamUtil.getString(httpRequest, "sEcho"));

				int start = iDisplayStart;
				int end = start + iDisplayLength;

				String authToken = CSRFValidationUtil.authToken(resourceRequest);

				PortletSession session = resourceRequest.getPortletSession();

				int commerceOrderId = ParamUtil.getInteger(resourceRequest, "orderId");
				CommerceOrder commerceOrder = CommerceOrderLocalServiceUtil.getCommerceOrder(commerceOrderId);
				String status = _commerceOrderStatusRegistry.getCommerceOrderStatus(commerceOrder.getOrderStatus())
						.getLabel(themeDisplay.getLocale());
				String invoiceNo = ParamUtil.getString(resourceRequest, "invoiceNo");
				String fullName = ParamUtil.getString(resourceRequest, "fullName");
				String phoneNumber = ParamUtil.getString(resourceRequest, "phoneNumber");
//				@SuppressWarnings("unchecked")
//				List<OrderDto> orderList = (List<OrderDto>) session.getAttribute("orderItemList_" + commerceOrderId,
//						PortletSession.PORTLET_SCOPE);
				List<OrderDto> orderList = InvoiceUtil.getOrderItemList(commerceOrder, commerceOrderId, invoiceNo, fullName, phoneNumber, status, themeDisplay).getOrderDtos();
				if (!Validator.isNull(orderList)) {
					if (end > orderList.size()) {
						end = orderList.size();
					}

					List<OrderDto> tempList = orderList.subList(start, end);

					JSONArray parameterJsonArray = JSONFactoryUtil.createJSONArray();
					for (OrderDto order : tempList) {
						JSONObject jsonBranch = JSONFactoryUtil.createJSONObject();

						jsonBranch.put("exam_merchandize_name", order.getExamName());
						jsonBranch.put("exam_merchandize_code", order.getSku());
						jsonBranch.put("quantity", order.getQuantity());
						jsonBranch.put("per_unit", order.getUnitPrice());
						jsonBranch.put("amount", order.getAmount());
						jsonBranch.put("invoice_no", order.getCommerceOrderId());
						jsonBranch.put("order_status", order.getOrderStatus().toUpperCase());
						jsonBranch.put("payment_status", CommerceOrderPaymentConstants.getOrderPaymentStatusLabel(order.getPaymentStatus()).toUpperCase());
						parameterJsonArray.put(jsonBranch);
					}

					int allCount = orderList.size();
					JSONObject tableData = JSONFactoryUtil.createJSONObject();
					tableData.put("iTotalRecords", allCount);
					tableData.put("iTotalDisplayRecords", allCount);
					tableData.put("sEcho", sEcho);
					tableData.put("aaData", parameterJsonArray);
					resourceResponse.getWriter().println(tableData.toString());
				} else {
					JSONArray emptyArr = JSONFactoryUtil.createJSONArray();
					JSONObject tableData = JSONFactoryUtil.createJSONObject();
					tableData.put("iTotalRecords", 0);
					tableData.put("iTotalDisplayRecords", 0);
					tableData.put("sEcho", 1);
					tableData.put("aaData", emptyArr);
					resourceResponse.getWriter().println(tableData.toString());
				}

				
			}else {
				log.info("Invoice Exam Merchandize Detail Resource - Invalid order id");
			}
		} catch (Exception e) {
			log.error("Found error at " + e.getMessage());
		}

		log.info("Invoice Exam Merchandize Detail Resource - End");
		return false;
	}
	
	@Reference
	private CommerceOrderStatusRegistry _commerceOrderStatusRegistry;
}
