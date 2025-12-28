package web.ntuc.eshop.reports.dto;

public class ListCategoryDto {
	private String category;
	
	public ListCategoryDto() {
		super();
	}
	
	public ListCategoryDto(String category) {
		super();
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
