package web.ntuc.nlh.eventfilter.dto;

public class EventDto {

	private String title;
	private String urlImage;
	private String urlMore;
	private String desc;
	private String structure;
	private String startDate;
	private String endDate;
	private String status;
	private String eventType;
	private Long categoryId;

	public EventDto() {
		super();
	}

	public EventDto(String title, String urlImage, String urlMore, String desc, String structure, String startDate,
			String endDate, String status, String eventType, Long categoryId) {
		super();
		this.title = title;
		this.urlImage = urlImage;
		this.urlMore = urlMore;
		this.desc = desc;
		this.structure = structure;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.eventType = eventType;
		this.categoryId = categoryId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	public String getUrlMore() {
		return urlMore;
	}

	public void setUrlMore(String urlMore) {
		this.urlMore = urlMore;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getStructure() {
		return structure;
	}

	public void setStructure(String structure) {
		this.structure = structure;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return "EventDto [title=" + title + ", urlImage=" + urlImage + ", urlMore=" + urlMore + ", desc=" + desc
				+ ", structure=" + structure + ", startDate=" + startDate + ", endDate=" + endDate + ", status="
				+ status + ", eventType=" + eventType + ", categoryId=" + categoryId + "]";
	}

}
