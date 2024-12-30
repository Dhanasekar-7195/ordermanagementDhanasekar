package com.ordermanagement.invoices.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ordermanagement.invoices.entity.InvoiceMasterItemEntity;

public interface InvoiceMasterItemDAO extends JpaRepository<InvoiceMasterItemEntity, String> {

}
