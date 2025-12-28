package web.ntuc.eshop.myaccount.action;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Address;
import com.liferay.portal.kernel.model.Contact;
import com.liferay.portal.kernel.model.Phone;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.AddressLocalServiceUtil;
import com.liferay.portal.kernel.service.PhoneLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.UploadException;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.PortletCommandUtil;
import web.ntuc.eshop.myaccount.constants.MVCCommandNames;
import web.ntuc.eshop.myaccount.constants.MyAccountPortletKeys;

@Component(immediate = true, property = { "mvc.command.name=" + MVCCommandNames.UPDATE_INFO,
		"javax.portlet.name=" + MyAccountPortletKeys.MY_ACCOUNT_PORTLET }, service = MVCActionCommand.class)
public class SubmitInfoAction extends BaseMVCActionCommand {

	private static Log log = LogFactoryUtil.getLog(SubmitInfoAction.class);

	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		log.info("Update My Account Info Action - Start");

		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		PortletCommandUtil.actionAndResourceCommand(actionRequest, themeDisplay);
		
		UploadPortletRequest uploadRequest = PortalUtil.getUploadPortletRequest(actionRequest);

//		Get Param from Form JSP
		long addressId = ParamUtil.getLong(actionRequest, "addressId");
		long phoneId = ParamUtil.getLong(actionRequest, "phoneId");
		String contactNumber = ParamUtil.getString(actionRequest, "contactNumber");
		String address1 = ParamUtil.getString(actionRequest, "address1");
		String address2 = ParamUtil.getString(actionRequest, "address2");
		String postalCode = ParamUtil.getString(actionRequest, "postalCode");
		long countryId = ParamUtil.getLong(actionRequest, "country");

		try {
//			Get Address from User 
			User user = themeDisplay.getUser();

			File profilePicture = uploadRequest.getFile("profilePicture");
			if (profilePicture.exists()) {
				InputStream inputStream = new FileInputStream(profilePicture);

				byte[] bytes = FileUtil.getBytes(inputStream);
				if ((bytes == null) || (bytes.length == 0)) {
					throw new UploadException();
				}
				UserLocalServiceUtil.updatePortrait(user.getUserId(), bytes);
			}

//			Update Address
			Address address = null;
			ServiceContext serviceContext = new ServiceContext();
			serviceContext.setScopeGroupId(user.getGroupId());			

			if (addressId != 0) {
				address = AddressLocalServiceUtil.getAddress(addressId);
				AddressLocalServiceUtil.updateAddress(addressId, address1, address2, address.getStreet3(),
						address.getCity(), postalCode, address.getRegionId(), countryId, address.getTypeId(),
						address.getMailing(), true);
			} else {
				AddressLocalServiceUtil.addAddress(user.getUserId(), Contact.class.getName(), user.getContactId(),
						address1, address2, "", "default", postalCode, 0, countryId, 11000, false, true, serviceContext);
			}

//			Update Phone
			Phone phone = null;
			if (phoneId != 0) {
				phone = PhoneLocalServiceUtil.getPhone(phoneId);
				PhoneLocalServiceUtil.updatePhone(phoneId, contactNumber, phone.getExtension(), phone.getTypeId(),
						true);
			} else {
				PhoneLocalServiceUtil.addPhone(user.getUserId(), Contact.class.getName(), user.getContactId(),
						contactNumber, "", 11006, true, serviceContext);
			}

			log.info("Update My Account Info Successfully");

		} catch (Exception e) {
			log.error("Update Info found error at : " + e.getMessage());
			e.printStackTrace();
		}
		log.info("Update My Account Info Action - End");
	}
}
