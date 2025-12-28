package api.ntuc.nlh.content.dto;

public class ConfigListDto {

	private Long topicId;
	private String topicName;
	private Long parentTopicId;
	private Long vocabId;
	private Long groupId;
	private Long companyId;

	public ConfigListDto(Long topicId, String topicName, Long parentTopicId, Long vocabId, Long groupId, Long companyId) {
		super();
		this.topicId = topicId;
		this.topicName = topicName;
		this.parentTopicId = parentTopicId;
		this.vocabId = vocabId;
		this.groupId = groupId;
		this.companyId = companyId;
	}

	public ConfigListDto() {
		super();
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public Long getParentTopicId() {
		return parentTopicId;
	}

	public void setParentTopicId(Long parentTopicId) {
		this.parentTopicId = parentTopicId;
	}

	public Long getVocabId() {
		return vocabId;
	}

	public void setVocabId(Long vocabId) {
		this.vocabId = vocabId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	@Override
	public String toString() {
		return "ListDto [topicId=" + topicId + ", topicName=" + topicName + ", parentTopicId=" + parentTopicId
				+ ", vocabId=" + vocabId + ", groupId=" + groupId + ", companyId=" + companyId + "]";
	}

}
