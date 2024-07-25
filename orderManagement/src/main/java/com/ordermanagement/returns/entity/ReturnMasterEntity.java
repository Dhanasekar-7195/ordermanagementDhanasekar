package com.ordermanagement.returns.entity;

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

@Entity(name = "return_master")
@Table(name = "return_master")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturnMasterEntity {
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
	@GenericGenerator(name = "generator", strategy = "com.ordermanagement.SequenceGenerator.StringPrefixedSequenceGenerator", parameters = {

			@Parameter(name = StringPrefixedSequenceGenerator.INCREMENT_PARAM, value = "1"),
			@Parameter(name = StringPrefixedSequenceGenerator.VALUE_PREFIX_PARAMETER, value = "RTRN_"),
			@Parameter(name = StringPrefixedSequenceGenerator.NUMBER_FORMATE_PARAMETER, value = "%05d"),

	})
	
	@Id
	private String returnId;
	
	private String invoiceNumber;
	
	private String reason;
	
	private String contactPerson;
	
	private String email;
	
	private double totalCredit;
	
	private String notes;
	
	@OneToMany(targetEntity = ReturnMasterItemEntity.class,cascade = CascadeType.ALL)
	@JoinColumn(name="returnId",referencedColumnName = "returnId")
    private List<ReturnMasterItemEntity> items;

}
