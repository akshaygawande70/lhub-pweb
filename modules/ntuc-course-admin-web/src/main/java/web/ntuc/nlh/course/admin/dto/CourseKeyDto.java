package web.ntuc.nlh.course.admin.dto;

public class CourseKeyDto {
	private String courseCode;
	private String batchId;
	
	public CourseKeyDto(String courseCode, String batchId) {
		this.courseCode = courseCode;
		this.batchId = batchId;
	}
	
	public String getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CourseKeyDto) {
			CourseKeyDto temp = (CourseKeyDto) obj;
			if(this.courseCode.equals(temp.courseCode) && this.batchId.equals(temp.batchId)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return (this.courseCode.hashCode() + this.batchId.hashCode());
	}
}
