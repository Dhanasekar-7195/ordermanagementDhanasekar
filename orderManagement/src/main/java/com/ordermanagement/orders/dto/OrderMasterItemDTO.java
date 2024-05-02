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
	
	private long sNo;
	
	private String description;
	
	private String part;
	
	private String uom;
	
	private long qty;
	
	private double rate;
	
	private double amount;
		
	private String orderId;

}
