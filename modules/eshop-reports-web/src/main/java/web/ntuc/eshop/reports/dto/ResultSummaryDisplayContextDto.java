package web.ntuc.eshop.reports.dto;

import java.util.List;

import api.ntuc.nlh.content.engine.SearchResultSummaryDisplayContext;

public class ResultSummaryDisplayContextDto {
	private List<SearchResultSummaryDisplayContext> result;
	private int count;
	
	public ResultSummaryDisplayContextDto(List<SearchResultSummaryDisplayContext> result, int count) {
		this.result = result;
		this.count = count;
	}
	
	public List<SearchResultSummaryDisplayContext> getResults() {
		return result;
	}
	public void setResults(List<SearchResultSummaryDisplayContext> result) {
		this.result = result;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
