package web.ntuc.eshop.registeredcourse.dto;

public class EvaluationDto {

	private String batchId;
	private String sectionSum;
	private double avg;

	public EvaluationDto() {
		super();
	}

	public EvaluationDto(String batchId, String sectionSum, double avg) {
		super();
		this.batchId = batchId;
		this.sectionSum = sectionSum;
		this.avg = avg;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getSectionSum() {
		return sectionSum;
	}

	public void setSectionSum(String sectionSum) {
		this.sectionSum = sectionSum;
	}

	public double getAvg() {
		return avg;
	}

	public void setAvg(double avg) {
		this.avg = avg;
	}

	@Override
	public String toString() {
		return "EvaluationDto [batchId=" + batchId + ", sectionSum=" + sectionSum + ", avg=" + avg + "]";
	}

}
