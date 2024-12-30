package com.ordermanagement.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMasterDTO {

	private String paymentId;

	private String orderId;

	private String deliveryId;

	private double grossAmount;

	private double payableAmount;

	private double paidAmount;

	private String deliveredDate;

	private String paymentDate;

	private String paymentMode;

	private String paymentStatus;

	private String customerName;

	private double exactPaidAmount;

	private String customerId;

	private double returnCredit;
	
	private double returnCreditUsed;


}
