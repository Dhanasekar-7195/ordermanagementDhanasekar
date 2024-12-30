package com.ordermanagement.invoices.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceMasterDTO {
	
	private String invoiceId;
	
	private String orderId;
 
	private String invoiceNo;
 
	private String orderDate;
 
	private String deliveryLocation;
 
	private String deliveryAddress;
 
	private String contactPerson;
 
	private String contactNumber;
 
	private String comments;
 
	private double total;
 
	private String status;
 
	private String paymentId;
 
	private String deliveryId;
	private String draftId;
 
	private double payableAmount;
 
	private double paidAmount;
 
	private String deliveredDate;
 
	private String paymentDate;
 
	private String paymentMode;
 
	private String paymentStatus;
 
	private String customerId;
	
	private List<InvoiceMasterItemDTO> items;

}
