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

@Entity(name = "order_master")
@Table(name = "order_master")
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
	
	private String invoiceNo;
	
	private String supplier;
	
	private String type;
	
	private String glGroup;
	
	private String orderDate;
	
	private String name;
	
	private String addressLine1;
	
	private String addressLine2;
	
	private String phone;
	
	private String fax;
	
	private String tel;
	
	private String comments;
	
	private double creditLimit;
	
	private double outstanding;
	
	private double available;
	
    private double amount;
	
	private double tax;
	
	private double totalAmount;
	
	private String notes;
	
	@OneToMany(targetEntity = OrderMasterItemEntity.class,cascade = CascadeType.ALL)
	@JoinColumn(name="orderId",referencedColumnName = "orderId")
    private List<OrderMasterItemEntity> items;

}
