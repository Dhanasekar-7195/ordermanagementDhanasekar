package com.ordermanagement.orders.dto;

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
	
	private String salesOrderType;
	
	private String salesOrganization;
	
	private String distributionChannel;
	
	private String organizationDivision;
	
	private String customerId;
	
	private String orderDate;
	
	private String incotermsClassification;
	
	private String incotermsTransferLocation;
	
	private String contactPerson;
	
	private String deliveryLocation;
	
	private String postalCode;
	
	private String streetName;
	
	private String region;
	
	private String telephoneNumber;
	
	private String salesOrderId;
	
	private double total;
	
	private String status;

	
	//private String mobileNumber;
	
	
	
	


	
	

//	private String invoiceNo;
//
//	private String orderDate;
//
//	private String deliveryLocation;
//
//	private String deliveryAddress;
//
//	private String contactPerson;
//
//	private String contactNumber;
//
//	private String comments;
//
//	private double total;
//
//	private String status;
//
//	private String customerId;
//	
//	private String draftId;

	private List<OrderMasterItemDTO> items;

}
