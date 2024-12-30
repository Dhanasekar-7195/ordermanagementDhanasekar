package com.ordermanagement.invoices.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceMasterItemDTO {
	
	private String invoiceMasterItemId;
	
	
		private String orderMasterItemId;
	 
		private String productName;
	 
		private String category;
	 
		private String subCategory;
	 
		private double price;
	 
		private long qty;
	 
		private double totalAmount;
	 
		private String orderId;
		
		private String tax;
		 
		private String discount;
		
		private double actualAmount;
		
		private String invoiceId;
	 
		
	 

}
