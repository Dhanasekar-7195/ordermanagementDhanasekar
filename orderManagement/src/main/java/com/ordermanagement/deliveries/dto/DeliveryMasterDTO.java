package com.ordermanagement.deliveries.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryMasterDTO {

	private String deliveryId;

	private String invoiceNo;

	private String orderId;

	//private String modifiedAt;
	
    private String createdDate;
	
	private String pickedDate;
	
	private String deliveredDate;

	private String deliveryLocation;

	private String status;

	private String deliveryAddress;

	private String contactPerson;

	private String contactNumber;

	private String comments;

	private String customerId;

	private double total;

	private List<DeliveryMasterItemDTO> items;

}