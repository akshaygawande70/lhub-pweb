package web.ntuc.eshop.invoice.render;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.service.RoleServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.PortletCommandUtil;
import web.ntuc.eshop.invoice.constants.InvoicePortletKeys;
import web.ntuc.eshop.invoice.dto.InvoiceDto;

@Component(immediate = true, property = {
		"mvc.command.name=" + web.ntuc.eshop.invoice.constants.MVCCommandNames.COURSE_ORDER_DETAIL_RENDER,
		"javax.portlet.name=" + InvoicePortletKeys.INVOICE_COURSE_PORTLET }, service = MVCRenderCommand.class)
public class InvoiceCourseOrderDetailRender implements MVCRenderCommand {

	private static Log log = LogFactoryUtil.getLog(InvoiceCourseOrderDetailRender.class);

	private static String detailPage = "/courses/course-detail.jsp";

	@Override
	public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
		log.info("View Course Order Detail Render - Start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
			String authToken = CSRFValidationUtil.authToken(renderRequest);

			User user = themeDisplay.getUser();
			long userId = user.getUserId();

			Date tempDate = null;
			String fullName = "";
			String fullNameCap = "";
			String nric = "";
			String companyName = "";
			String companyCode = "";
			String uenNumber = "";

			int accType = ParamUtil.getInteger(renderRequest, "accType");
			String batchId = ParamUtil.getString(renderRequest, "batchId");

			PortletCommandUtil.renderCommand(renderRequest);

			PortletSession session = renderRequest.getPortletSession();
			@SuppressWarnings("unchecked")
			List<InvoiceDto> invoiceList = (List<InvoiceDto>) session.getAttribute("invoiceList_" + user.getUuid(),
					PortletSession.PORTLET_SCOPE);

			CommerceAccount account = null;

			List<Role> roleList = new ArrayList<>();
			for (Long roleId : user.getRoleIds()) {
				Role newRole = RoleServiceUtil.getRole(roleId);
				roleList.add(newRole);
			}

			account = CommerceAccountLocalServiceUtil.getPersonalCommerceAccount(user.getUserId());

			if (!user.getMiddleName().isEmpty()) {
				fullName = user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName();
			} else {
				fullName = user.getFirstName() + " " + user.getLastName();
			}

			fullNameCap = fullName.toUpperCase();

			if (accType == 2) {

				uenNumber = (String) account.getExpandoBridge().getAttribute("UEN Number");
				companyCode = (String) account.getExpandoBridge().getAttribute("Company Code");
				companyName = (String) account.getExpandoBridge().getAttribute("Company Name");

			} else if (accType == 1) {

				nric = (String) account.getExpandoBridge().getAttribute("NRIC");
			}

			InvoiceDto invoice = null;

			for (InvoiceDto dto : invoiceList) {
				if (dto.getBatchId().equals(batchId)) {
					invoice = dto;

				}
			}

			renderRequest.setAttribute("invoice", invoice);
			renderRequest.setAttribute("nric", nric);
			renderRequest.setAttribute("accType", accType);
			renderRequest.setAttribute("uenNumber", uenNumber);
			renderRequest.setAttribute("companyCode", companyCode);
			renderRequest.setAttribute("companyName", companyName);

			renderRequest.setAttribute("batchId", invoice.getBatchId());
			renderRequest.setAttribute("courseCode", invoice.getCourseCode());
			renderRequest.setAttribute("courseTitle", invoice.getCourseTitle());
			renderRequest.setAttribute("fullName", fullNameCap);
			renderRequest.setAttribute("authToken", authToken);

		} catch (Exception e) {
			log.error("Failed when render data from " + e.getMessage());
		}
		log.info("View Course Order Detail Render - Stop");
		return detailPage;
	}

}
