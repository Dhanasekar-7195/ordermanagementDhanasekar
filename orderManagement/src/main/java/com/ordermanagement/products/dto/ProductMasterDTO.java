package com.ordermanagement.products.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductMasterDTO {
	
private String prodId;
	
	private String productName;

	private String Category;

	private String subCategory;

	private String TAX;

	private String Unit;

	private int Price;

	private String Discount;


}
