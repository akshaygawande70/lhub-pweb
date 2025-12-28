package web.ntuc.eshop.invoice.resource;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.AESEncryptUtil;
import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.HttpApiUtil;
import api.ntuc.common.util.PortletCommandUtil;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.eshop.invoice.constants.InvoicePortletKeys;
import web.ntuc.eshop.invoice.constants.MVCCommandNames;
import web.ntuc.eshop.invoice.dto.InvoiceDto;
import web.ntuc.eshop.invoice.util.InvoiceUtil;

@Component(immediate = true, property = {
		"mvc.command.name=" + web.ntuc.eshop.invoice.constants.MVCCommandNames.COURSE_ORDER_RESOURCE,
		"javax.portlet.name=" + InvoicePortletKeys.INVOICE_COURSE_PORTLET }, service = MVCResourceCommand.class)
public class InvoiceCourseOrderResource implements MVCResourceCommand {

	private Log log = LogFactoryUtil.getLog(InvoiceCourseOrderResource.class);

	private static final String INDIVIDUAL_URL = "/individual/invoices";
	private static final String CORPORATE_URL = "/corporate/invoices";

	@Override
	public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException {
		log.info("Invoice Course Data Resource - Start");
		try {
			ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
			String portletName = (String) resourceRequest.getAttribute(WebKeys.PORTLET_ID);
			User user = themeDisplay.getUser();

			String authToken = CSRFValidationUtil.authToken(resourceRequest);
			int accType = ParamUtil.getInteger(resourceRequest, "accType");
			String nric = ParamUtil.getString(resourceRequest, "nric");
			String birthDate = ParamUtil.getString(resourceRequest, "birthDate");
			String companyCode = ParamUtil.getString(resourceRequest, "companyCode");
			String uenNumber = ParamUtil.getString(resourceRequest, "uenNumber");

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

			ParameterGroup parameterAuthGroup = ParameterGroupLocalServiceUtil
					.getByCode(InvoicePortletKeys.PARAMETER_AUTH_GROUP_CODE, false);
			long siteGroupId = parameterAuthGroup.getGroupId();
			Parameter parameterClientId = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(), InvoicePortletKeys.PARAMETER_ESHOP_CLIENT_ID_CODE, false);
			Parameter parameterClientSecret = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterAuthGroup.getParameterGroupId(), InvoicePortletKeys.PARAMETER_ESHOP_CLIENT_SECRET_CODE,
					false);

