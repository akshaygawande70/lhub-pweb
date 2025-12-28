package web.ntuc.nlh.search.result.dto;

import api.ntuc.common.dto.ResultDto;

public class SearchResultDto extends ResultDto{
	
	private Double originalPrice;

	public SearchResultDto(String title, String urlImage, String urlMore, String desc, String structure, String date,
			String typeForm) {
		super(title, urlImage, urlMore, desc, structure, date, typeForm);
	}
	
	public SearchResultDto() {
		super();
	}

	public Double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Double originalPrice) {
		this.originalPrice = originalPrice;
	}

}
