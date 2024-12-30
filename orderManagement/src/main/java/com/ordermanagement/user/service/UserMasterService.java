package com.ordermanagement.user.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ordermanagement.user.entity.UserMasterEntity;
import com.ordermanagement.user.repository.UserMasterRepository;

@Component
public class UserMasterService {
	
	@Autowired
	private UserMasterRepository umRepo;

	public List<UserMasterEntity> getAllUserMaster(String companyName) {
		return this.umRepo.getAllUserMaster(companyName);
	}
	
	public Map<String, Object> updateUserStatus(String userId, Boolean active) {
		return this.umRepo.updateUserStatus(userId, active);
	}

	public Map<String, Object> addPassword(String email, String password) {
		return this.umRepo.addPassword(email, password);
	}

	public Map<String, Double> getCustomerDashboardData(String customerId) {
		return this.umRepo.getCustomerDashboardData(customerId);
	}
	
	public ResponseEntity<Object> getAllCustomerData() {
        return this.umRepo.getAllCustomerData();
	}


}
