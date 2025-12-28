package web.ntuc.nlh.search.global.result.dto;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;

import api.ntuc.common.util.DateUtil;
import api.ntuc.common.util.DocumentMediaUtil;

public class SearchResultDto {
	private static Log log = LogFactoryUtil.getLog(SearchResultDto.class);

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
	private String category;
	private Long[] topicIds;
	private Long[] themeIds;

	public SearchResultDto() {
		super();
	}

	public SearchResultDto(String title, String urlImage, String urlMore, String desc, String structure, String date,
			String typeForm) {
		super();
		this.title = title;
		this.urlImage = urlImage;
		this.urlMore = urlMore;
		this.desc = desc;
		this.structure = structure;
		this.date = date;
		this.typeForm = typeForm;
	}

	public String getTypeForm() {
		return typeForm;
	}

	public void setTypeForm(String typeForm) {
		this.typeForm = typeForm;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStructure() {
		return structure;
	}

	public void setStructure(String structure) {
		this.structure = structure;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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
	
}
