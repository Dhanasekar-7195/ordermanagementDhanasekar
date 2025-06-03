package com.ordermanagement.customers.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordermanagement.customers.dto.CustomerMasterDTO;
import com.ordermanagement.customers.entity.CustomerMasterEntity;
import com.ordermanagement.customers.entity.SaveAllCustomerEntity;
import com.ordermanagement.customers.service.CustomerMasterService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("api/public/customer_master")
@Api(description = "Customer Master Service", tags = { "CustomerMasterAPI" })
public class CustomerMasterController {

	@Autowired
	private CustomerMasterService cmService;

	@PostMapping("/add_customer_master")
	public Map<String, Object> addCustomerMaster(@PathVariable String tenantId,@RequestBody CustomerMasterDTO cmDTO) {
		return this.cmService.addCustomerMaster(cmDTO);
	}

	@GetMapping("/get_all_customermaster")
	public List<CustomerMasterEntity> getAllCustomerMaster(@PathVariable String tenantId) {
		return this.cmService.getAllCustomerMaster();
	}
	
	@GetMapping("/get_all_customer_data")
	public ResponseEntity<Object> getAllCustomerData(@PathVariable String tenantId) {
		return this.cmService.getAllCustomerData();
	}	
	
	
	
	
	@GetMapping("/get_all_s4hana_customermaster")
	public List<SaveAllCustomerEntity> getAllS4hanaCustomerMaster() {
		return this.cmService.getAllS4hanaCustomerMaster();
	}



}
