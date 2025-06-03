package com.ordermanagement.customers.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ordermanagement.customers.dto.CustomerMasterDTO;
import com.ordermanagement.customers.entity.CustomerMasterEntity;
import com.ordermanagement.customers.entity.SaveAllCustomerEntity;
import com.ordermanagement.customers.repository.CustomerMasterRepository;

@Component
public class CustomerMasterService {

	@Autowired
	private CustomerMasterRepository cmRepo;

	public Map<String, Object> addCustomerMaster(CustomerMasterDTO cmDTO) {
		return this.cmRepo.addCustomerMaster(cmDTO);
	}

	public List<CustomerMasterEntity> getAllCustomerMaster() {
		return this.cmRepo.getAllCustomerMaster();
	}

	public ResponseEntity<Object> getAllCustomerData() {
        return this.cmRepo.getAllCustomerData();
	}
	
	
	
	
	
	

	public String getAndSaveAllCustomerData() {
        return this.cmRepo.getAndSaveAllCustomerData();

	}

	public List<SaveAllCustomerEntity> getAllS4hanaCustomerMaster() {
		return this.cmRepo.getAllS4hanaCustomerMaster();
	}



}
