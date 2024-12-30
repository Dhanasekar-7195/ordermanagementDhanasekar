package com.ordermanagement.returns.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnMasterItemDTO {

	private String returnMasterItemId;

	private String productName;

	private String category;

	private String subCategory;

	private double price;

	private long qty;

	private long returnQty;

	private double invoiceAmount;

	private double creditRequest;

	private String imageId;

	private String returnId;

	private String tax;

	private String discount;

}
