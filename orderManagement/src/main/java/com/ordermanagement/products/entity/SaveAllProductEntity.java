package com.ordermanagement.products.entity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "save_all_product_master")
@Table(name = "save_all_product_master")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveAllProductEntity {
	
	 @Id
	    private String product; // Corresponds to "Product"
	    private String categoryName; // Corresponds to "CategoryName"
	    private String productType; // Corresponds to "ProductType"
	    private String baseUnit; // Corresponds to "BaseUnit"
	    private String productDescription; // Corresponds to "ProductDescription"
	    private Double standardPrice; // Corresponds to "StandardPrice"
	    private String currency; // Corresponds to "Currency"
	    private String productSalesOrg;
	    private String productDistributionChnl;
	    private String division;
	    
	 // Fields for metadata
	    private String metadataId;
	    private String metadataUri;
	    private String metadataType;

}
