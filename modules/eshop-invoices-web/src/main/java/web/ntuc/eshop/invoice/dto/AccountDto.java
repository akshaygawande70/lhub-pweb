package web.ntuc.eshop.invoice.dto;

import java.util.Date;

public class AccountDto {

	private long userId;
	private long addressId;
	private long phoneId;
	private String nric;
	private String uenNumber;
	private String companyCode;
	private String companyName;
	private String fullName;
	private String email;
	private String birthDate;
	private String imgProfile;
	private String address1;
	private String address2;
	private String contactNumber;
	private String postalCode;
	private long countryId;

	public AccountDto() {
		super();
	}

	public AccountDto(long userId, long addressId, long phoneId, String nric, String uenNumber, String companyCode,
			String fullName, String email, String birthDate, String imgProfile, String address1, String address2,
			String contactNumber, String postalCode, long countryId, String companyName) {
		super();
		this.userId = userId;
		this.addressId = addressId;
		this.phoneId = phoneId;
		this.nric = nric;
		this.uenNumber = uenNumber;
		this.companyCode = companyCode;
		this.fullName = fullName;
		this.email = email;
		this.birthDate = birthDate;
		this.imgProfile = imgProfile;
		this.address1 = address1;
		this.address2 = address2;
		this.contactNumber = contactNumber;
		this.postalCode = postalCode;
		this.countryId = countryId;
		this.companyName = companyName;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getAddressId() {
		return addressId;
	}

	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}

	public long getPhoneId() {
		return phoneId;
	}

	public void setPhoneId(long phoneId) {
		this.phoneId = phoneId;
	}

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public String getUenNumber() {
		return uenNumber;
	}

	public void setUenNumber(String uenNumber) {
		this.uenNumber = uenNumber;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getImgProfile() {
		return imgProfile;
	}

	public void setImgProfile(String imgProfile) {
		this.imgProfile = imgProfile;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public long getCountryId() {
		return countryId;
	}

	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override
	public String toString() {
		return "AccountDto [userId=" + userId + ", addressId=" + addressId + ", phoneId=" + phoneId + ", nric=" + nric
				+ ", uenNumber=" + uenNumber + ", companyCode=" + companyCode + ", companyName=" + companyName
				+ ", fullName=" + fullName + ", email=" + email + ", birthDate=" + birthDate + ", imgProfile="
				+ imgProfile + ", address1=" + address1 + ", address2=" + address2 + ", contactNumber=" + contactNumber
				+ ", postalCode=" + postalCode + ", countryId=" + countryId + "]";
	}

}
