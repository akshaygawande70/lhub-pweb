package web.ntuc.eshop.registeredcourse.dto;

import java.util.Date;
import java.util.List;

public class RegisteredCourseDto {

	private String nricNumber;
	private String courseTitle;
	private String courseCode;
	private String courseLevel;
	private String courseCategory;
	private String courseType;
	private Date courseStartDate;
	private Date courseEndDate;
	private String batchId;
	private String sessionHrs;
	private String funded;
	private String patternCode;
	private String courseExamFlag;
	private String subBookingId;
	private List<TraineeDto> traineeList;

	public RegisteredCourseDto() {
		super();
	}

	public RegisteredCourseDto(String nricNumber, String courseTitle, String courseCode, String courseLevel,
			String courseCategory, String courseType, Date courseStartDate, Date courseEndDate, String batchId,
			String sessionHrs, String funded, String patternCode, String courseExamFlag, List<TraineeDto> traineeList) {
		super();
		this.nricNumber = nricNumber;
		this.courseTitle = courseTitle;
		this.courseCode = courseCode;
		this.courseLevel = courseLevel;
		this.courseCategory = courseCategory;
		this.courseType = courseType;
		this.courseStartDate = courseStartDate;
		this.courseEndDate = courseEndDate;
		this.batchId = batchId;
		this.sessionHrs = sessionHrs;
		this.funded = funded;
		this.patternCode = patternCode;
		this.courseExamFlag = courseExamFlag;
		this.traineeList = traineeList;
	}

	public String getNricNumber() {
		return nricNumber;
	}

	public void setNricNumber(String nricNumber) {
		this.nricNumber = nricNumber;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseLevel() {
		return courseLevel;
	}

	public void setCourseLevel(String courseLevel) {
		this.courseLevel = courseLevel;
	}

	public String getCourseCategory() {
		return courseCategory;
	}

	public void setCourseCategory(String courseCategory) {
		this.courseCategory = courseCategory;
	}

	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}

	public Date getCourseStartDate() {
		return courseStartDate;
	}

	public void setCourseStartDate(Date courseStartDate) {
		this.courseStartDate = courseStartDate;
	}

	public Date getCourseEndDate() {
		return courseEndDate;
	}

	public void setCourseEndDate(Date courseEndDate) {
		this.courseEndDate = courseEndDate;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getSessionHrs() {
		return sessionHrs;
	}

	public void setSessionHrs(String sessionHrs) {
		this.sessionHrs = sessionHrs;
	}

	public String getFunded() {
		return funded;
	}

	public void setFunded(String funded) {
		this.funded = funded;
	}

	public String getPatternCode() {
		return patternCode;
	}

	public void setPatternCode(String patternCode) {
		this.patternCode = patternCode;
	}

	public String getCourseExamFlag() {
		return courseExamFlag;
	}

	public void setCourseExamFlag(String courseExamFlag) {
		this.courseExamFlag = courseExamFlag;
	}

	public String getSubBookingId() {
		return subBookingId;
	}

	public void setSubBookingId(String subBookingId) {
		this.subBookingId = subBookingId;
	}

	public List<TraineeDto> getTraineeList() {
		return traineeList;
	}

	public void setTraineeList(List<TraineeDto> traineeList) {
		this.traineeList = traineeList;
	}

	@Override
	public String toString() {
		return "RegisteredCourseDto [nricNumber=" + nricNumber + ", courseTitle=" + courseTitle + ", courseCode="
				+ courseCode + ", courseLevel=" + courseLevel + ", courseCategory=" + courseCategory + ", courseType="
				+ courseType + ", courseStartDate=" + courseStartDate + ", courseEndDate=" + courseEndDate
				+ ", batchId=" + batchId + ", sessionHrs=" + sessionHrs + ", funded=" + funded + ", patternCode="
				+ patternCode + ", courseExamFlag=" + courseExamFlag + ", subBookingId=" + subBookingId
				+ ", traineeList=" + traineeList + "]";
	}

}
