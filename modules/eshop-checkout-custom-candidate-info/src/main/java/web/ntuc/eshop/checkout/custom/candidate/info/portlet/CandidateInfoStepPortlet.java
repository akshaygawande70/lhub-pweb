package web.ntuc.eshop.checkout.custom.candidate.info.portlet;

import com.liferay.asset.entry.rel.model.AssetEntryAssetCategoryRel;
import com.liferay.asset.entry.rel.service.AssetEntryAssetCategoryRelLocalServiceUtil;
import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetCategoryLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil;
import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.type.virtual.constants.VirtualCPTypeConstants;
import com.liferay.commerce.product.util.CPInstanceHelper;
import com.liferay.commerce.service.CommerceOrderLocalServiceUtil;
import com.liferay.commerce.util.BaseCommerceCheckoutStep;
import com.liferay.commerce.util.CommerceCheckoutStep;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.PortletPreferences;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.LiferayPortletMode;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.security.auth.AuthTokenUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import api.ntuc.common.util.RoleUtil;
import svc.ntuc.nlh.parameter.model.Parameter;
import svc.ntuc.nlh.parameter.model.ParameterGroup;
import svc.ntuc.nlh.parameter.service.ParameterGroupLocalServiceUtil;
import svc.ntuc.nlh.parameter.service.ParameterLocalServiceUtil;
import web.ntuc.eshop.checkout.custom.candidate.info.constants.CandidateInfoStepPortletKeys;
import web.ntuc.eshop.checkout.custom.candidate.info.display.context.CandidateInfoStepDisplayContext;

@Component(immediate = true, property = { 
		"commerce.checkout.step.name=custom-candidate-info-step",
		"commerce.checkout.step.order:Integer=32" }, service = { CommerceCheckoutStep.class })
public class CandidateInfoStepPortlet extends BaseCommerceCheckoutStep {
	private static final Log log = LogFactoryUtil.getLog(CandidateInfoStepPortlet.class);

	public static final String NAME = "custom-candidate-info-step";

	@Reference
	private JSPRenderer _jspRenderer;

	@Reference
	private CPInstanceHelper _cpInstanceHelper;

	@Reference(target = "(osgi.web.symbolicname=web.ntuc.eshop.checkout.custom.candidate.info)")
	private ServletContext _servletContext;

	public String getName() {
		return "custom-candidate-info-step";
	}
	
