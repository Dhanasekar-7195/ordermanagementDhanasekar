package com.ordermanagement.customers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerMasterDTO {

	private String customerId;

	private String customerName;

	private String contactNo;

	private String email;

	private String deliveryLocation;

	private String billingAddress;

	private String shippingAddress1;
	
	private String shippingAddress2;

	private double returnCredit;

}
