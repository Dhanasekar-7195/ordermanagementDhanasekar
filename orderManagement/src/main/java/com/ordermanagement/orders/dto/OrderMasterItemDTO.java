package com.ordermanagement.orders.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMasterItemDTO {
	
	private String orderMasterItemId;
		
	private String productName;
		
	private String category;
	
	private String subCategory;
	
	private double price;
	
	private long qty;
	
	private double totalAmount;
		
	private String orderId;

}
