package web.ntuc.nlh.course.admin.dto;

import java.util.List;

import api.ntuc.nlh.content.engine.SearchResultSummaryDisplayContext;

public class ResultSummaryDisplayContextDto {
	private List<SearchResultSummaryDisplayContext> courses;
	private int count;
	
	public ResultSummaryDisplayContextDto(List<SearchResultSummaryDisplayContext> courses, int count) {
		this.courses = courses;
		this.count = count;
	}
	
	public List<SearchResultSummaryDisplayContext> getCourses() {
		return courses;
	}
	public void setCourses(List<SearchResultSummaryDisplayContext> courses) {
		this.courses = courses;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
