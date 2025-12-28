package web.ntuc.eshop.invoice.dto;

import java.util.List;

public class ResultOrderListDto {

	private List<OrderDto> orderDtos;
	private String invoiceNo;

	public ResultOrderListDto(List<OrderDto> orderDtos, String invoiceNo) {
		this.orderDtos = orderDtos;
		this.invoiceNo = invoiceNo;
	}

	public List<OrderDto> getOrderDtos() {
		return orderDtos;
	}

	public void setOrderDtos(List<OrderDto> orderDtos) {
		this.orderDtos = orderDtos;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

}
