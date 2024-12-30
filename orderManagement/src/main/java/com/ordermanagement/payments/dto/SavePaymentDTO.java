package com.ordermanagement.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavePaymentDTO {

	private String invoice;

	private double grossAmount;

	private double paidAmount;

	private String paymentDate;

	private String paymentMode;

	private String paymentStatus;

	private double payableAmount;
	
	private String userId;

}
