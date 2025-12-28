package web.ntuc.eshop.invoice.dto;

import com.liferay.commerce.model.CommerceAddress;

import java.math.BigDecimal;
import java.util.Date;


public class OrderDto {

	private int commerceOrderId;
	private String fullName;
	private String phoneNumber;
	private Date orderDate;
	private String paymentMethod;
	private String taxAmount;
	private String totalDiscountAmount;
	private String netPrice;
	private String orderStatus;
	private int paymentStatus;
	private String sku;
	private String examName;
	private String unitPrice;
	private int quantity;
	private String amount;
	private String invoiceNo;
	private CommerceAddress shippingAddress;
	private CommerceAddress billingAddress;

	public OrderDto() {
		super();
	}

	public OrderDto(int commerceOrderId, String fullName, String phoneNumber, Date orderDate, String paymentMethod,
			String taxAmount, String totalDiscountAmount, String netPrice, String status, String sku, String examName, BigDecimal unitPrice, int quantity, BigDecimal amount,
			String invoiceNo, CommerceAddress shippingAddress, CommerceAddress billingAddress) {
		super();
		this.commerceOrderId = commerceOrderId;
		this.fullName = fullName;
		this.phoneNumber = phoneNumber;
		this.orderDate = orderDate;
		this.paymentMethod = paymentMethod;
		this.taxAmount = taxAmount;
		this.totalDiscountAmount = totalDiscountAmount;
		this.netPrice = netPrice;
		this.orderStatus = status;
		this.sku = sku;
		this.examName = examName;
		this.unitPrice = String.valueOf(unitPrice);
		this.quantity = quantity;
		this.amount = String.valueOf(amount);
		this.invoiceNo = invoiceNo;
		this.shippingAddress = shippingAddress;
		this.billingAddress = billingAddress;
	}
	
	public int getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(int paymentStatus) {
		this.paymentStatus = paymentStatus;
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

	public String getNetPrice() {
		return netPrice;
	}

	public void setNetPrice(String netPrice) {
		this.netPrice = netPrice;
	}

	public int getCommerceOrderId() {
		return commerceOrderId;
	}

	public void setCommerceOrderId(int commerceOrderId) {
		this.commerceOrderId = commerceOrderId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String status) {
		this.orderStatus = status;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getExamName() {
		return examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public CommerceAddress getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(CommerceAddress shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public CommerceAddress getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(CommerceAddress billingAddress) {
		this.billingAddress = billingAddress;
	}

	@Override
	public String toString() {
		return "OrderDto [commerceOrderId=" + commerceOrderId + ", fullName=" + fullName + ", phoneNumber="
				+ phoneNumber + ", orderDate=" + orderDate + ", paymentMethod=" + paymentMethod + ", status=" + orderStatus
				+ ", sku=" + sku + ", examName=" + examName + ", unitPrice=" + unitPrice + ", quantity=" + quantity
				+ ", amount=" + amount + ", invoiceNo=" + invoiceNo + ", shippingAddress=" + shippingAddress
				+ ", billingAddress=" + billingAddress + "]";
	}

}
