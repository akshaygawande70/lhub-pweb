package web.ntuc.eshop.register.dto;

public class PersonDto {
	private String nric;
	private String fullName;
	private String dob;
	private String email;
	
	public PersonDto(String nric, String fullName, String dob, String email) {
		this.nric = nric;
		this.fullName = fullName;
		this.dob = dob;
		this.email = email;
	}
	
	public String getNric() {
		return nric;
	}
	public void setNric(String nric) {
		this.nric = nric;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
