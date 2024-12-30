package com.ordermanagement.invoices.entity;

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

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ordermanagement.SequenceGenerator.StringPrefixedSequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity(name = "invoice_master")
@Table(name = "invoice_master")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceMasterEntity {
	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
	@GenericGenerator(name = "generator", strategy = "com.ordermanagement.SequenceGenerator.StringPrefixedSequenceGenerator", parameters = {
 
			@Parameter(name = StringPrefixedSequenceGenerator.INCREMENT_PARAM, value = "1"),
			@Parameter(name = StringPrefixedSequenceGenerator.VALUE_PREFIX_PARAMETER, value = "INVNO_"),
			@Parameter(name = StringPrefixedSequenceGenerator.NUMBER_FORMATE_PARAMETER, value = "%05d"),
 
	})
	@Id
	private String invoiceId;
	
	private String orderId;
 
	private String invoiceNo;
 
	private String orderDate;
 
	private String deliveryLocation;
 
	private String deliveryAddress;
 
	private String contactPerson;
 
	private String contactNumber;
 
	private String comments;
 
	private double total;
 
	private String status;
 
	private String paymentId;
 
	private String deliveryId;
	private String draftId;
 
	private double payableAmount;
 
	private double paidAmount;
 
	private String deliveredDate;
 
	private String paymentDate;
 
	private String paymentMode;
 
	private String paymentStatus;
 
	private String customerId;
 
	@OneToMany(targetEntity = InvoiceMasterItemEntity.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "invoiceId", referencedColumnName = "invoiceId")
	private List<InvoiceMasterItemEntity> items;


}
