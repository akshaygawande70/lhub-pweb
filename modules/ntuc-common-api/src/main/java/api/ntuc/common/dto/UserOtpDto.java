package api.ntuc.common.dto;

import java.io.Serializable;

public class UserOtpDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long creatorUserId;
	private boolean autoPassword;
	private String password1;
	private String password2;
	private boolean autoScreenName;
	private String screenName;
	private String emailAddress;
	private String languageId;
	private String fullName;
	private String firstName;
	private String lastName;
	private String middleName;
	private  long prefixId;
	private long suffixId;
	private boolean male;
	private int birthdayMonth;
	private int birthdayDay;
	private int birthdayYear;
	private String jobTitle;
	private long[] groupIds;
	private long[] organizationIds;
	private long[] roleIds;
	private long[] userGroupIds;
	private boolean sendEmail;
	private int type;
	private String nric;
	private String companyName;
	private String companyCode;
	private String uenNumber;
	private String preferredName;
	
	public UserOtpDto(long creatorUserId, boolean autoPassword, String password1, String password2,
			boolean autoScreenName, String screenName, String emailAddress, String languageId, String fullName, String firstName, String lastName, String middleName,
			long prefixId, long suffixId, boolean male, int birthdayMonth, int birthdayDay, int birthdayYear,
			String jobTitle, long[] groupIds, long[] organizationIds, long[] roleIds, long[] userGroupIds,
			boolean sendEmail, int type, String nric, String companyName, String companyCode, String uenNumber) {
		this.creatorUserId = creatorUserId;
		this.autoPassword = autoPassword;
		this.password1 = password1;
		this.password2 = password2;
		this.autoScreenName = autoScreenName;
		this.screenName = screenName;
		this.emailAddress = emailAddress;
		this.languageId = languageId;
		this.fullName = fullName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleName = middleName;
		this.prefixId = prefixId;
		this.suffixId = suffixId;
		this.male = male;
		this.birthdayMonth = birthdayMonth;
		this.birthdayDay = birthdayDay;
		this.birthdayYear = birthdayYear;
		this.jobTitle = jobTitle;
		this.groupIds = groupIds;
		this.organizationIds = organizationIds;
		this.roleIds = roleIds;
		this.userGroupIds = userGroupIds;
		this.sendEmail = sendEmail;
		this.type = type;
		this.nric = nric;
		this.companyName = companyName;
		this.companyCode = companyCode;
		this.uenNumber = uenNumber;
		this.preferredName = fullName;
	}
	
	
	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public long getCreatorUserId() {
		return creatorUserId;
	}
	public void setCreatorUserId(long creatorUserId) {
		this.creatorUserId = creatorUserId;
	}
	public boolean isAutoPassword() {
		return autoPassword;
	}
	public void setAutoPassword(boolean autoPassword) {
		this.autoPassword = autoPassword;
	}
	public String getPassword1() {
		return password1;
	}
	public void setPassword1(String password1) {
		this.password1 = password1;
	}
	public String getPassword2() {
		return password2;
	}
	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	public boolean isAutoScreenName() {
		return autoScreenName;
	}
	public void setAutoScreenName(boolean autoScreenName) {
		this.autoScreenName = autoScreenName;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getLanguageId() {
		return languageId;
	}
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public long getPrefixId() {
		return prefixId;
	}
	public void setPrefixId(long prefixId) {
		this.prefixId = prefixId;
	}
	public long getSuffixId() {
		return suffixId;
	}
	public void setSuffixId(long suffixId) {
		this.suffixId = suffixId;
	}
	public boolean isMale() {
		return male;
	}
	public void setMale(boolean male) {
		this.male = male;
	}
	public int getBirthdayMonth() {
		return birthdayMonth;
	}
	public void setBirthdayMonth(int birthdayMonth) {
		this.birthdayMonth = birthdayMonth;
	}
	public int getBirthdayDay() {
		return birthdayDay;
	}
	public void setBirthdayDay(int birthdayDay) {
		this.birthdayDay = birthdayDay;
	}
	public int getBirthdayYear() {
		return birthdayYear;
	}
	public void setBirthdayYear(int birthdayYear) {
		this.birthdayYear = birthdayYear;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public long[] getGroupIds() {
		return groupIds;
	}
	public void setGroupIds(long[] groupIds) {
		this.groupIds = groupIds;
	}
	public long[] getOrganizationIds() {
		return organizationIds;
	}
	public void setOrganizationIds(long[] organizationIds) {
		this.organizationIds = organizationIds;
	}
	public long[] getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(long[] roleIds) {
		this.roleIds = roleIds;
	}
	public long[] getUserGroupIds() {
		return userGroupIds;
	}
	public void setUserGroupIds(long[] userGroupIds) {
		this.userGroupIds = userGroupIds;
	}
	public boolean isSendEmail() {
		return sendEmail;
	}
	public void setSendEmail(boolean sendEmail) {
		this.sendEmail = sendEmail;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getNric() {
		return nric;
	}
	public void setNric(String nric) {
		this.nric = nric;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getUenNumber() {
		return uenNumber;
	}
	public void setUenNumber(String uenNumber) {
		this.uenNumber = uenNumber;
	}
	public String getPreferredName() {
		return preferredName;
	}
	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}
}
