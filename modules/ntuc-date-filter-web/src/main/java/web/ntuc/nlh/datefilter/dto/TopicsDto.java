package web.ntuc.nlh.datefilter.dto;

public class TopicsDto {

	private String title;
	private String subtitle;
	private String urlImage;
	private String urlMore;
	private String desc;
	private String structure;
	private String date;
	private String typeForm;
	private String status;

	public TopicsDto() {
		super();
	}

	public TopicsDto(String title, String subtitle, String urlImage, String urlMore, String desc, String structure,
			String date, String typeForm, String status) {
		super();
		this.title = title;
		this.subtitle = subtitle;
		this.urlImage = urlImage;
		this.urlMore = urlMore;
		this.desc = desc;
		this.structure = structure;
		this.date = date;
		this.typeForm = typeForm;
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTypeForm() {
		return typeForm;
	}

	public void setTypeForm(String typeForm) {
		this.typeForm = typeForm;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "TopicsDto [title=" + title + ", subtitle=" + subtitle + ", urlImage=" + urlImage + ", urlMore="
				+ urlMore + ", desc=" + desc + ", structure=" + structure + ", date=" + date + ", typeForm=" + typeForm
				+ ", status=" + status + "]";
	}

}
