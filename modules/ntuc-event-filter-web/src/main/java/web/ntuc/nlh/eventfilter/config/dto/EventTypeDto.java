package web.ntuc.nlh.eventfilter.config.dto;

public class EventTypeDto {

	private Long typeId;
	private String typeName;

	public EventTypeDto() {
		super();
	}

	public EventTypeDto(Long typeId, String typeName) {
		super();
		this.typeId = typeId;
		this.typeName = typeName;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String toString() {
		return "EventTypeDto [typeId=" + typeId + ", typeName=" + typeName + "]";
	}

}
