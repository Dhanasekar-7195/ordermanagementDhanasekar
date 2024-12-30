package com.ordermanagement.user.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordermanagement.orders.service.OrderMasterService;
import com.ordermanagement.user.dao.UserMasterDAO;
import com.ordermanagement.user.dto.ChangePasswordDTO;
import com.ordermanagement.user.dto.EmailDTO;
import com.ordermanagement.user.entity.UserMasterEntity;
import com.ordermanagement.user.service.EmailService;
import com.ordermanagement.user.service.UserMasterService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/public/email")
@Api(description = "Email Service", tags = { "EmailAPI" })
public class EmailController {

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserMasterDAO userMasterDAO;
	
	@Autowired
	private UserMasterService userMasterService;
	
	@Autowired
	private OrderMasterService omService;

	@PostMapping("/forget_password")
	public String forgetPassword(@RequestBody EmailDTO emailDTO) {
		UserMasterEntity user = userMasterDAO.findByEmail(emailDTO.getEmail());
		if (user != null) {
			// String token = jwtUtility.generateToken(user);
			String otp = String.format("%06d", new Random().nextInt(999999));
			emailService.sendEmail(emailDTO.getEmail(), otp);
			user.setToken(otp);
			user.setTokenCreationDate(LocalDateTime.now());
			userMasterDAO.save(user);
			return "Reset password email sent successfully";
		} else {
			return "email not found";
		}
	}

	@PostMapping("/change_password")
	public String changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
		UserMasterEntity user = userMasterDAO.findByToken(changePasswordDTO.getOtp());
		if (user == null) {
			return "Invalid OTP";
		}

		if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
			return "Passwords do not match";
		}
		
		String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}$";
	    if (!changePasswordDTO.getNewPassword().matches(passwordPattern)) {
	        return "Password must be between 8 to 12 characters and include uppercase, lowercase, number, and special character.";
	    }
	    
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setPassword(passwordEncoder.encode(changePasswordDTO.getConfirmPassword()));
		userMasterDAO.save(user);

		return "Password changed successfully";

	}

	@PostMapping("validate_otp")
	public String validateOtp(@RequestBody ChangePasswordDTO changePasswordDTO) {
		UserMasterEntity user = userMasterDAO.findByToken(changePasswordDTO.getOtp());
		if (user == null) {
			return "Invalid OTP";
		} else {
			return "otp validated successfully";
		}
	}
	
	@GetMapping("/get_all_user_master/{companyName}")
	public List<UserMasterEntity> getAllUserMaster(@PathVariable String companyName) {
		return this.userMasterService.getAllUserMaster(companyName);
	}
	
//	@DeleteMapping("/delete_usermaster_by_id/{userId}")
//	public Map<String, Object> deleteUserMasterById(@PathVariable String userId) {
//		if (userMasterDAO.getUserById(userId) != null) {
//			userMasterDAO.deleteById(userId);
//			Map<String, Object> response = new LinkedHashMap<>();
//			response.put("status", "success");
//			return response;
//		}
//		Map<String, Object> response = new LinkedHashMap<>();
//		response.put("status", "204");
//		response.put("id", "not_found");
//		return response;
//	}
	
//	@DeleteMapping("/delete_ordermaster_by_id/{orderId}")
//	public Map<String, Object> deleteByOrderId(@PathVariable String orderId) {
//		return this.omService.deleteByOrderId(orderId);
//	}
	
	

}
