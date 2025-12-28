package web.ntuc.eshop.registeredcourse.dto;

import java.util.List;

public class CorporateAttendanceDto {
	private String courseCode;
	private String batchId;
	private String companyCode;
	private String courseTitle;
	private String courseStartDate;
	private String courseEndDate;
	private int session;
	private String attendanceHour;
	private String attendancePercentage;
	private String isAttended;
	private String makeBatch;
	private String attendanceDateTime;
	private String traineeName;
	private String traineeEmail;

	public CorporateAttendanceDto() {
		super();
	}

	public CorporateAttendanceDto(String courseCode, String batchId, String companyCode, String courseTitle,
			String courseStartDate, String courseEndDate, int session, String attendanceHour,
			String attendancePercentage, String isAttended, String makeBatch, String attendanceDateTime,
			String traineeName, String traineeEmail) {
		super();
		this.courseCode = courseCode;
		this.batchId = batchId;
		this.companyCode = companyCode;
		this.courseTitle = courseTitle;
		this.courseStartDate = courseStartDate;
		this.courseEndDate = courseEndDate;
		this.session = session;
		this.attendanceHour = attendanceHour;
		this.attendancePercentage = attendancePercentage;
		this.isAttended = isAttended;
		this.makeBatch = makeBatch;
		this.attendanceDateTime = attendanceDateTime;
		this.traineeName = traineeName;
		this.traineeEmail = traineeEmail;
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

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public String getCourseStartDate() {
		return courseStartDate;
	}

	public void setCourseStartDate(String courseStartDate) {
		this.courseStartDate = courseStartDate;
	}

	public String getCourseEndDate() {
		return courseEndDate;
	}

	public void setCourseEndDate(String courseEndDate) {
		this.courseEndDate = courseEndDate;
	}

	public int getSession() {
		return session;
	}

	public void setSession(int session) {
		this.session = session;
	}

	public String getAttendanceHour() {
		return attendanceHour;
	}

	public void setAttendanceHour(String attendanceHour) {
		this.attendanceHour = attendanceHour;
	}

	public String getAttendancePercentage() {
		return attendancePercentage;
	}

	public void setAttendancePercentage(String attendancePercentage) {
		this.attendancePercentage = attendancePercentage;
	}

	public String getIsAttended() {
		return isAttended;
	}

	public void setIsAttended(String isAttended) {
		this.isAttended = isAttended;
	}

	public String getMakeBatch() {
		return makeBatch;
	}

	public void setMakeBatch(String makeBatch) {
		this.makeBatch = makeBatch;
	}

	public String getAttendanceDateTime() {
		return attendanceDateTime;
	}

	public void setAttendanceDateTime(String attendanceDateTime) {
		this.attendanceDateTime = attendanceDateTime;
	}

	public String getTraineeName() {
		return traineeName;
	}

	public void setTraineeName(String traineeName) {
		this.traineeName = traineeName;
	}

	public String getTraineeEmail() {
		return traineeEmail;
	}

	public void setTraineeEmail(String traineeEmail) {
		this.traineeEmail = traineeEmail;
	}

	@Override
	public String toString() {
		return "CorporateAttendanceDto [courseCode=" + courseCode + ", batchId=" + batchId + ", companyCode="
				+ companyCode + ", courseTitle=" + courseTitle + ", courseStartDate=" + courseStartDate
				+ ", courseEndDate=" + courseEndDate + ", session=" + session + ", attendanceHour=" + attendanceHour
				+ ", attendancePercentage=" + attendancePercentage + ", isAttended=" + isAttended + ", makeBatch="
				+ makeBatch + ", attendanceDateTime=" + attendanceDateTime + ", traineeName=" + traineeName
				+ ", traineeEmail=" + traineeEmail + "]";
	}

}