			ParameterGroup parameterApiGroup = ParameterGroupLocalServiceUtil
					.getByCode(InvoicePortletKeys.PARAMETER_URL_GROUP_CODE, false);
			Parameter parameterApiCorporateValidate = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
					parameterApiGroup.getParameterGroupId(), InvoicePortletKeys.PARAMETER_ESHOP_API_CODE, false);

			String encodedKey = InvoiceUtil.getSecretKey();

			JSONArray parameterJsonArray = JSONFactoryUtil.createJSONArray();

			PortletSession session = resourceRequest.getPortletSession();

			Object tmsResponseSession = (Object) session.getAttribute("invoiceResponse_" + user.getUuid(),
					PortletSession.PORTLET_SCOPE);

			List<InvoiceDto> invoiceList = new ArrayList<>();
			if (!Validator.isNull(tmsResponseSession)) {

				if (!Validator.isNull(tmsResponseSession) && tmsResponseSession.toString().contains("[")) {
					JSONArray invoiceArr = JSONFactoryUtil.createJSONArray(tmsResponseSession.toString());

					if (invoiceArr.length() < end) {
						end = invoiceArr.length();
					}

					for (int i = start; i < end; i++) {
						JSONObject obj = JSONFactoryUtil.createJSONObject(invoiceArr.get(i).toString());
						mappedParam(accType, obj, themeDisplay, resourceRequest, portletName, authToken,
								parameterJsonArray, invoiceList);
					}

					int allCount = invoiceArr.length();

					resourceResponse.getWriter().println(
							InvoiceUtil.getTableData(allCount, allCount, sEcho, parameterJsonArray).toString());

				} else if (!Validator.isNull(tmsResponseSession) && !tmsResponseSession.toString().contains("[")) {
					JSONObject obj = JSONFactoryUtil.createJSONObject(tmsResponseSession.toString());
					String invoiceUrl = obj.getString("invoice_url");
					if(Validator.isNull(invoiceUrl)) {
						JSONArray emptyArr = JSONFactoryUtil.createJSONArray();
						resourceResponse.getWriter().println(InvoiceUtil.getTableData(0, 10, "1", emptyArr).toString());
					} else {
						end = 1;

						for (int i = start; i < end; i++) {
							mappedParam(accType, obj, themeDisplay, resourceRequest, portletName, authToken,
									parameterJsonArray, invoiceList);
						}

						int allCount = parameterJsonArray.length();
						resourceResponse.getWriter().println(
								InvoiceUtil.getTableData(allCount, allCount, sEcho, parameterJsonArray).toString());
					}
				} else {
					JSONArray emptyArr = JSONFactoryUtil.createJSONArray();
					resourceResponse.getWriter().println(InvoiceUtil.getTableData(0, 10, "1", emptyArr).toString());
				}
			} else {

				JSONObject jsonOrderRequest = JSONFactoryUtil.createJSONObject();

				if (accType == 1) {
					jsonOrderRequest.put("nric_number", nric);
					jsonOrderRequest.put("dob", birthDate);
				} else if (accType == 2) {
					jsonOrderRequest.put("company_code", companyCode);
					jsonOrderRequest.put("uen_no", uenNumber);
				} else {
					jsonOrderRequest.put("company_code", "0");
					jsonOrderRequest.put("uen_no", "0");
					jsonOrderRequest.put("nric_number", "0");
					jsonOrderRequest.put("dob", "1960-01-01");
				}

				String encryptedJsonRequest = AESEncryptUtil.encrypt(jsonOrderRequest.toString(), encodedKey);

				Object tmsResponse = null;
				if (accType == 1) {
					tmsResponse = HttpApiUtil.request(parameterApiCorporateValidate.getParamValue() + INDIVIDUAL_URL,
							Http.Method.GET.name(), encryptedJsonRequest, parameterClientId.getParamValue(),
							parameterClientSecret.getParamValue(), encodedKey);
				} else {
					tmsResponse = HttpApiUtil.request(parameterApiCorporateValidate.getParamValue() + CORPORATE_URL,
							Http.Method.GET.name(), encryptedJsonRequest, parameterClientId.getParamValue(),
							parameterClientSecret.getParamValue(), encodedKey);
				}
				session.setAttribute("invoiceResponse_" + user.getUuid(), tmsResponse, PortletSession.PORTLET_SCOPE);

				if (!Validator.isNull(tmsResponse) && tmsResponse.toString().contains("[")) {
					JSONArray invoiceArr = JSONFactoryUtil.createJSONArray(tmsResponse.toString());

					if (invoiceArr.length() < end) {
						end = invoiceArr.length();
					}

					for (int i = start; i < end; i++) {
						JSONObject obj = JSONFactoryUtil.createJSONObject(invoiceArr.get(i).toString());
						mappedParam(accType, obj, themeDisplay, resourceRequest, portletName, authToken,
								parameterJsonArray, invoiceList);
					}

					session.setAttribute("invoiceList_" + user.getUuid(), invoiceList, PortletSession.PORTLET_SCOPE);

					int allCount = invoiceArr.length();
					resourceResponse.getWriter().println(
							InvoiceUtil.getTableData(allCount, allCount, sEcho, parameterJsonArray).toString());

				} else if (!Validator.isNull(tmsResponse) && !tmsResponse.toString().contains("[")) {
					JSONObject obj = JSONFactoryUtil.createJSONObject(tmsResponse.toString());
					String invoiceUrl = obj.getString("invoice_url");
					if(Validator.isNull(invoiceUrl)) {
						JSONArray emptyArr = JSONFactoryUtil.createJSONArray();
						resourceResponse.getWriter().println(InvoiceUtil.getTableData(0, 10, "1", emptyArr).toString());
					} else {
						end = 1;

						for (int i = start; i < end; i++) {
							mappedParam(accType, obj, themeDisplay, resourceRequest, portletName, authToken,
									parameterJsonArray, invoiceList);
						}

						session.setAttribute("invoiceList_" + user.getUuid(), invoiceList, PortletSession.PORTLET_SCOPE);

						int allCount = parameterJsonArray.length();
						resourceResponse.getWriter().println(
								InvoiceUtil.getTableData(allCount, allCount, sEcho, parameterJsonArray).toString());
					}
				} else {
					JSONArray emptyArr = JSONFactoryUtil.createJSONArray();
					resourceResponse.getWriter().println(InvoiceUtil.getTableData(0, 10, "1", emptyArr).toString());
				}
			}

		} catch (Exception e) {
			log.error("Found error at " + e.getMessage());
			return true;
		}

		log.info("Invoice Course Data Resource - End");
		return false;
	}

	public void mappedParam(int accType, JSONObject obj, ThemeDisplay themeDisplay, ResourceRequest resourceRequest,
			String portletName, String authToken, JSONArray paramJsonArray, List<InvoiceDto> invoiceList) {
		JSONObject jsonBranch = JSONFactoryUtil.createJSONObject();
		InvoiceDto dto = new InvoiceDto();

		if (accType == 1) {
			dto.setAmount(obj.getDouble("amount"));
			dto.setPaymentURL(obj.getString("payment_url"));
			dto.setBatchId(obj.getString("batch_id"));
			dto.setTrnId(obj.getString("trn_id"));
			dto.setInvoiceNo(obj.getString("invoice_no"));
			dto.setCourseTitle(obj.getString("course_title"));
			dto.setNricNumber(obj.getString("nric_number"));
			dto.setCourseStartDate(
					parseDate("yyyy-MM-dd'T'HH:mm:ss", "dd MMMM yyyy", obj.getString("course_start_date")));
			dto.setCourseCode(obj.getString("course_code"));
			dto.setPurchaseDate(parseDate("yyyy-MM-dd'T'HH:mm:ss.SS", "dd MMMM yyyy", obj.getString("purchase_date")));
			dto.setPaymentMethod(obj.getString("payment_method"));
			dto.setInvoiceURL(obj.getString("invoice_url"));
			dto.setStatus(obj.getString("status"));
		} else {
			dto.setAmount(obj.getDouble("amount"));
			dto.setPaymentURL(obj.getString("payment_url"));
			dto.setTrnId(obj.getString("trn_id"));
			dto.setBatchId(obj.getString("batchid"));
			dto.setCourseStartDate(parseDate("yyyy-MM-dd'T'HH:mm:ss", "dd MMMM yyyy", obj.getString("startdate")));
			dto.setCourseTitle(obj.getString("coursetitle"));
			dto.setBano(obj.getString("bano"));
			dto.setCourseCode(obj.getString("coursecode"));
			dto.setPurchaseDate(parseDate("yyyy-MM-dd'T'HH:mm:ss.SSS", "dd MMMM yyyy", obj.getString("purchase_date")));
			dto.setPaymentMethod(obj.getString("payment_method"));
			dto.setInvoiceURL(obj.getString("invoice_url"));
			dto.setStatus(obj.getString("status"));
			dto.setSubBookingId(obj.getString("subbookingid"));
			dto.setPartyId(obj.getString("partyid"));
		}

		jsonBranch.put("course_title", dto.getCourseTitle());
		jsonBranch.put("course_code", dto.getCourseCode());
		jsonBranch.put("batch_id", dto.getBatchId());
		jsonBranch.put("amount", dto.getAmount());

		PortletURL detailUrl = PortletURLFactoryUtil.create(PortalUtil.getHttpServletRequest(resourceRequest),
				portletName, themeDisplay.getLayout().getPlid(), PortletRequest.RENDER_PHASE);
		detailUrl.getRenderParameters().setValue("mvcRenderCommandName", MVCCommandNames.COURSE_ORDER_DETAIL_RENDER);
		detailUrl.getRenderParameters().setValue("accType", String.valueOf(accType));
		detailUrl.getRenderParameters().setValue("batchId", dto.getBatchId());

		if (accType == 1) {
			detailUrl.getRenderParameters().setValue("invoice_no", dto.getInvoiceNo());
			jsonBranch.put("invoice_no", dto.getInvoiceNo());
		} else {
			detailUrl.getRenderParameters().setValue("invoice_no", dto.getBano());
			jsonBranch.put("invoice_no", dto.getBano());
		}

		detailUrl.getRenderParameters().setValue("authToken", authToken);
		jsonBranch.put("detailUrl", detailUrl);

		invoiceList.add(dto);

		paramJsonArray.put(jsonBranch);

	}

	public String parseDate(String oldFormat, String newFormat, String inputDate) {
		String tempDate = "";

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
			Date d = sdf.parse(inputDate);
			sdf.applyPattern(newFormat);
			tempDate = sdf.format(d);

		} catch (Exception e) {
			log.error("Error while parsing the date cause " + e.getMessage());
		}
		return tempDate;
	}
}
