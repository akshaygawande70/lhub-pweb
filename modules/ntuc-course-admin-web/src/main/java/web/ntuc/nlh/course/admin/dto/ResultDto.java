package web.ntuc.nlh.course.admin.dto;

import java.util.List;

public class ResultDto {
	private List<SearchResultDto> courses;
	private int count;
	
	public ResultDto(List<SearchResultDto> courses, int count) {
		this.courses = courses;
		this.count = count;
	}
	
	public List<SearchResultDto> getCourses() {
		return courses;
	}
	public void setCourses(List<SearchResultDto> courses) {
		this.courses = courses;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
