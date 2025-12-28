package web.ntuc.eshop.checkout.custom.candidate.info.display.context;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetVocabularyLocalServiceUtil;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.util.CPInstanceHelper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import web.ntuc.eshop.checkout.custom.candidate.info.constants.CandidateInfoStepPortletKeys;

public class CandidateInfoStepDisplayContext {
	private final CPInstanceHelper _cpInstanceHelper;

	private final HttpServletRequest _httHttpServletRequest;

	private final CommerceOrder _commerceOrder;

	private static final Log log = LogFactoryUtil.getLog(CandidateInfoStepDisplayContext.class);

	private String orderNotes1;
	private String orderNotes2;
	
	public CandidateInfoStepDisplayContext() {
		this._commerceOrder = null;
		this._cpInstanceHelper = null;
		this._httHttpServletRequest = null;
	}

	public CandidateInfoStepDisplayContext(CPInstanceHelper cpInstanceHelper, HttpServletRequest httpServletRequest) {
		this._cpInstanceHelper = cpInstanceHelper;
		this._httHttpServletRequest = httpServletRequest;
		this._commerceOrder = (CommerceOrder) this._httHttpServletRequest.getAttribute("COMMERCE_ORDER");
	}

	public String getOrderNotes1() {
		return orderNotes1;
	}

	public void setOrderNotes1(String orderNotes1) {
		this.orderNotes1 = orderNotes1;
	}

	public String getOrderNotes2() {
		return orderNotes2;
	}

	public void setOrderNotes2(String orderNotes2) {
		this.orderNotes2 = orderNotes2;
	}

	public CommerceOrder getCommerceOrder() {
		return this._commerceOrder;
	}

	public String getCommerceOrderItemThumbnailSrc(CommerceOrderItem commerceOrderItem) throws Exception {
		return this._cpInstanceHelper.getCPInstanceThumbnailSrc(commerceOrderItem.getCPInstanceId());
	}

