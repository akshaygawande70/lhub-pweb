package web.ntuc.nlh.mediafilter.dto;

public class TopicsDto {

	private String title;
	private String urlMore;
	private String desc;
	private String date;

	public TopicsDto() {
		super();
	}

	public TopicsDto(String title, String urlMore, String desc, String date) {
		super();
		this.title = title;
		this.urlMore = urlMore;
		this.desc = desc;
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "TopicsDto [title=" + title + ", urlMore=" + urlMore + ", desc=" + desc + ", date=" + date + "]";
	}

}
