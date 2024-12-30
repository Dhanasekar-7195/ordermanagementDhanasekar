package com.ordermanagement.user.repository;

import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordermanagement.dashboard.dao.DashBoardDAO;
import com.ordermanagement.dashboard.entity.DashBoardEntity;
import com.ordermanagement.user.dao.UserMasterDAO;
import com.ordermanagement.user.entity.UserMasterEntity;

@Component
public class UserMasterRepository {
	
	@Autowired
	private UserMasterDAO umDAO;
	
	@Autowired
	private DashBoardDAO dbDAO;
	
	public List<UserMasterEntity> getAllUserMaster(String companyName) {
		List<UserMasterEntity> umList = umDAO.findByCompanyName(companyName);
		return umList;
	}

	public Map<String, Object> updateUserStatus(String userId, Boolean active) {
		Optional<UserMasterEntity> userList = umDAO.findById(userId);
		if(userList != null) {
			UserMasterEntity umEnt = userList.get();
			umEnt.setActive(active);
			umDAO.save(umEnt);
		}
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("id", userId);
		response.put("message", "updated successfully");
		return response;
		
	}

	public Map<String, Object> addPassword(String email, String password) {
	    Map<String, Object> response = new LinkedHashMap<>();
	    
	    UserMasterEntity userList = umDAO.findUserByEmailBy(email);
	    if (userList == null) {
	        response.put("status", "failed");
	        response.put("code", "404");
	        response.put("error", "User not found.");
	        return response;
	    }
	    
	    String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}$";
	    if (!password.matches(passwordPattern)) {
	        response.put("status", "failed");
	        response.put("code", "400");
	        response.put("error", "Password must be between 8 to 12 characters and include uppercase, lowercase, number, and special character.");
	        return response;
	    }
	    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    userList.setPassword(passwordEncoder.encode(password));
	    umDAO.save(userList);
	    
	    response.put("status", "success");
	    response.put("code", "200");
	    response.put("message", "Password updated successfully.");
	    return response;
	}

	public Map<String, Double> getCustomerDashboardData(String customerId) {
		List<DashBoardEntity> getDashBoard = dbDAO.findAllByCustomerId(customerId);
		Map<String, Double> statusCounts = new HashMap<>();
		int notStartedCount = 0;
		int deliveredCount = 0;
		int inProgressCount = 0;
		int pickedCount = 0;
 
		for (DashBoardEntity dbEnt : getDashBoard) {
			String status = dbEnt.getStatus();
 
			if ("Not Started".equals(status)) {
				notStartedCount++;
			} else if ("Delivered".equals(status)) {
				deliveredCount++;
			} else if ("In Progress".equals(status)) {
				inProgressCount++;
			} else if ("Picked".equals(status)) {
				pickedCount++;
			}
		}
		statusCounts.put("Not Started", (double)notStartedCount);
		statusCounts.put("Delivered", (double)deliveredCount);
		statusCounts.put("In Progress", (double)inProgressCount);
		statusCounts.put("Picked", (double)pickedCount);
		
		UserMasterEntity customerList = umDAO.getAllUserById(customerId);
		double returnCredit = umDAO.getreturnCredit(customerList.getUserId());
 
		statusCounts.put("Return Credit", returnCredit);
		return statusCounts ;
	}
	
	public ResponseEntity<Object> getAllCustomerData() {
		 RestTemplate restTemplate = new RestTemplate();

		    String url = "https://my403545-api.s4hana.cloud.sap/sap/opu/odata/sap/ZGETBPDATA/ZGETBPDATA?$format=json&$top=500";
		    String username = "INTEGRATION";
		    String password = "gNQH>ydgzvCKfwuxQdcEDQzHMz5YPQfaLxzPHqLz";

		    String auth = username + ":" + password;
		    String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
		    String authHeader = "Basic " + encodedAuth;

		    HttpHeaders headers = new HttpHeaders();
		    headers.set("Authorization", authHeader);

		    HttpEntity<String> request = new HttpEntity<>(headers);

		    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

		    ObjectMapper objectMapper = new ObjectMapper();
		    try {
		        Object jsonResponse = objectMapper.readValue(response.getBody(), Object.class);
		        return ResponseEntity.ok(jsonResponse);
		    } catch (JsonProcessingException e) {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error parsing JSON response");
		    }
	}


}
