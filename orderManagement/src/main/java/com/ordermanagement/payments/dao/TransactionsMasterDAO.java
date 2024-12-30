package com.ordermanagement.payments.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ordermanagement.payments.entity.TransactionsMasterEntity;

public interface TransactionsMasterDAO extends JpaRepository<TransactionsMasterEntity, String> {
//	@Query(value = "SELECT * FROM transaction_master WHERE invoiceNo = ?1 ORDER BY CAST(SUBSTRING(transactionsId, 5) AS UNSIGNED) DESC LIMIT 1", nativeQuery = true)
//	TransactionsMasterEntity getLatestPaymentListByInvoiceNo(String invoiceNo);
//
//	@Query(value = "SELECT * FROM transaction_master WHERE invoiceNo = ?1 ORDER BY CAST(SUBSTRING(transactionsId, 5) AS UNSIGNED) ASC LIMIT 1", nativeQuery = true)
//	TransactionsMasterEntity getInitialPaymentListByInvoiceNo(String invoiceNo);

	@Query(value = "SELECT * FROM transaction_master WHERE orderId = ?1 AND paymentStatus <> 'open'", nativeQuery = true)
	List<TransactionsMasterEntity> getTransactionMasterByOrderId(String orderId);
	
	@Query(value = "SELECT * FROM transaction_master WHERE invoiceNo = ?1 ORDER BY CAST(SUBSTRING(transactionsId, 5) AS INTEGER) DESC LIMIT 1", nativeQuery = true)
	TransactionsMasterEntity getLatestPaymentListByInvoiceNo(String invoiceNo);

	@Query(value = "SELECT * FROM transaction_master WHERE invoiceNo = ?1 ORDER BY CAST(SUBSTRING(transactionsId, 5) AS INTEGER) ASC LIMIT 1", nativeQuery = true)
	TransactionsMasterEntity getInitialPaymentListByInvoiceNo(String invoiceNo);


}
