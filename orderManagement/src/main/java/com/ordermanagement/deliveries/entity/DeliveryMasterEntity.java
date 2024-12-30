package com.ordermanagement.deliveries.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ordermanagement.SequenceGenerator.StringPrefixedSequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "delivery_master")
@Table(name = "delivery_master")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryMasterEntity {
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
	@GenericGenerator(name = "generator", strategy = "com.ordermanagement.SequenceGenerator.StringPrefixedSequenceGenerator", parameters = {

			@Parameter(name = StringPrefixedSequenceGenerator.INCREMENT_PARAM, value = "1"),
			@Parameter(name = StringPrefixedSequenceGenerator.VALUE_PREFIX_PARAMETER, value = "DLVY_"),
			@Parameter(name = StringPrefixedSequenceGenerator.NUMBER_FORMATE_PARAMETER, value = "%05d"),

	})

	@Id
	private String deliveryId;

	private String invoiceNo;

	private String orderId;

	//private String modifiedAt;
	
	private String createdDate;
	
	private String pickedDate;
	
	private String deliveredDate;

	private String deliveryLocation;

	private String status;

	private String deliveryAddress;

	private String contactPerson;

	private String contactNumber;

	private String comments;

	private String customerId;

	private double total;

	@OneToMany(targetEntity = DeliveryMasterItemEntity.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "deliveryId", referencedColumnName = "deliveryId")
	private List<DeliveryMasterItemEntity> items;

}