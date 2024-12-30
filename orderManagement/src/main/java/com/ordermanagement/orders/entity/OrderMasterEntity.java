package com.ordermanagement.orders.entity;

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

@Entity(name = "order_master_sap")
@Table(name = "order_master_sap")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderMasterEntity {
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
	@GenericGenerator(name = "generator", strategy = "com.ordermanagement.SequenceGenerator.StringPrefixedSequenceGenerator", parameters = {

			@Parameter(name = StringPrefixedSequenceGenerator.INCREMENT_PARAM, value = "1"),
			@Parameter(name = StringPrefixedSequenceGenerator.VALUE_PREFIX_PARAMETER, value = "ORD_"),
			@Parameter(name = StringPrefixedSequenceGenerator.NUMBER_FORMATE_PARAMETER, value = "%05d"),

	})

	@Id
	private String orderId;
	
	private String salesOrderType;
	
	private String salesOrganization;
	
	private String distributionChannel;
	
	private String organizationDivision;
	
	private String customerId;
	
	private String orderDate;
	
	private String incotermsClassification;
	
	private String incotermsTransferLocation;
	
	private String contactPerson;
	
	private String deliveryLocation;
	
	private String postalCode;
	
	private String streetName;
	
	private String region;
	
	private String telephoneNumber;
	
	private double total;
	
    private String status;
    
    private String salesOrderId;

	@OneToMany(targetEntity = OrderMasterItemEntity.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "orderId", referencedColumnName = "orderId")
	private List<OrderMasterItemEntity> items;
	
}
	
	//private String mobileNumber;
//	private String orderId;
//
//	private String invoiceNo;
//
//	private String orderDate;
//
//	private String deliveryLocation;
//
//	private String deliveryAddress;
//
//	private String contactPerson;
//
//	private String contactNumber;
//
//	private String comments;
//
	 //
//
//	private String paymentId;
//
//	private String deliveryId;
//	
//	private String draftId;
//
//	private double payableAmount;
//
//	private double paidAmount;
//
//	private String deliveredDate;
//
//	private String paymentDate;
//
//	private String paymentMode;
//
//	private String paymentStatus;
//
//	private String customerId;
//	
	

//}
