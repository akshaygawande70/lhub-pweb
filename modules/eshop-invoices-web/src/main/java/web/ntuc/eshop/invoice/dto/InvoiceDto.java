package web.ntuc.eshop.invoice.dto;

public class InvoiceDto {

	private double amount;
	private String paymentURL;
	private String batchId;
	private String trnId;
	private String invoiceNo;
	private String courseTitle;
	private String nricNumber;
	private String courseStartDate;
	private String courseCode;
	private String purchaseDate;
	private String paymentMethod;
	private String invoiceURL;
	private String status;

	private String bano;
	private String subBookingId;
	private String partyId;

	public InvoiceDto() {
		super();
	}

	public InvoiceDto(double amount, String paymentURL, String batchId, String trnId, String invoiceNo,
			String courseTitle, String nricNumber, String courseStartDate, String courseCode, String purchaseDate,
			String paymentMethod, String invoiceURL, String status, String bano, String subBookingId, String partyId) {
		super();
		this.amount = amount;
		this.paymentURL = paymentURL;
		this.batchId = batchId;
		this.trnId = trnId;
		this.invoiceNo = invoiceNo;
		this.courseTitle = courseTitle;
		this.nricNumber = nricNumber;
		this.courseStartDate = courseStartDate;
		this.courseCode = courseCode;
		this.purchaseDate = purchaseDate;
		this.paymentMethod = paymentMethod;
		this.invoiceURL = invoiceURL;
		this.status = status;
		this.bano = bano;
		this.subBookingId = subBookingId;
		this.partyId = partyId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getPaymentURL() {
		return paymentURL;
	}

	public void setPaymentURL(String paymentURL) {
		this.paymentURL = paymentURL;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getTrnId() {
		return trnId;
	}

	public void setTrnId(String trnId) {
		this.trnId = trnId;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public String getNricNumber() {
		return nricNumber;
	}

	public void setNricNumber(String nricNumber) {
		this.nricNumber = nricNumber;
	}

	public String getCourseStartDate() {
		return courseStartDate;
	}

	public void setCourseStartDate(String courseStartDate) {
		this.courseStartDate = courseStartDate;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getInvoiceURL() {
		return invoiceURL;
	}

	public void setInvoiceURL(String invoiceURL) {
		this.invoiceURL = invoiceURL;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBano() {
		return bano;
	}

	public void setBano(String bano) {
		this.bano = bano;
	}

	public String getSubBookingId() {
		return subBookingId;
	}

	public void setSubBookingId(String subBookingId) {
		this.subBookingId = subBookingId;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	@Override
	public String toString() {
		return "InvoiceDto [amount=" + amount + ", paymentURL=" + paymentURL + ", batchId=" + batchId + ", trnId="
				+ trnId + ", invoiceNo=" + invoiceNo + ", courseTitle=" + courseTitle + ", nricNumber=" + nricNumber
				+ ", courseStartDate=" + courseStartDate + ", courseCode=" + courseCode + ", purchaseDate="
				+ purchaseDate + ", paymentMethod=" + paymentMethod + ", invoiceURL=" + invoiceURL + ", status="
				+ status + ", bano=" + bano + ", subBookingId=" + subBookingId + ", partyId=" + partyId + "]";
	}

}
