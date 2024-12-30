package com.ordermanagement.payments.entity;

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

@Entity(name = "transaction_master")
@Table(name = "transaction_master")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionsMasterEntity {

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
	@GenericGenerator(name = "generator", strategy = "com.ordermanagement.SequenceGenerator.StringPrefixedSequenceGenerator", parameters = {

			@Parameter(name = StringPrefixedSequenceGenerator.INCREMENT_PARAM, value = "1"),
			@Parameter(name = StringPrefixedSequenceGenerator.VALUE_PREFIX_PARAMETER, value = "TXN_"),
			@Parameter(name = StringPrefixedSequenceGenerator.NUMBER_FORMATE_PARAMETER, value = "%05d"),

	})

	@Id
	private String transactionsId;

	private String invoiceNo;

	private String orderId;

	private String deliveryId;

	private double grossAmount;

	private double payableAmount;

	private double paidAmount;

	private String deliveredDate;

	private String paymentDate;

	private String paymentMode;

	private String paymentStatus;

	private String customerName;

	private double exactPaidAmount;

	private String customerId;
	
	private String paidBy;

}
