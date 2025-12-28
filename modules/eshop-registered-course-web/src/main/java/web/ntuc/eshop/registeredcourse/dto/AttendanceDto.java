package web.ntuc.eshop.registeredcourse.dto;

import java.util.Date;

public class AttendanceDto {

	private String courseCode;
	private String batchId;
	private String courseTitle;
	private Date courseStartDate;
	private Date courseEndDate;
	private int session;
	private String attendanceHour;
	private String attendancePercentage;
	private String isAttended;
	private String makeBatch;
	private String attendanceDateTime;
	private String companyCode;

	public AttendanceDto() {
		super();
	}

	public AttendanceDto(String courseCode, String batchId, String courseTitle, Date courseStartDate,
			Date courseEndDate, int session, String attendanceHour, String attendancePercentage, String isAttended,
			String makeBatch, String attendanceDateTime, String companyCode) {
		super();
		this.session = session;
		this.attendanceHour = attendanceHour;
		this.attendancePercentage = attendancePercentage;
		this.isAttended = isAttended;
		this.makeBatch = makeBatch;
		this.attendanceDateTime = attendanceDateTime;
		this.courseCode = courseCode;
		this.batchId = batchId;
		this.courseTitle = courseTitle;
		this.courseStartDate = courseStartDate;
		this.courseEndDate = courseEndDate;
		this.companyCode = companyCode;
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

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
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

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	@Override
	public String toString() {
		return "AttendanceDto [courseCode=" + courseCode + ", batchId=" + batchId + ", courseTitle=" + courseTitle
				+ ", courseStartDate=" + courseStartDate + ", courseEndDate=" + courseEndDate + ", session=" + session
				+ ", attendanceHour=" + attendanceHour + ", attendancePercentage=" + attendancePercentage
				+ ", isAttended=" + isAttended + ", makeBatch=" + makeBatch + ", attendanceDateTime="
				+ attendanceDateTime + ", companyCode=" + companyCode + "]";
	}

}
