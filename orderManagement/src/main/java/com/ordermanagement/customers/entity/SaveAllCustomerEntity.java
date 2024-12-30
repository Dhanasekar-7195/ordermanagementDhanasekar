package com.ordermanagement.customers.entity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ordermanagement.products.entity.SaveAllProductEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "save_all_customer_master")
@Table(name = "save_all_customer_master")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveAllCustomerEntity {
	
	@Id
	private String customer;
	private String customerName;
	private String addressID;
	private String cityName;
	private String postalCode;
	private String streetName;
	private String region;
	private String telephoneNumber1;
	private String country;
	private String districtName;
	private String emailAddress;
	private String mobilePhoneNumber;
	
	 // Fields for metadata
    private String metadataId;
    private String metadataUri;
    private String metadataType;
	

}