	@Override
	public boolean isVisible(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws Exception {
		CandidateInfoStepDisplayContext candidateInfoStepDisplayContext = new CandidateInfoStepDisplayContext(
				this._cpInstanceHelper, httpServletRequest);
		for (CommerceOrderItem commerceOrderItem : candidateInfoStepDisplayContext.getCommerceOrder().getCommerceOrderItems()) {
			if(commerceOrderItem.getCPDefinition().getProductTypeName().equals(VirtualCPTypeConstants.NAME) == true) {
				return super.isVisible(httpServletRequest, httpServletResponse);
			}
		}
		return false;
	}

	@Override
	public boolean isActive(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws Exception {
		CandidateInfoStepDisplayContext candidateInfoStepDisplayContext = new CandidateInfoStepDisplayContext(
				this._cpInstanceHelper, httpServletRequest);
		for (CommerceOrderItem commerceOrderItem : candidateInfoStepDisplayContext.getCommerceOrder().getCommerceOrderItems()) {
			if(commerceOrderItem.getCPDefinition().getProductTypeName().equals(VirtualCPTypeConstants.NAME) == true) {
				return super.isActive(httpServletRequest, httpServletResponse);
			}
		}
		return false;
	}

	public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

		CommerceOrder commerceOrder = (CommerceOrder) actionRequest.getAttribute("COMMERCE_ORDER");
		CandidateInfoStepDisplayContext candidateInfoStepDisplayContext = new CandidateInfoStepDisplayContext();
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		
		for (CommerceOrderItem item : commerceOrder.getCommerceOrderItems()) {
			
			String date1Test = ParamUtil.getString((PortletRequest) actionRequest, "date1");
			CPDefinition cpDefinition = item.getCPDefinition();
			boolean productCitrep = Boolean.parseBoolean(ParamUtil.getString((PortletRequest) actionRequest, "citrepNewFlag"));
			AssetEntry assetEntry = AssetEntryLocalServiceUtil.fetchEntry(CPDefinition.class.getName(),
					cpDefinition.getCPDefinitionId());
			List<AssetCategory> categories = assetEntry.getCategories();
			Map<String, AssetCategory> categoryMap = candidateInfoStepDisplayContext.getSingleCategory(categories);
			Set<String> listField = candidateInfoStepDisplayContext.getListField(commerceOrder, categoryMap);
			listField.add("email_address");
			
			Map<String, String> customShippingField = new HashMap<>();

			if (listField.contains(CandidateInfoStepPortletKeys.EXAM_DATE)) {
				String date1 = ParamUtil.getString((PortletRequest) actionRequest, "date1");
				String time1 = ParamUtil.getString((PortletRequest) actionRequest, "time1");
				time1 = time1.replace(".", ":");
				time1 = time1.replace("a:m", "AM");
				String dateTimeStr1 = date1 + " " + time1;

				String date2 = ParamUtil.getString((PortletRequest) actionRequest, "date2");
				String time2 = ParamUtil.getString((PortletRequest) actionRequest, "time2");
				time2 = time2.replace(".", ":");
				time2 = time2.replace("a:m", "AM");
				String dateTimeStr2 = date2 + " " + time2;

				String date3 = ParamUtil.getString((PortletRequest) actionRequest, "date3");
				String time3 = ParamUtil.getString((PortletRequest) actionRequest, "time3");
				time3 = time3.replace(".", ":");
				time3 = time3.replace("a:m", "AM");
				String dateTimeStr3 = date3 + " " + time3;

				customShippingField.put("Preferred Date 1", dateTimeStr1);
				customShippingField.put("Preferred Date 2", dateTimeStr2);
				customShippingField.put("Preferred Date 3", dateTimeStr3);

			} 
			if (listField.contains(CandidateInfoStepPortletKeys.ACCA_REG_NO)) {
				String accaRegistrationNo = ParamUtil.getString((PortletRequest) actionRequest, "accaRegistrationNo");
				customShippingField.put("ACCA Registration No", accaRegistrationNo);
			}
			
			if (listField.contains(CandidateInfoStepPortletKeys.FULL_NAME)) {
				String candidateName = ParamUtil.getString((PortletRequest) actionRequest, "full_name");
				customShippingField.put("Candidate Full Name", String.valueOf(candidateName));
			} 
			if (listField.contains(CandidateInfoStepPortletKeys.DOB)) {
				String candidateDate = ParamUtil.getString((PortletRequest) actionRequest, "dob");
				customShippingField.put("Candidate Date of Birth", candidateDate);
			} 
			if (listField.contains(CandidateInfoStepPortletKeys.EMAIL_ADDRESS)) {
				String candidateEmail = ParamUtil.getString((PortletRequest) actionRequest, "email_address");
				customShippingField.put("Candidate Email Address", String.valueOf(candidateEmail));
			} 
			if (listField.contains(CandidateInfoStepPortletKeys.GENDER)) {
				boolean gender = ParamUtil.getBoolean((PortletRequest) actionRequest, "gender");
				customShippingField.put("Male", String.valueOf(gender));
			} 
			if (listField.contains(CandidateInfoStepPortletKeys.MS_ID)) {
				String msId = ParamUtil.getString((PortletRequest) actionRequest, "msId");
				customShippingField.put("MS ID", msId);
			}
			if (listField.contains(CandidateInfoStepPortletKeys.PMI_ID)) {
				String pmiId = ParamUtil.getString((PortletRequest) actionRequest, "pmiId");
				customShippingField.put("PMI ID", pmiId);
			} 
			if (listField.contains(CandidateInfoStepPortletKeys.PMI_MEMBERSHIP)) {
				boolean pmiMembership = ParamUtil.getBoolean((PortletRequest) actionRequest, "pmiMembership");
				customShippingField.put("PMI Membership", String.valueOf(pmiMembership));
			}
			if (listField.contains(CandidateInfoStepPortletKeys.CANCEL_REASON)) {
				String reasonForCancellation = ParamUtil.getString((PortletRequest) actionRequest,
						"reasonForCancellation");
				customShippingField.put("Reason For Cancellation", reasonForCancellation);
			}
			if (listField.contains(CandidateInfoStepPortletKeys.ADDITIONAL_NOTES)) {
				String additionalNotes = ParamUtil.getString((PortletRequest) actionRequest, "additionalNotes");
				customShippingField.put("Additional Notes", additionalNotes);
			}
			if (productCitrep) {
				Map<String, String[]> customArrayField = new HashMap<>();
				String orderCitrep = ParamUtil.getString((PortletRequest) actionRequest, "citrep");
				String orderCitrepNew = "true";
				customShippingField.put("CITREP", orderCitrepNew);
				
				
				//modified adi
				String citrepFullNameNric = ParamUtil.getString((PortletRequest) actionRequest, "citrepFullNameNric");
				customShippingField.put("CITREP-Full-Name-NRIC", citrepFullNameNric);
				String nricNumber = ParamUtil.getString((PortletRequest) actionRequest, "nricNumber");
				customShippingField.put("CITREP-NRIC", nricNumber);
				String citrepEmail = ParamUtil.getString((PortletRequest) actionRequest, "citrepEmail");
				customShippingField.put("CITREP-Email", citrepEmail);
				String citrepMobileNumber = ParamUtil.getString((PortletRequest) actionRequest, "citrepMobileNumber");
				customShippingField.put("CITREP-Mobile-Number", citrepMobileNumber);
				String citrepNationalityDetail = ParamUtil.getString((PortletRequest) actionRequest, "nationalityDetail");
				customShippingField.put("CITREP-Nationality-Detail", citrepNationalityDetail);
				String citrepPmiId = ParamUtil.getString((PortletRequest) actionRequest, "pmiId");
				customShippingField.put("PMI ID", citrepPmiId);
				
				
				String citrepStatus = ParamUtil.getString((PortletRequest) actionRequest, "citrepStatus");
				customShippingField.put("CITREP-Full-Time-Student", citrepStatus);
				String citrepExamDate = ParamUtil.getString((PortletRequest) actionRequest, "citrepExamDate");
				customShippingField.put("CITREP-Is-Exam-Date", citrepExamDate);
				String citrepExamDateDetail = "9999-01-01";
				customShippingField.put("CITREP-Exam-Date", citrepExamDateDetail);
				String citrepDob = "9999-01-01";
				customShippingField.put("CITREP-DOB", citrepDob);
				String citrepProfession = ParamUtil.getString((PortletRequest) actionRequest, "citrepProfession");
				customShippingField.put("CITREP-Profession", citrepProfession);
				String citrepNationality = ParamUtil.getString((PortletRequest) actionRequest, "citrepNationality");
				customArrayField.put("CITREP-Nationality", new String[] {citrepNationality});
				String citrepCompanySponsored = ParamUtil.getString((PortletRequest) actionRequest, "citrepCompanySponsored");
				customArrayField.put("CITREP-Company-Sponsored", new String[] {citrepCompanySponsored});
				String citrepCompanyName = ParamUtil.getString((PortletRequest) actionRequest, "citrepCompanyName");
				customShippingField.put("CITREP-Company-Name", citrepCompanyName);
				String citrepDeclaration1 = ParamUtil.getString((PortletRequest) actionRequest, "citrepDeclaration1");
				String citrepDeclaration2 = ParamUtil.getString((PortletRequest) actionRequest, "citrepDeclaration2");
				String citrepDeclaration3 = ParamUtil.getString((PortletRequest) actionRequest, "citrepDeclaration3");
				String citrepDeclaration4 = ParamUtil.getString((PortletRequest) actionRequest, "citrepDeclaration4");
				List<String> citrepDeclaration = new ArrayList<>();
				if(Validator.isNotNull(citrepDeclaration1)) citrepDeclaration.add(citrepDeclaration1);
				if(Validator.isNotNull(citrepDeclaration2)) citrepDeclaration.add(citrepDeclaration2);
				if(Validator.isNotNull(citrepDeclaration3)) citrepDeclaration.add(citrepDeclaration3);
				if(Validator.isNotNull(citrepDeclaration4)) citrepDeclaration.add(citrepDeclaration4);
				String[] declarationArray = new String[citrepDeclaration.size()];
				customArrayField.put("CITREP-Declaration", citrepDeclaration.toArray(declarationArray));
				this.insertOrderCustomArrayField(commerceOrder.getCommerceOrderId(), customArrayField, actionRequest);
				String citrepAdditionalNotes = ParamUtil.getString((PortletRequest) actionRequest, "citrepAdditionalNotes");
				customShippingField.put("CITREP-Additional-Notes", citrepAdditionalNotes);
				String citrepPdf = ParamUtil.getString((PortletRequest) actionRequest, "citrepPdf");
				customShippingField.put("CITREP-Pdf", citrepPdf);
			}
			String invoiceUrl = ParamUtil.getString((PortletRequest) actionRequest, "invoiceUrl");
			customShippingField.put("Order Link", invoiceUrl);
			
			this.insertOrderCustomField(commerceOrder.getCommerceOrderId(), customShippingField, actionRequest);
		}

	}

	public void render(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws Exception {
		
		CandidateInfoStepDisplayContext candidateInfoStepDisplayContext = new CandidateInfoStepDisplayContext(
				this._cpInstanceHelper, httpServletRequest);
		ArrayList<String> times = candidateInfoStepDisplayContext
				.generateTimeRange(CandidateInfoStepPortletKeys.START_TIME, CandidateInfoStepPortletKeys.END_TIME);
		httpServletRequest.setAttribute("times", times);
		CommerceOrder commerceOrder = candidateInfoStepDisplayContext.getCommerceOrder();
		User user = UserLocalServiceUtil.getUser(commerceOrder.getUserId());
		int accType = 0;
		String companyName = "";
		long accountId = 0;
		List<Role> roleList = new ArrayList<Role>();
		for (Long roleId : user.getRoleIds()) {
			Role newRole = RoleServiceUtil.getRole(roleId);
			roleList.add(newRole);
		}
		CommerceAccount account2 = CommerceAccountLocalServiceUtil.getPersonalCommerceAccount(user.getUserId());
		if (RoleUtil.matchByFullRoleName(roleList, "Eshop_Individual_Role")) {
			accType = 1;
		} else if (RoleUtil.matchByFullRoleName(roleList, "Eshop_Corporate_Role")) {
			accType = 2;
			companyName = (String) account2.getExpandoBridge().getAttribute("Company Name");
		}
		
		SimpleDateFormat sdfExpandoDob = new SimpleDateFormat("yyyy/MM/dd");
		Date dobFromExpando = (Date) commerceOrder.getExpandoBridge().getAttribute("Candidate Date of Birth"); 
		String dobNew = sdfExpandoDob.format(dobFromExpando);
		dobNew = dobNew.replace("/", "-");
		httpServletRequest.setAttribute("dobNew", dobNew);

		accountId = account2.getCommerceAccountId();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dob = sdfExpandoDob.format(user.getBirthday());
		dob = dob.replace("/", "-");
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.add(Calendar.DATE, 2);
		today = cal.getTime();

		String addNotes = (String) commerceOrder.getExpandoBridge().getAttribute("Additional Notes");
		Date date1 = (Date) commerceOrder.getExpandoBridge().getAttribute("Preferred Date 1");
		Date date2 = (Date) commerceOrder.getExpandoBridge().getAttribute("Preferred Date 2");
		Date date3 = (Date) commerceOrder.getExpandoBridge().getAttribute("Preferred Date 3");

		String pattern = "MM/dd/yyyy HH:mm:ss";
		DateFormat df = new SimpleDateFormat(pattern);  
		
		String time1 = df.format(date1);
		String time2 = df.format(date2);
		String time3 = df.format(date3);
		String newTime1 = time1.substring(11,16).replace(":", ".")+" a.m";
		String newTime2 = time2.substring(11,16).replace(":", ".")+" a.m";
		String newTime3 = time3.substring(11,16).replace(":", ".")+" a.m";
		httpServletRequest.setAttribute("newTime1", newTime1);
		httpServletRequest.setAttribute("newTime2", newTime2);
		httpServletRequest.setAttribute("newTime3", newTime3);
		
		String accaRegistrationNo = (String) commerceOrder.getExpandoBridge().getAttribute("ACCA Registration No");
		boolean citrep = (Boolean) commerceOrder.getExpandoBridge().getAttribute("CITREP");
		boolean gender = (Boolean) commerceOrder.getExpandoBridge().getAttribute("Male");
		String msId = (String) commerceOrder.getExpandoBridge().getAttribute("MS ID");
		String pmiId = (String) commerceOrder.getExpandoBridge().getAttribute("PMI ID");
		boolean pmiMembership = (Boolean) commerceOrder.getExpandoBridge().getAttribute("PMI Membership");
		String reasonForCancellation = (String) commerceOrder.getExpandoBridge().getAttribute("Reason For Cancellation");
		
		String citrepAdditionalNotes = (String) commerceOrder.getExpandoBridge().getAttribute("CITREP-Additional-Notes");
		String[] citrepCompanySponsored = (String[]) commerceOrder.getExpandoBridge().getAttribute("CITREP-Company-Sponsored");
		String citrepCompanyName = (String) commerceOrder.getExpandoBridge().getAttribute("CITREP-Company-Name");
		String[] citrepDeclaration =  (String[]) commerceOrder.getExpandoBridge().getAttribute("CITREP-Declaration");
	
		Date citrepDob = (Date) commerceOrder.getExpandoBridge().getAttribute("CITREP-DOB");
		String citrepEmail = (String) commerceOrder.getExpandoBridge().getAttribute("CITREP-Email");
		String citrepMobileNumber = (String) commerceOrder.getExpandoBridge().getAttribute("CITREP-Mobile-Number");
		String[] citrepNationality =  (String[]) commerceOrder.getExpandoBridge().getAttribute("CITREP-Nationality");
		String citrepNationalityDetail = (String) commerceOrder.getExpandoBridge().getAttribute("CITREP-Nationality-Detail");
		String citrepNric = (String) commerceOrder.getExpandoBridge().getAttribute("CITREP-NRIC");
		String candidateFullName = (String) commerceOrder.getExpandoBridge().getAttribute("Candidate Full Name");
		String candidateEmailAddress = (String) commerceOrder.getExpandoBridge().getAttribute("Candidate Email Address");
		
		String citrepFullNameNric = (String) commerceOrder.getExpandoBridge().getAttribute("CITREP-Full-Name-NRIC");		
		String citrepProfession = (String) commerceOrder.getExpandoBridge().getAttribute("CITREP-Profession");
		boolean citrepFullTimeStudent = (Boolean) commerceOrder.getExpandoBridge().getAttribute("CITREP-Full-Time-Student");
		
		boolean citrepIsExamDate = (Boolean) commerceOrder.getExpandoBridge().getAttribute("CITREP-Is-Exam-Date");
		Date citrepExamDate = (Date) commerceOrder.getExpandoBridge().getAttribute("CITREP-Exam-Date");
		String citrepPdf = (String) commerceOrder.getExpandoBridge().getAttribute("CITREP-Pdf");
		String citrepDateRange = this.getParamValue(CandidateInfoStepPortletKeys.PARAMETER_ESHOP_DATE_RANGE);
		
		String date1Str = candidateInfoStepDisplayContext.getCustomDate(today.after(date1) ? today : date1);
		String time1Str = candidateInfoStepDisplayContext.getCustomTime(today.after(date1) ? today : date1);
		String date2Str = candidateInfoStepDisplayContext.getCustomDate(today.after(date2) ? today : date2);
		String time2Str = candidateInfoStepDisplayContext.getCustomTime(today.after(date2) ? today : date2);
		String date3Str = candidateInfoStepDisplayContext.getCustomDate(today.after(date3) ? today : date3);
		String time3Str = candidateInfoStepDisplayContext.getCustomTime(today.after(date3) ? today : date3);
		String citrepDobStr = candidateInfoStepDisplayContext.getCustomDate(citrepDob);
		String citrepExamDateStr = candidateInfoStepDisplayContext.getCustomDate(citrepExamDate);
		
		httpServletRequest.setAttribute("accType", accType);
		httpServletRequest.setAttribute("comapanyName", companyName);
		httpServletRequest.setAttribute("accountId", accountId);
		
		httpServletRequest.setAttribute("citrepCompanyName", citrepCompanyName);
		httpServletRequest.setAttribute("citrepNationalityDetail", citrepNationalityDetail);
		httpServletRequest.setAttribute("citrepNric", citrepNric);
		httpServletRequest.setAttribute("candidateFullName", candidateFullName);
		httpServletRequest.setAttribute("candidateEmailAddress", candidateEmailAddress); 
		httpServletRequest.setAttribute("citrepProfession", citrepProfession);
		httpServletRequest.setAttribute("citrepFullTimeStudent", citrepFullTimeStudent);
		httpServletRequest.setAttribute("citrepIsExamDate", citrepIsExamDate);
		httpServletRequest.setAttribute("citrepExamDateStr", citrepExamDateStr);
		
		httpServletRequest.setAttribute("date1Str", date1Str);
		httpServletRequest.setAttribute("time1Str", time1Str);
		httpServletRequest.setAttribute("date2Str", date2Str);
		httpServletRequest.setAttribute("time2Str", time2Str);
		httpServletRequest.setAttribute("date3Str", date3Str);
		httpServletRequest.setAttribute("time3Str", time3Str);
		httpServletRequest.setAttribute("addNotes", addNotes);
		httpServletRequest.setAttribute("accaRegistrationNo", accaRegistrationNo);
		httpServletRequest.setAttribute("citrep", citrep);
		httpServletRequest.setAttribute("gender", gender);
		httpServletRequest.setAttribute("msId", msId);
		httpServletRequest.setAttribute("pmiId", pmiId);
		httpServletRequest.setAttribute("pmiMembership", pmiMembership);
		httpServletRequest.setAttribute("reasonForCancellation", reasonForCancellation);
		httpServletRequest.setAttribute("dob", dob);
		log.info(">>>>>>>>>>>>>>>>>>>>>> dob : " + dob);
		httpServletRequest.setAttribute("user", user);
		httpServletRequest.setAttribute("citrepAdditionalNotes", citrepAdditionalNotes);
		httpServletRequest.setAttribute("citrepCompanySponsored", citrepCompanySponsored);
		httpServletRequest.setAttribute("citrepDeclaration", citrepDeclaration);
		httpServletRequest.setAttribute("citrepDobStr", citrepDobStr);
		httpServletRequest.setAttribute("citrepEmail", citrepEmail);
		httpServletRequest.setAttribute("citrepMobileNumber", citrepMobileNumber);
		httpServletRequest.setAttribute("citrepNationality", citrepNationality);
		httpServletRequest.setAttribute("citrepFullNameNric", citrepFullNameNric);
		httpServletRequest.setAttribute("citrepDateRange", citrepDateRange);
		httpServletRequest.setAttribute("citrepPdf", citrepPdf);
		httpServletRequest.setAttribute("CheckoutCustomCandidateInfoDisplayContext", candidateInfoStepDisplayContext);
		
		List<String> productTypeList = new ArrayList<String>();
		for (CommerceOrderItem item : commerceOrder.getCommerceOrderItems()) {
			CPDefinition cpDefinition = item.getCPDefinition();
			
			String productType = cpDefinition.getProductTypeName();
			productTypeList.add(productType);
			if(productType.contentEquals("virtual")) {
				String examItemName = item.getName();
				httpServletRequest.setAttribute("examItemName", examItemName);
				ThemeDisplay themeDisplay = (ThemeDisplay) httpServletRequest.getAttribute(WebKeys.THEME_DISPLAY);
				String examItemNameLocale = item.getName(themeDisplay.getLocale());
				httpServletRequest.setAttribute("examItemNameLocale", examItemNameLocale);
			}
		}
		
		this._jspRenderer.renderJSP(this._servletContext, httpServletRequest, httpServletResponse, "/view.jsp");
	}

	private void insertOrderCustomField(long commerceOrderId, Map<String, String> customShippingField,
			ActionRequest actionRequest) {
		try {
			
			CandidateInfoStepDisplayContext candidateInfoStepDisplayContext = new CandidateInfoStepDisplayContext();
			Map<String, Serializable> exapandoBridgeAttributes = new HashMap<>();
			String dateKeyword = "date";
			String membershipKeyword = "membership";
			String genderKeyword = "male";
			String citrepKeyword = "citrep";
			String status = "CITREP-Full-Time-Student";
			String isExamDate = "CITREP-Is-Exam-Date";
			String dob = "CITREP-DOB";
			String examDate = "CITREP-Exam-Date";
			String candidateFullName = "Candidate Full Name";
			String candidateDOB = "Candidate Date of Birth";
			String candidateEmail = "Candidate Email Address";
			String preferedDate1 = "Preferred Date 1"; 
			String preferedDate2 = "Preferred Date 2";
			String preferedDate3 = "Preferred Date 3";
			
			customShippingField.forEach((key,value) -> {
                
//                log.info("Key : " + key + " , value : " + value);
                
                if(key.equalsIgnoreCase(preferedDate1) || key.equalsIgnoreCase(preferedDate2) || key.equalsIgnoreCase(preferedDate3)) {
					Date val = candidateInfoStepDisplayContext.generateDateTime(value);
					log.info("generateDateTime val "+key+" : " + val);
				     // Define the date format corresponding to the input date string
			        String dateFormat = "EEE MMM dd HH:mm:ss z yyyy";
			          // Create SimpleDateFormat instance with the specified format
			            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			          // Parse the input date string into a Date object
			          //  Date inputDate = sdf.parse(dateStr);			            
			            // Get today's date
			            Date today = new Date();			   
			            // Compare today's date with the input date
			            if (today.before(val)) {
			            	log.info("exam  date greater than today's date.-- valid request");
			                exapandoBridgeAttributes.put(key, val);
			            } else if (today.after(val)) {
			            	log.info("exam  date less than today's date- invalid request");
			            } else {
			            	log.info("exam  date = todays date-- invalid request");
			            }
					
				}
                else if(key.equalsIgnoreCase(candidateDOB)) {
					Date val = candidateInfoStepDisplayContext.generateDate(value);
					exapandoBridgeAttributes.put(key, val);
				}
                else if(key.equalsIgnoreCase(candidateEmail)) {
					exapandoBridgeAttributes.put(key, value);
				}
                else if(key.toLowerCase().indexOf(membershipKeyword.toLowerCase()) != -1 || key.equalsIgnoreCase(genderKeyword) || key.equalsIgnoreCase(citrepKeyword) || key.equalsIgnoreCase(status) || key.equalsIgnoreCase(isExamDate)) {
                    Boolean val = Boolean.valueOf(value);
                    exapandoBridgeAttributes.put(key, val);
                } else if (key.equalsIgnoreCase(dob) || key.equalsIgnoreCase(examDate)) {
                    Date val = candidateInfoStepDisplayContext.generateDate(value);
                    exapandoBridgeAttributes.put(key, val);
                }else if ((key.toLowerCase().indexOf(dateKeyword.toLowerCase()) != -1
                        && !key.toLowerCase().equalsIgnoreCase(candidateFullName.toLowerCase())
                                        && !key.toLowerCase().equalsIgnoreCase(candidateEmail.toLowerCase()))
                        || key.equalsIgnoreCase(dob) || key.equalsIgnoreCase(examDate)) {
                    log.info("generateDateTime : " + value);
                    Date val = candidateInfoStepDisplayContext.generateDateTime(value);
                    Calendar cal = Calendar.getInstance();
                    exapandoBridgeAttributes.put(key, val);
                } else {
                    exapandoBridgeAttributes.put(key, value);
                }
                
            });
			
			ServiceContext serviceContext = ServiceContextFactory.getInstance(CommerceOrder.class.getName(),
					(PortletRequest) actionRequest);
			serviceContext.setExpandoBridgeAttributes(exapandoBridgeAttributes);
			CommerceOrderLocalServiceUtil.updateCustomFields(commerceOrderId, serviceContext);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error while inserting custom field : " + e.getMessage());
		}
	}
	
	private void insertOrderCustomArrayField(long commerceOrderId, Map<String, String[]> customShippingField,
			ActionRequest actionRequest) {
		try {
			CandidateInfoStepDisplayContext candidateInfoStepDisplayContext = new CandidateInfoStepDisplayContext();
			Map<String, Serializable> exapandoBridgeAttributes = new HashMap<>();
			customShippingField.forEach((key,value) -> {
				exapandoBridgeAttributes.put(key, value);
				
			});

			ServiceContext serviceContext = ServiceContextFactory.getInstance(CommerceOrder.class.getName(),
					(PortletRequest) actionRequest);
			serviceContext.setExpandoBridgeAttributes(exapandoBridgeAttributes);
			CommerceOrderLocalServiceUtil.updateCustomFields(commerceOrderId, serviceContext);
		} catch (Exception e) {
			log.error("error while inserting custom field : " + e.getMessage());
		}
	}

	private String getParamValue(String param) {
		ParameterGroup parameterGroup = ParameterGroupLocalServiceUtil
				.getByCode(CandidateInfoStepPortletKeys.PARAMETER_AUTH_GROUP_CODE, false);
		long siteGroupId = parameterGroup.getGroupId();
		Parameter parameter = ParameterLocalServiceUtil.getByGroupCode(siteGroupId,
				parameterGroup.getParameterGroupId(), param, false);
		return parameter.getParamValue();
	}

}
