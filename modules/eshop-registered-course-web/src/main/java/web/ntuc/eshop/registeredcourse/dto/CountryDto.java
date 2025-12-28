package web.ntuc.eshop.registeredcourse.dto;

public class CountryDto {

	long countryId = 0;
	String countryName = "";

	public CountryDto() {
		super();
	}

	public CountryDto(long countryId, String countryName) {
		super();
		this.countryId = countryId;
		this.countryName = countryName;
	}

	public long getCountryId() {
		return countryId;
	}

	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	@Override
	public String toString() {
		return "CountryDto [countryId=" + countryId + ", countryName=" + countryName + "]";
	}

}