	public String getCustomDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	public String getCustomTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		return sdf.format(date);
	}

	public Set<String> getListField(CommerceOrder commerceOrder, Map<String, AssetCategory> categoryMap) throws PortalException {
		AssetCategory parentCategory = categoryMap.get(CandidateInfoStepPortletKeys.CATEGORY_PARENT);
		AssetCategory childCategory = categoryMap.get(CandidateInfoStepPortletKeys.CATEGORY_CHILD);
		
		Set<String> listField = new HashSet<>();
		listField.add(CandidateInfoStepPortletKeys.DOB);
		listField.add(CandidateInfoStepPortletKeys.EXAM_DATE);
		if (parentCategory.getName().equalsIgnoreCase(CandidateInfoStepPortletKeys.ACCA)) {
			listField.add(CandidateInfoStepPortletKeys.FULL_NAME);
			listField.add(CandidateInfoStepPortletKeys.CONTACT_NO);
			listField.add(CandidateInfoStepPortletKeys.DOB);
			listField.add(CandidateInfoStepPortletKeys.EXAM_CODE_AND_TITLE);
			listField.add(CandidateInfoStepPortletKeys.ACCA_REG_NO);
			listField.add(CandidateInfoStepPortletKeys.EXAM_DATE);
			listField.add(CandidateInfoStepPortletKeys.ADDITIONAL_NOTES);
			listField.add(CandidateInfoStepPortletKeys.NOTES_1);
			setOrderNotes1("selected-dates-subject-to-availability");
			listField.add(CandidateInfoStepPortletKeys.NOTES_2);
			setOrderNotes2("select-date-wed-fri-9-1-3");
		} else if (parentCategory.getName().equalsIgnoreCase(CandidateInfoStepPortletKeys.ITIL)
				|| parentCategory.getName().equalsIgnoreCase(CandidateInfoStepPortletKeys.PRINCE2)) {
			listField.add(CandidateInfoStepPortletKeys.FULL_NAME);
			listField.add(CandidateInfoStepPortletKeys.CONTACT_NO);
			listField.add(CandidateInfoStepPortletKeys.EMAIL_ADDRESS);
			listField.add(CandidateInfoStepPortletKeys.DOB);
			listField.add(CandidateInfoStepPortletKeys.GENDER);
			listField.add(CandidateInfoStepPortletKeys.EXAM_CODE_AND_TITLE);
			listField.add(CandidateInfoStepPortletKeys.EXAM_DATE);
			listField.add(CandidateInfoStepPortletKeys.ADDITIONAL_NOTES);
			listField.add(CandidateInfoStepPortletKeys.NOTES_1);
			setOrderNotes1("selected-dates-subject-to-availability");
			listField.add(CandidateInfoStepPortletKeys.NOTES_2);
			setOrderNotes2("select-date-wed-fri-9-3");
		} else if (parentCategory.getName().equalsIgnoreCase(CandidateInfoStepPortletKeys.PMI)) {
			listField.add(CandidateInfoStepPortletKeys.FULL_NAME);
			listField.add(CandidateInfoStepPortletKeys.CITREP);
			listField.add(CandidateInfoStepPortletKeys.ADDITIONAL_NOTES);
			listField.add(CandidateInfoStepPortletKeys.NOTES_1);
			listField.add(CandidateInfoStepPortletKeys.DOB);
			listField.add(CandidateInfoStepPortletKeys.EXAM_DATE);
			setOrderNotes1("send-screenshoot");
			if (childCategory.getName().equalsIgnoreCase(CandidateInfoStepPortletKeys.PMI_MEMBER)) {
				listField.add(CandidateInfoStepPortletKeys.PMI_ID);
			}
			
		} else if (parentCategory.getName().equalsIgnoreCase(CandidateInfoStepPortletKeys.ISACA)) {
			addField2(listField);
		} else if (parentCategory.getName().equalsIgnoreCase(CandidateInfoStepPortletKeys.CISCO)) {
			listField.add(CandidateInfoStepPortletKeys.FULL_NAME);
			listField.add(CandidateInfoStepPortletKeys.CONTACT_NO);
			listField.add(CandidateInfoStepPortletKeys.EMAIL_ADDRESS);
			listField.add(CandidateInfoStepPortletKeys.DOB);
			listField.add(CandidateInfoStepPortletKeys.MAILING_ADDRESS);
			listField.add(CandidateInfoStepPortletKeys.EXAM_CODE_AND_TITLE);
			listField.add(CandidateInfoStepPortletKeys.EXAM_DATE);
			listField.add(CandidateInfoStepPortletKeys.ADDITIONAL_NOTES);
			listField.add(CandidateInfoStepPortletKeys.NOTES_1);
			setOrderNotes1("selected-dates-subject-to-availability");
			listField.add(CandidateInfoStepPortletKeys.NOTES_2);
			setOrderNotes2("select-date-mon-fri-9-2");
		} else if (parentCategory.getName().equalsIgnoreCase(CandidateInfoStepPortletKeys.MICROSOFT)) {
			listField.add(CandidateInfoStepPortletKeys.FULL_NAME);
			listField.add(CandidateInfoStepPortletKeys.CONTACT_NO);
			listField.add(CandidateInfoStepPortletKeys.EMAIL_ADDRESS);
			listField.add(CandidateInfoStepPortletKeys.DOB);
			listField.add(CandidateInfoStepPortletKeys.MS_ID);
			listField.add(CandidateInfoStepPortletKeys.MAILING_ADDRESS);
			listField.add(CandidateInfoStepPortletKeys.EXAM_CODE_AND_TITLE);
			listField.add(CandidateInfoStepPortletKeys.EXAM_DATE);
			listField.add(CandidateInfoStepPortletKeys.ADDITIONAL_NOTES);
			listField.add(CandidateInfoStepPortletKeys.NOTES_1);
			setOrderNotes1("selected-dates-subject-to-availability");
			listField.add(CandidateInfoStepPortletKeys.NOTES_2);
			setOrderNotes2("select-date-mon-fri-9-2");
		} else if (parentCategory.getName().equalsIgnoreCase(CandidateInfoStepPortletKeys.BLOCKCHAIN)) {
			addField1(listField);
			listField.add(CandidateInfoStepPortletKeys.NOTES_2);
			setOrderNotes1("select-date-mon-fri-9-2");
		} else if (parentCategory.getName().equalsIgnoreCase(CandidateInfoStepPortletKeys.ORACLE)) {
			listField.add(CandidateInfoStepPortletKeys.FULL_NAME);
			listField.add(CandidateInfoStepPortletKeys.CONTACT_NO);
			listField.add(CandidateInfoStepPortletKeys.EMAIL_ADDRESS);
			listField.add(CandidateInfoStepPortletKeys.MAILING_ADDRESS);
			listField.add(CandidateInfoStepPortletKeys.EXAM_CODE_AND_TITLE);
			listField.add(CandidateInfoStepPortletKeys.ADDITIONAL_NOTES);
			listField.add(CandidateInfoStepPortletKeys.NOTES_1);
			setOrderNotes1("voucher-code-will-be-emailed-to-candidate");
		} else if (parentCategory.getName().equalsIgnoreCase(CandidateInfoStepPortletKeys.DEVOPS)) {
			addField2(listField);
		} else if (parentCategory.getName().equalsIgnoreCase(CandidateInfoStepPortletKeys.MISCELLANEOUS)) {
			for (CommerceOrderItem item : commerceOrder.getCommerceOrderItems()) {
				CPDefinition cpDefinition = item.getCPDefinition();
				if (HtmlUtil.escape(cpDefinition.getName())
						.equalsIgnoreCase(CandidateInfoStepPortletKeys.EXAM_RESCHEDULE_FEE)) {
					listField.add(CandidateInfoStepPortletKeys.FULL_NAME);
					listField.add(CandidateInfoStepPortletKeys.CONTACT_NO);
					listField.add(CandidateInfoStepPortletKeys.EMAIL_ADDRESS);
					listField.add(CandidateInfoStepPortletKeys.EXAM_CODE_AND_TITLE);
					listField.add(CandidateInfoStepPortletKeys.EXAM_DATE);
					listField.add(CandidateInfoStepPortletKeys.ADDITIONAL_NOTES);
					listField.add(CandidateInfoStepPortletKeys.NOTES_1);
					setOrderNotes1("selected-dates-subject-to-availability");
					listField.add(CandidateInfoStepPortletKeys.NOTES_2);
					setOrderNotes2("select-date-mon-fri-9-3");
				} else if (HtmlUtil.escape(cpDefinition.getName())
						.equalsIgnoreCase(CandidateInfoStepPortletKeys.RESCHEDULE_FEE)) {
					addField1(listField);
					listField.add(CandidateInfoStepPortletKeys.NOTES_2);
					setOrderNotes1("select-date-wed-fri-9-1130-230");
				} else if (HtmlUtil.escape(cpDefinition.getName())
						.equalsIgnoreCase(CandidateInfoStepPortletKeys.LOMA_ADMIN_FEE)) {
					addField1(listField);
					listField.add(CandidateInfoStepPortletKeys.NOTES_2);
					setOrderNotes1("select-date-mon-fri-9-3");
				} else if (HtmlUtil.escape(cpDefinition.getName())
						.equalsIgnoreCase(CandidateInfoStepPortletKeys.ADMIN_FEE)) {
					listField.add(CandidateInfoStepPortletKeys.FULL_NAME);
					listField.add(CandidateInfoStepPortletKeys.CONTACT_NO);
					listField.add(CandidateInfoStepPortletKeys.EMAIL_ADDRESS);
					listField.add(CandidateInfoStepPortletKeys.EXAM_CODE_AND_TITLE);
					listField.add(CandidateInfoStepPortletKeys.CANCEL_REASON);
				} else {
					listField.add(CandidateInfoStepPortletKeys.FULL_NAME);
					listField.add(CandidateInfoStepPortletKeys.CONTACT_NO);
					listField.add(CandidateInfoStepPortletKeys.EMAIL_ADDRESS);
					listField.add(CandidateInfoStepPortletKeys.EXAM_CODE_AND_TITLE);
				}
			}
		} else {
			listField.add(CandidateInfoStepPortletKeys.NOTES_1);
			setOrderNotes1("selected-dates-subject-to-availability");
			listField.add(CandidateInfoStepPortletKeys.FULL_NAME);
			listField.add(CandidateInfoStepPortletKeys.EXAM_CODE_AND_TITLE);
			listField.add(CandidateInfoStepPortletKeys.EXAM_DATE);
			listField.add(CandidateInfoStepPortletKeys.ADDITIONAL_NOTES);
		}
		return listField;
	}

	private void addField1(Set<String> listField) {
		listField.add(CandidateInfoStepPortletKeys.FULL_NAME);
		listField.add(CandidateInfoStepPortletKeys.CONTACT_NO);
		listField.add(CandidateInfoStepPortletKeys.EMAIL_ADDRESS);
		listField.add(CandidateInfoStepPortletKeys.DOB);
		listField.add(CandidateInfoStepPortletKeys.MAILING_ADDRESS);
		listField.add(CandidateInfoStepPortletKeys.EXAM_CODE_AND_TITLE);
		listField.add(CandidateInfoStepPortletKeys.EXAM_DATE);
		listField.add(CandidateInfoStepPortletKeys.ADDITIONAL_NOTES);
		listField.add(CandidateInfoStepPortletKeys.NOTES_1);
		setOrderNotes1("selected-dates-subject-to-availability");
	}

	private void addField2(Set<String> listField) {
		listField.add(CandidateInfoStepPortletKeys.FULL_NAME);
		listField.add(CandidateInfoStepPortletKeys.CONTACT_NO);
		listField.add(CandidateInfoStepPortletKeys.EMAIL_ADDRESS);
		listField.add(CandidateInfoStepPortletKeys.ADDITIONAL_NOTES);
		listField.add(CandidateInfoStepPortletKeys.NOTES_1);
		setOrderNotes1("voucher-code-will-be-emailed");
	}

	public Map<String, AssetCategory> getSingleCategory(List<AssetCategory> categories) {
		try {
			Company company = CompanyLocalServiceUtil.getCompanyByMx(PropsUtil.get(PropsKeys.COMPANY_DEFAULT_WEB_ID));
//			long globalGroupId = company.getGroup().getGroupId();
//			AssetVocabulary topicVocabulary = AssetVocabularyLocalServiceUtil.fetchGroupVocabulary(
//					company.getGroup().getGroupId(), CandidateInfoStepPortletKeys.TOPIC_VOCABULARY);
//			AssetCategory eshopCategory = AssetCategoryLocalServiceUtil.fetchCategory(globalGroupId, 0,
//					CandidateInfoStepPortletKeys.ESHOP_CATEGORY, topicVocabulary.getVocabularyId());
			Map<String, AssetCategory> categoryMap = new HashMap<>();
			for (AssetCategory category : categories) {
				
				if (category.getParentCategoryId() == 0) {
//					return category;
					categoryMap.put(CandidateInfoStepPortletKeys.CATEGORY_PARENT, category);
					categoryMap.put(CandidateInfoStepPortletKeys.CATEGORY_CHILD, null);
				} else {
//					return category.getParentCategory();
					categoryMap.put(CandidateInfoStepPortletKeys.CATEGORY_PARENT, category.getParentCategory());
					categoryMap.put(CandidateInfoStepPortletKeys.CATEGORY_CHILD, category);
				}
			}
			return categoryMap;
		} catch (PortalException e) {
			log.error("error while getting the category : " + e.getMessage());
		}

		return null;
	}

	public Date generateDateTime(String dateTimeStr) {
		try {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
		Date dateTime = sdf.parse(dateTimeStr);
		return dateTime;
		} catch (ParseException e) {
			return new Date();
		}
	}
	
	public Date generateDate(String dateTime) {
		try {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(dateTime);
		return date;
		} catch (ParseException e) {
			return new Date();
		}
	}

	public ArrayList<String> generateTimeRange(String start, String end) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
			SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a");
			Date startTime = sdf.parse(start);
			Date endTime = sdf.parse(end);
			ArrayList<String> times = new ArrayList<>();
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTime(startTime);
			times.add(sdf1.format(calendar.getTime()));
			while (calendar.getTime().before(endTime)) {
				calendar.add(12, 30);
				times.add(sdf1.format(calendar.getTime()));
			}
			return times;
		} catch (Exception e) {
			log.error("Error while generate time range : " + e.getMessage());
			return null;
		}
	}
}
