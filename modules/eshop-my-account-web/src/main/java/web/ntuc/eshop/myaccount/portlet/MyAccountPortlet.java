package web.ntuc.eshop.myaccount.portlet;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Address;
import com.liferay.portal.kernel.model.Contact;
import com.liferay.portal.kernel.model.Country;
import com.liferay.portal.kernel.model.Phone;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.AddressLocalServiceUtil;
import com.liferay.portal.kernel.service.CountryServiceUtil;
import com.liferay.portal.kernel.service.PhoneLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import api.ntuc.common.util.CSRFValidationUtil;
import api.ntuc.common.util.RoleUtil;
import web.ntuc.eshop.myaccount.constants.MyAccountPortletKeys;
import web.ntuc.eshop.myaccount.dto.AccountDto;
import web.ntuc.eshop.myaccount.dto.CountryDto;

/**
 * @author muhamadpangestu
 */
@Component(immediate = true, property = { "com.liferay.portlet.display-category=category.eshop",
		"com.liferay.portlet.header-portlet-css=/css/main.css", "com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=MyAccount", "javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + MyAccountPortletKeys.MY_ACCOUNT_PORTLET,
		"javax.portlet.resource-bundle=content.Language", "javax.portlet.version=3.0",
		"javax.portlet.security-role-ref=power-user,user" }, service = Portlet.class)
public class MyAccountPortlet extends MVCPortlet {

	private static Log log = LogFactoryUtil.getLog(MyAccountPortlet.class);
	List<Long> categoryId = new ArrayList<>();

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException, NullPointerException {
		log.info("My Account Portlet render - start");
		try {

			int accType = 0;
			String fullName = "";
			String fullNameCap = "";
			long userId = 0;
			long addressId = 0;
			long phoneId = 0;
			String contactNumber = "";
			String address1 = "";
			String address2 = "";
			String postalCode = "";
			long countryId = 0;
			String companyCode = "";
			String companyName = "";
			String uenNumber = "";
			String nric = "";
			String dob = "";

			String authToken = CSRFValidationUtil.authToken(renderRequest);
			renderRequest.setAttribute("authToken", authToken);

			ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

//			Get Address from User 
			User user = themeDisplay.getUser();
			userId = user.getUserId();
			Contact contact = user.getContact();

//			log.info(Arrays.toString(user.getRoleIds()));

			List<Role> roleList = new ArrayList<Role>();
			for (Long roleId : user.getRoleIds()) {
				Role newRole = RoleServiceUtil.getRole(roleId);
				roleList.add(newRole);
			}

//			Get NRIC from Commerce Account
			int allAccountCount = CommerceAccountLocalServiceUtil.getCommerceAccountsCount();

			CommerceAccount account = null;
			List<CommerceAccount> accList = CommerceAccountLocalServiceUtil.getCommerceAccounts(0, allAccountCount);

			for (CommerceAccount ca : accList) {
				if (ca.getUserId() == userId) {
					account = CommerceAccountLocalServiceUtil.getCommerceAccount(ca.getCommerceAccountId());
//					log.info(account.toString());
				}
			}

			if (RoleUtil.matchByFullRoleName(roleList, "Eshop_Individual_Role")) {
				log.info("Personal Account");
				accType = 1;
				nric = (String) account.getExpandoBridge().getAttribute("NRIC");
			} else {
				log.info("Business Account");
				accType = 2;
				uenNumber = (String) account.getExpandoBridge().getAttribute("UEN Number");
				companyCode = (String) account.getExpandoBridge().getAttribute("Company Code");
				companyName = (String) account.getExpandoBridge().getAttribute("Company Name");
			}

			List<AccountDto> dtoList = new ArrayList<>();

//			Get Addresses
			List<Address> addressList = AddressLocalServiceUtil.getAddresses(user.getCompanyId(),
					Contact.class.getName(), contact.getContactId());

//			Select Primary Address One
			if (!addressList.isEmpty()) {
				for (Address address : addressList) {
					if (address.isPrimary()) {
						addressId = address.getAddressId();
						countryId = address.getCountryId();
						address1 = address.getStreet1();
						address2 = address.getStreet2();
						postalCode = address.getZip();
					}
				}
			}

//			Get Phone from Contact Information
			List<Phone> phoneList = PhoneLocalServiceUtil.getPhones(user.getCompanyId(), Contact.class.getName(),
					contact.getContactId());

//			Select Primary Phone One
			if (!phoneList.isEmpty()) {
				for (Phone phone : phoneList) {
					if (phone.isPrimary()) {
						phoneId = phone.getPhoneId();
						contactNumber = phone.getNumber();
					}
				}
			}

			List<Country> countryList = CountryServiceUtil.getCountries();
			List<CountryDto> countryDtoList = new ArrayList<>();

			for (Country country : countryList) {
				CountryDto dto = new CountryDto();
				dto.setCountryId(country.getCountryId());

				String[] splitName = country.getName().split("-");
				String result = "";

				for (String name : splitName) {
					result += capitalizeString(name) + " ";
				}
				dto.setCountryName(result);
				countryDtoList.add(dto);
			}

//			Mapping to dto
			AccountDto dto = new AccountDto();
//
			if (!user.getMiddleName().isEmpty()) {
				fullName = user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName();
			} else {
				fullName = user.getFirstName() + " " + user.getLastName();
			}

			dto.setUserId(userId);
			dto.setAddressId(addressId);
			dto.setPhoneId(phoneId);

//			From User
			dto.setFullName(fullName);
			fullNameCap = fullName.toUpperCase();
			dto.setEmail(user.getEmailAddress());

			SimpleDateFormat formatter = new SimpleDateFormat("dd / MM / yyyy");
			String birthdayDate = formatter.format(user.getBirthday());
			dto.setBirthDate(birthdayDate);

//			Get User Profile URL
			String portraitURL = user.getPortraitURL(themeDisplay);
			dto.setImgProfile(portraitURL);

//			From Commerce Account
			dto.setNric(nric);
			dto.setCompanyCode(companyCode);
			dto.setCompanyName(companyName);
			dto.setUenNumber(uenNumber);

//			From User Addresses
			dto.setAddress1(address1);
			dto.setAddress2(address2);
			dto.setContactNumber(contactNumber);
			dto.setCountryId(countryId);
			dto.setPostalCode(postalCode);

			dtoList.add(dto);

			renderRequest.setAttribute("type", accType);
			renderRequest.setAttribute("accountList", dtoList);
			renderRequest.setAttribute("fullNameCap", fullNameCap);
			renderRequest.setAttribute("countryList", countryDtoList);

		} catch (Exception e) {
			log.error("Failed when render My Account, error:" + e.getMessage());
		}
		log.info("My Account Portlet render - end");
		super.render(renderRequest, renderResponse);
	}

	public static String capitalizeString(String text) {
		return Character.toUpperCase(text.charAt(0)) + text.substring(1);
	}
}