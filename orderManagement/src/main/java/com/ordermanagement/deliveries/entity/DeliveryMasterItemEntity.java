package com.ordermanagement.deliveries.entity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ordermanagement.SequenceGenerator.StringPrefixedSequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "delivery_master_item")
@Table(name = "delivery_master_item")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryMasterItemEntity {
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
	@GenericGenerator(name = "generator", strategy = "com.ordermanagement.SequenceGenerator.StringPrefixedSequenceGenerator", parameters = {

			@Parameter(name = StringPrefixedSequenceGenerator.INCREMENT_PARAM, value = "1"),
			@Parameter(name = StringPrefixedSequenceGenerator.VALUE_PREFIX_PARAMETER, value = "DLVYI_"),
			@Parameter(name = StringPrefixedSequenceGenerator.NUMBER_FORMATE_PARAMETER, value = "%05d"),

	})

	@Id
	private String deliveryMasterItemId;

	private String productName;

	private String category;

	private String subCategory;

	private double price;

	private long qty;

	private double totalAmount;

	private String deliveryId;
	
	private String tax;

	private String discount;

	private double actualAmount;
	
	private String orderMasterItemId;

}