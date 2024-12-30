package com.ordermanagement.customers.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ordermanagement.customers.entity.CustomerMasterEntity;
import com.ordermanagement.user.entity.UserMasterEntity;

public interface CustomerMasterDAO extends JpaRepository<CustomerMasterEntity, String> {

	@Query(value = "SELECT * FROM customer_master WHERE customerId = ?1", nativeQuery = true)
	CustomerMasterEntity getAllCustomerById(String customerId);
	
	@Query(value = "Select * FROM customer_master where email=?1", nativeQuery = true)
	CustomerMasterEntity findCustomerByEmailBy(String email);
	
	@Query(value = "Select * FROM customer_master where contactNo=?1", nativeQuery = true)
	CustomerMasterEntity findCustomerByPhoneNoBy(String contactNo);
	
	@Query(value = "Select * FROM customer_master where customerName=?1", nativeQuery = true)
	CustomerMasterEntity findCustomerExists(String customerName);

}
