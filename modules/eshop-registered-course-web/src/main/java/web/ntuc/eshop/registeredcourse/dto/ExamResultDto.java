package web.ntuc.eshop.registeredcourse.dto;

import java.io.Serializable;
import java.util.Date;

public class ExamResultDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3180015932026319667L;
	private String courseCode;
	private String courseTitle;
	private String batchId;
	private Date courseStartDate;
	private Date courseEndDate;
	private String examName;
	private Date examDate;
	private String examResult;

	public ExamResultDto() {
		super();
	}

	public ExamResultDto(String courseCode, String courseTitle, String batchId, Date courseStartDate,
			Date courseEndDate, String examName, Date examDate, String examResult) {
		super();
		this.courseCode = courseCode;
		this.courseTitle = courseTitle;
		this.batchId = batchId;
		this.courseStartDate = courseStartDate;
		this.courseEndDate = courseEndDate;
		this.examName = examName;
		this.examDate = examDate;
		this.examResult = examResult;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
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

	public String getExamName() {
		return examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

	public Date getExamDate() {
		return examDate;
	}

	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}

	public String getExamResult() {
		return examResult;
	}

	public void setExamResult(String examResult) {
		this.examResult = examResult;
	}

	@Override
	public String toString() {
		return "ExamResultDto [courseCode=" + courseCode + ", courseTitle=" + courseTitle + ", batchId=" + batchId
				+ ", courseStartDate=" + courseStartDate + ", courseEndDate=" + courseEndDate + ", examName=" + examName
				+ ", examDate=" + examDate + ", examResult=" + examResult + "]";
	}

}
