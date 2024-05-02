package com.ordermanagement.orders.dto;

import java.sql.Date;
import java.util.List;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMasterDTO {
	
	private String orderId;
	
	private String invoiceNo;
	
	private String supplier;
	
	private String type;
	
	private String glGroup;
	
	private String orderDate;
	
	private String name;
	
	private String addressLine1;
	
	private String addressLine2;
	
	private String phone;
	
	private String fax;
	
	private String tel;
	
	private String comments;
	
	private double creditLimit;
	
	private double outstanding;
	
	private double available;
	
	private double Amount;
	
	private double tax;

	private double totalAmount;
	
	private String notes;

	private List<OrderMasterItemDTO> items;

}
