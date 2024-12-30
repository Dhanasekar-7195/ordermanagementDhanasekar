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
	
	private String product;
	
	private String categoryName;
	
	private String productType;
	
	private String baseUnit;
	
	private String productDescription;
	
	private double standardPrice;
	
	private String currency;

//	private String productName;
//
//	private String category;
//
//	private String subCategory;
//
//	private double price;
//
	private String qty;
//
	private double totalAmount;

	private String orderId;

//	private String tax;
//
//	private String discount;
//
//	private double actualAmount;

}
