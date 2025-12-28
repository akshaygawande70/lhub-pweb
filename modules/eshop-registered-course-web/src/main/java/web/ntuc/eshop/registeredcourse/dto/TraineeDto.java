package web.ntuc.eshop.registeredcourse.dto;

import java.util.List;

public class TraineeDto {

	private String batchId;
	private String traineeName;
	private String traineeEmail;
	private List<AttendanceDto> attendanceList;
	private List<ExamResultDto> examList;

	public TraineeDto() {
		super();
	}

	public TraineeDto(String batchId, String traineeName, String traineeEmail, List<AttendanceDto> attendanceList,
			List<ExamResultDto> examList) {
		super();
		this.batchId = batchId;
		this.traineeName = traineeName;
		this.traineeEmail = traineeEmail;
		this.attendanceList = attendanceList;
		this.examList = examList;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
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

	public List<AttendanceDto> getAttendanceList() {
		return attendanceList;
	}

	public void setAttendanceList(List<AttendanceDto> attendanceList) {
		this.attendanceList = attendanceList;
	}

	public List<ExamResultDto> getExamList() {
		return examList;
	}

	public void setExamList(List<ExamResultDto> examList) {
		this.examList = examList;
	}

	@Override
	public String toString() {
		return "TraineeDto [batchId=" + batchId + ", traineeName=" + traineeName + ", traineeEmail=" + traineeEmail
				+ ", attendanceList=" + attendanceList + ", examList=" + examList + "]";
	}

}
