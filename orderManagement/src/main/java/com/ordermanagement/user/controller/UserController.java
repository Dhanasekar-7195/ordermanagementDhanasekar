package com.ordermanagement.user.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordermanagement.customers.controller.CustomerMasterController;
import com.ordermanagement.customers.service.CustomerMasterService;
import com.ordermanagement.orders.service.OrderMasterService;
import com.ordermanagement.products.controller.ProductMasterController;
import com.ordermanagement.products.service.ProductMasterService;
import com.ordermanagement.user.dao.UserMasterDAO;
import com.ordermanagement.user.dto.UserMasterDTO;
import com.ordermanagement.user.entity.UserMasterEntity;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/public/user")
@Api(description = "User Service", tags = { "User API" })
public class UserController {
	
	public static final String USER_ROLE = "user";
	public static final String MANAGER_ROLE = "manager";
	public static final String ADMIN_ROLE = "admin";
	
	@Autowired
	private UserMasterDAO umDAO;
	
	@Autowired
	private OrderMasterService omService;
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Autowired
	private ProductMasterService pmService;
	
	@Autowired
	private CustomerMasterService cmService;
	
	@DeleteMapping("/delete_usermaster_by_id/{userId}")
	public Map<String, Object> deleteUserMasterById(@PathVariable String userId) {
		if (umDAO.getUserById(userId) != null) {
			umDAO.deleteById(userId);
			Map<String, Object> response = new LinkedHashMap<>();
			response.put("status", "success");
			return response;
		}
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("status", "204");
		response.put("id", "not_found");
		return response;
	}
	
//	@DeleteMapping("/delete_ordermaster_by_id/{orderId}")
//	public Map<String, Object> deleteByOrderId(@PathVariable String orderId) {
//		return this.omService.deleteByOrderId(orderId);
//	}
	
	@PutMapping("/edit-usermaster")
	public Map<String, Object> EditUserMaster(@RequestBody UserMasterDTO userDTO) {
	    UserMasterEntity userEnt = umDAO.getUserById(userDTO.getUserId());
	    if (userEnt != null) {
	        userEnt.setUserId(userDTO.getUserId());
	        String trimmedUserName = userDTO.getUserName() != null ? userDTO.getUserName().trim() : null;
	        userEnt.setUserName(trimmedUserName);
	        userEnt.setEmail(userDTO.getEmail());
	        userEnt.setActive(userDTO.isActive());
	        userEnt.setRole(userDTO.getRole());
	        userEnt.setToken(userDTO.getToken());
	        userEnt.setTokenCreationDate(userDTO.getTokenCreationDate());
	        userEnt.setLocation(userDTO.getLocation());
	        userEnt.setCompanyName(userDTO.getCompanyName());
	        userEnt.setMobileNumber(userDTO.getMobileNumber());

	        // Validate mobile number
	        if (userEnt.getMobileNumber().length() != 10 || !userEnt.getMobileNumber().matches("\\d+")) {
	            Map<String, Object> response = new LinkedHashMap<>();
	            response.put("status", "failed");
	            response.put("code", "510");
	            response.put("error", "Please enter a valid 10-digit mobile number");
	            return response;
	        }

	        // Check for duplicate email
	        UserMasterEntity uEmail = this.umDAO.findUserByEmailByExcludeUserId(userDTO.getUserId(), userDTO.getEmail());
	        if (uEmail != null) {
	            Map<String, Object> response = new LinkedHashMap<>();
	            response.put("status", "failed");
	            response.put("code", "509");
	            response.put("error", "Email already exists");
	            return response;
	        }

	        // Check for duplicate phone number
	        UserMasterEntity uPho = this.umDAO.findUserByPhoneNoByExcludeUserId(userDTO.getUserId(), userDTO.getMobileNumber());
	        if (uPho != null) {
	            Map<String, Object> response = new LinkedHashMap<>();
	            response.put("status", "failed");
	            response.put("code", "509");
	            response.put("error", "Mobile number already exists");
	            return response;
	        }

	        // Save the updated entity
	        umDAO.save(userEnt);

	        // Send email only if the update is successful
	        if (!userEnt.getEmail().equals(userDTO.getEmail())) {
	            SimpleMailMessage message = new SimpleMailMessage();
	            message.setTo(userDTO.getEmail());
	            message.setSubject("Registration successfully completed!");
	            message.setText("Hi " + userDTO.getUserName() + ",\n\n" +
	                    "Welcome to IKYAM Order Management System application!\n\n" +
	                    "Please verify your account by clicking the link below to set your password:\n\n" +
	                    //"https://orderbooking.cfapps.us10-001.hana.ondemand.com/#/Create_Account");
	                    "https://omswepapp.cfapps.us10-001.hana.ondemand.com/#/Create_Account");
	            mailSender.send(message);
	        }
	    }

	    Map<String, Object> response = new LinkedHashMap<>();
	    response.put("status", "success");
	    response.put("id", userEnt.getUserId());
	    return response;
	}

	
	@GetMapping("/get_and_save_all_product_data")
	public String getAndSaveAllProductData() {
		return pmService.getAndSaveAllProductData();
	}
	
	@GetMapping("/get_and_save_all_customer_data")
	public String getAndSaveAllCustomerData() {
		return this.cmService.getAndSaveAllCustomerData();
	}


}
