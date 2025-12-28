package api.ntuc.nlh.content.dto;

public class FilterCoursesDto {

	private Long filterId;
	private String filterName;
	private String category;

	public FilterCoursesDto() {
		super();
	}

	public FilterCoursesDto(Long filterId, String filterName, String category) {
		super();
		this.filterId = filterId;
		this.filterName = filterName;
		this.category = category;
	}

	public Long getFilterId() {
		return filterId;
	}

	public void setFilterId(Long filterId) {
		this.filterId = filterId;
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "FilterDto [filterId=" + filterId + ", filterName=" + filterName + ", category=" + category + "]";
	}
	
	@Override
	public boolean equals(Object arg0) {
		if(arg0 instanceof FilterCoursesDto) {
			FilterCoursesDto temp = (FilterCoursesDto) arg0;
			if(this.filterId == temp.filterId && this.filterName.equals(temp.getFilterName()) && this.category.equals(temp.getCategory())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return (this.filterId.hashCode() + this.filterName.hashCode() + this.category.hashCode());
	}

}
