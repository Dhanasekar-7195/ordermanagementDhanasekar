package com.ordermanagement.payments.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ordermanagement.payments.entity.PaymentMasterEntity;

public interface PaymentMasterDAO extends JpaRepository<PaymentMasterEntity, String> {

	@Query(value = "SELECT * FROM payment_master WHERE invoiceNo = ?1", nativeQuery = true)
	PaymentMasterEntity getPaymentListByInvoiceNo(String invoiceNo);

}
