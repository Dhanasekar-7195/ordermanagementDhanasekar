package com.ordermanagement.invoices.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ordermanagement.invoices.dto.InvoiceMasterDTO;
import com.ordermanagement.invoices.entity.InvoiceMasterEntity;
import com.ordermanagement.invoices.repository.InvoiceMasterRepository;

@Component
public class InvoiceMasterService {
	
	@Autowired
	private InvoiceMasterRepository imRepo;
	
	public List<InvoiceMasterEntity> getAllInvoiceMaster() {
		return this.imRepo.getAllInvoiceMaster();
	}
	
	public Map<String, Object> addInvoiceMaster(InvoiceMasterDTO imDTO) {
		return this.imRepo.addInvoiceMaster(imDTO);
	}

}
