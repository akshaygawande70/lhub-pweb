package web.ntuc.eshop.invoice.render;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.order.status.CommerceOrderStatusRegistry;
import com.liferay.commerce.service.CommerceOrderLocalServiceUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Contact;
import com.liferay.portal.kernel.model.Phone;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.service.PhoneLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.PortletCommandUtil;
import web.ntuc.eshop.invoice.constants.InvoicePortletKeys;
import web.ntuc.eshop.invoice.dto.ResultOrderListDto;
import web.ntuc.eshop.invoice.util.InvoiceUtil;

@Component(immediate = true, property = {
		"mvc.command.name=" + web.ntuc.eshop.invoice.constants.MVCCommandNames.EXAM_MERCHANDIZE_ORDER_DETAIL_RENDER,
		"javax.portlet.name=" + InvoicePortletKeys.INVOICE_EXAM_MERCHANDIZE_PORTLET }, service = MVCRenderCommand.class)
public class InvoiceExamMerchandizeOrderDetailRender implements MVCRenderCommand {
	
	private static Log log = LogFactoryUtil.getLog(InvoiceExamMerchandizeOrderDetailRender.class);

	private static String detailPage = "/exam_merchandize_result/exam-merchandize-detail.jsp";

	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		log.info("View Exam Merchandize Order Detail Render - Start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
			if (themeDisplay.isSignedIn()) {
				String authToken = CSRFValidationUtil.authToken(renderRequest);
				PortletCommandUtil.renderCommand(renderRequest);
				String fullName = "";
				String fullNameCap = "";

				long phoneId = 0;
				String phoneNumber = "";
				String invoiceNo = "";

				User user = themeDisplay.getUser();
				long userId = user.getUserId();
				Contact contact = user.getContact();

				String companyName = ParamUtil.getString(renderRequest, "companyName");
				int accType = ParamUtil.getInteger(renderRequest, "accType");
				int commerceOrderId = ParamUtil.getInteger(renderRequest, "orderId");

				CommerceAccount account = CommerceAccountLocalServiceUtil.getPersonalCommerceAccount(user.getUserId());

				if (accType == 2) {
					companyName = (String) account.getExpandoBridge().getAttribute("Company Name");
				} else {
					if (!user.getMiddleName().isEmpty()) {
						fullName = user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName();
					} else {
						fullName = user.getFirstName() + " " + user.getLastName();
					}

					fullNameCap = fullName.toUpperCase();
				}

				List<Phone> phoneList = PhoneLocalServiceUtil.getPhones(user.getCompanyId(), Contact.class.getName(),
						contact.getContactId());

				if (!phoneList.isEmpty()) {
					for (Phone phone : phoneList) {
						if (phone.isPrimary()) {
							phoneId = phone.getPhoneId();
							phoneNumber = phone.getNumber();
						}
					}
				}

				CommerceOrder commerceOrder = CommerceOrderLocalServiceUtil.getCommerceOrder(commerceOrderId);
				String status = _commerceOrderStatusRegistry.getCommerceOrderStatus(commerceOrder.getPaymentStatus())
						.getLabel(themeDisplay.getLocale());
				ResultOrderListDto resultOrderListDto = InvoiceUtil.getOrderItemList(commerceOrder, commerceOrderId,
						invoiceNo, fullNameCap, phoneNumber, status, themeDisplay);

				renderRequest.setAttribute("orderItemList", resultOrderListDto.getOrderDtos());
				renderRequest.setAttribute("orderId", commerceOrderId);
				renderRequest.setAttribute("invoiceNo", resultOrderListDto.getInvoiceNo());
				renderRequest.setAttribute("companyName", companyName);
				renderRequest.setAttribute("phoneNumber", phoneNumber);
				renderRequest.setAttribute("fullName", fullNameCap);
				renderRequest.setAttribute("authToken", authToken);
				renderRequest.setAttribute("accType", accType);
				renderRequest.setAttribute("user", user);
			} else {
				HttpServletRequest httpServletResquest = PortalUtil
						.getOriginalServletRequest(PortalUtil.getHttpServletRequest(renderRequest));
				HttpServletResponse httpServletResponse = PortalUtil.getHttpServletResponse(renderResponse);
				String queryString = httpServletResquest.getRequestURI()+StringPool.QUESTION+httpServletResquest.getQueryString();
				String redirectUrl = "_com_liferay_login_web_portlet_LoginPortlet_redirect=";
				httpServletResponse.sendRedirect(themeDisplay.getPortalURL()+"/registration"+StringPool.QUESTION+redirectUrl+queryString);
			}
		} catch (Exception e) {
			log.error("Failed when render data from " + e.getMessage());
		}
		log.info("View Exam Merchandize Order Detail Render - Stop");
		return detailPage;
	}

	@Reference
	private CommerceOrderStatusRegistry _commerceOrderStatusRegistry;
}
