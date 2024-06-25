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
	
	private String orderDate;
	
	private String deliveryLocation;
	
	private String deliveryAddress;
	
	private String contactPerson;
	
	private String contactNumber;
	
	private String comments;
	
	private double total;
	
	private List<OrderMasterItemDTO> items;

}
