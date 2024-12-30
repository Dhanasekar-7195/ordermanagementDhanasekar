package com.ordermanagement.invoices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ordermanagement.invoices.entity.InvoiceMasterEntity;

public interface InvoiceMasterDAO extends JpaRepository<InvoiceMasterEntity, String> {

	@Query(value = "SELECT * FROM invoice_master WHERE orderId = ?1", nativeQuery = true)
	InvoiceMasterEntity getInvoiceByOrderId(String orderId);
}
