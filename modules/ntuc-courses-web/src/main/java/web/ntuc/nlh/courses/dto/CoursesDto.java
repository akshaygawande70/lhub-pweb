package web.ntuc.nlh.courses.dto;

import java.util.Arrays;

public class CoursesDto {

	private String title;
	private String urlImage;
	private String urlMore;
	private String desc;
	private String structure;
	private String date;
	private String typeForm;
	private String status;
	private String popular;
	private Double price;
	private String funded;
	private String whatInIt;
	private Long[] topicIds;
	private Long[] themeIds;

	public CoursesDto() {
		super();
	}

	public CoursesDto(String title, String urlImage, String urlMore, String desc, String structure, String date,
			String typeForm, String status, String popular, Double price, String funded, String whatInIt,
			Long[] topicIds, Long[] themeIds) {
		super();
		this.title = title;
		this.urlImage = urlImage;
		this.urlMore = urlMore;
		this.desc = desc;
		this.structure = structure;
		this.date = date;
		this.typeForm = typeForm;
		this.status = status;
		this.popular = popular;
		this.price = price;
		this.funded = funded;
		this.whatInIt = whatInIt;
		this.topicIds = topicIds;
		this.themeIds = themeIds;
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

	public String getPopular() {
		return popular;
	}

	public void setPopular(String popular) {
		this.popular = popular;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getFunded() {
		return funded;
	}

	public void setFunded(String funded) {
		this.funded = funded;
	}

	public String getWhatInIt() {
		return whatInIt;
	}

	public void setWhatInIt(String whatInIt) {
		this.whatInIt = whatInIt;
	}

	public Long[] getTopicIds() {
		return topicIds;
	}

	public void setTopicIds(Long[] topicIds) {
		this.topicIds = topicIds;
	}

	public Long[] getThemeIds() {
		return themeIds;
	}

	public void setThemeIds(Long[] themeIds) {
		this.themeIds = themeIds;
	}

	@Override
	public String toString() {
		return "CoursesDto [title=" + title + ", urlImage=" + urlImage + ", urlMore=" + urlMore + ", desc=" + desc
				+ ", structure=" + structure + ", date=" + date + ", typeForm=" + typeForm + ", status=" + status
				+ ", popular=" + popular + ", price=" + price + ", funded=" + funded + ", whatInIt=" + whatInIt
				+ ", topicIds=" + Arrays.toString(topicIds) + ", themeIds=" + Arrays.toString(themeIds) + "]";
	}

}
