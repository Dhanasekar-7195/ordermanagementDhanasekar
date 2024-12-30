package com.ordermanagement.returns.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnMasterDTO {

	private String returnId;

	private String returnDate;

	private String invoiceNumber;

	private String reason;

	private String contactPerson;

	private String email;

	private double returnCredit;

	private String notes;

	private List<ReturnMasterItemDTO> items;

	private String customerId;
	
    private String orderId;
	
	private String contactNumber;

	private String shippingAddress;
	
	private String userId;

}
