package web.ntuc.eshop.reports.dto;

import java.util.Date;

public class SalesDto {

	private long invoiceId;
	private Date invoiceDate;
	private String invoiceNo;
	private String item;
	private String SKU;
	private int orderStatus;
	private int quantity;
	private String unitPrice;
	private String netPrice;
	private String taxAmount;
	private String totalDiscountAmount;
	private String total;
	private String stripeId;
	private String stripeLink;

	public SalesDto() {
		super();
	}

	public SalesDto(long invoiceId, Date invoiceDate, String invoiceNo, String item, String sKU, 
			int orderStatus, int quantity,
			String unitPrice, String total, String stripeId, String stripeLink) {
		super();
		this.invoiceId = invoiceId;
		this.invoiceDate = invoiceDate;
		this.invoiceNo = invoiceNo;
		this.item = item;
		SKU = sKU;
		this.orderStatus = orderStatus;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.total = total;
		this.stripeId = stripeId;
		this.stripeLink = stripeLink;
	}
   
	
	public String getNetPrice() {
		return netPrice;
	}

	public void setNetPrice(String netPrice) {
		this.netPrice = netPrice;
	}

	public String getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(String taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getTotalDiscountAmount() {
		return totalDiscountAmount;
	}

	public void setTotalDiscountAmount(String totalDiscountAmount) {
		this.totalDiscountAmount = totalDiscountAmount;
	}

	public String getStripeId() {
		return stripeId;
	}

	public void setStripeId(String stripeId) {
		this.stripeId = stripeId;
	}

	public String getStripeLink() {
		return stripeLink;
	}

	public void setStripeLink(String stripeLink) {
		this.stripeLink = stripeLink;
	}

	public long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getSKU() {
		return SKU;
	}

	public void setSKU(String sKU) {
		SKU = sKU;
	}
	
	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "SalesDto [invoiceId=" + invoiceId + ", invoiceDate=" + invoiceDate + ", invoiceNo=" + invoiceNo
				+ ", item=" + item + ", SKU=" + SKU + ", quantity=" + quantity + ", unitPrice=" + unitPrice + ", total="
				+ total + "]";
	}

}
