package web.ntuc.nlh.course.admin.dto;

import api.ntuc.common.dto.ResultDto;

public class SearchResultDto extends ResultDto{

	public SearchResultDto(String title, String urlImage, String urlMore, String desc, String structure, String date,
			String typeForm) {
		super(title, urlImage, urlMore, desc, structure, date, typeForm);
	}
	
	public SearchResultDto() {
		super();
	}

	private String courseCode;

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	
}
