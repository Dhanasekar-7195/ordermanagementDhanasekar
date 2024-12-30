package com.ordermanagement.deliveries.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryMasterItemDTO {

	private String deliveryMasterItemId;

	private String productName;

	private String category;

	private String subCategory;

	private double price;

	private long qty;

	private double totalAmount;

	private String deliveryId;
	
	private String tax;

	private String discount;

	private double actualAmount;
	
	private String orderMasterItemId;

}