package com.ordermanagement.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashBoardDTO {

	private String dashBoardId;

	private String orderId;

	private String createdDate;

	private String deliveredDate;

	private double totalAmount;

	private String status;

	private String paymentStatus;

	private String customerId;
	
private String draftId;

}
