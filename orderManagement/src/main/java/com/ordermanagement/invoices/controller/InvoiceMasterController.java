package com.ordermanagement.invoices.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordermanagement.invoices.dto.InvoiceMasterDTO;
import com.ordermanagement.invoices.entity.InvoiceMasterEntity;
import com.ordermanagement.invoices.service.InvoiceMasterService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("api/invoice_master")
@Api(description = "Invoice Master Service", tags = { "InvoiceMasterAPI" })
public class InvoiceMasterController {
	
	@Autowired
	private InvoiceMasterService imService;
	
	@GetMapping("/get_all_invoice_master")
	public List<InvoiceMasterEntity> getAllInvoiceMaster() {
		return this.imService.getAllInvoiceMaster();
	}
	
	@PostMapping("/add_invoice_master")
	public Map<String, Object> addInvoiceMaster(@RequestBody InvoiceMasterDTO inDTO) {
		return this.imService.addInvoiceMaster(inDTO);
	}

}
