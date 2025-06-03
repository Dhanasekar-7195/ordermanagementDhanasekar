package com.ordermanagement.user.controller;
 
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ordermanagement.JWTService.JWTService;
import com.ordermanagement.JWTUtility.JWTUtility;
import com.ordermanagement.user.dao.UserMasterDAO;
import com.ordermanagement.user.dto.LoginTokenDTO;
import com.ordermanagement.user.dto.UserMasterDTO;
import com.ordermanagement.user.entity.UserMasterEntity;
import com.ordermanagement.user.service.GroupUserDetailsService;
import com.ordermanagement.user.service.UserMasterService;

import io.swagger.annotations.Api;
 
@RestController
@RequestMapping("api/public/user_master")
@Api(description = "User Master Controller", tags = { "User Master API" })
public class UserMasterController {
 
	public static final String USER_ROLE = "user";
	public static final String MANAGER_ROLE = "manager";
	public static final String ADMIN_ROLE = "admin";
 
	@Autowired
	private GroupUserDetailsService uService;
@Autowired
    private JavaMailSender mailSender;
 
	@Autowired
	private UserMasterDAO umDAO;
 
	@Autowired
	private JWTService jwtService;
 
	@Autowired
	private JWTUtility jwtUtility;
 
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserMasterService userMasterService; 
	
	@Autowired
	private GroupUserDetailsService eService;
	
	@PostMapping("/add-usermaster")
	public Map<String, Object> AddUserMaster(@RequestBody UserMasterDTO userDTO) {
	    UserMasterEntity userEnt = new UserMasterEntity();
	    String trimmedUserName = userDTO.getUserName() != null ? userDTO.getUserName().trim() : null;
	    userEnt.setUserName(trimmedUserName);
	    userEnt.setPassword("-");
	    userEnt.setEmail(userDTO.getEmail());
	    userEnt.setActive(userDTO.isActive());
	    userEnt.setRole(userDTO.getRole());
	    userEnt.setToken(userDTO.getToken());
	    userEnt.setTokenCreationDate(userDTO.getTokenCreationDate());
	    userEnt.setLocation(userDTO.getLocation());
	    userEnt.setCompanyName(userDTO.getCompanyName());
	    UserMasterEntity umEnt = umDAO.getByCompanyName(userDTO.getCompanyName());
	    String tenantId = umEnt.getTenantId();
	    userEnt.setTenantId(tenantId);
	    userEnt.setMobileNumber(userDTO.getMobileNumber());
	    userEnt.setReturnCredit(0);
//	    userEnt.setShippingAddress1("-");
//	    userEnt.setShippingAddress2("-");

	    if (userEnt.getMobileNumber().length() != 10 || !userEnt.getMobileNumber().matches("\\d+")) {
	        Map<String, Object> response = new LinkedHashMap<>();
	        response.put("status", "failed");
	        response.put("code", "510");
	        response.put("error", "Please enter a valid 10-digit mobile number");
	        return response;
	    }

	    UserMasterEntity uEmail = this.umDAO.findUserByEmailBy(userEnt.getEmail());

	    if (uEmail != null) {
	        Map<String, Object> response = new LinkedHashMap<>();
	        response.put("status", "failed");
	        response.put("code", "509");
	        response.put("error", "email already exists");
	        return response;
	    }
	    UserMasterEntity uPho = this.umDAO.findUserByPhoneNoBy(userEnt.getMobileNumber());
	    if (uPho != null) {
	        Map<String, Object> response = new LinkedHashMap<>();
	        response.put("status", "failed");
	        response.put("code", "509");
	        response.put("error", "mobile number already exists");
	        return response;
	    }

	    umDAO.save(userEnt);

	    Map<String, Object> s4HanaResponse = new LinkedHashMap<>();
	    if ("Customer".equalsIgnoreCase(userEnt.getRole())) {
	        Map<String, Object> s4Payload = new HashMap<>();
	        s4Payload.put("BusinessPartnerGrouping", "BP04"); 
	        s4Payload.put("BusinessPartnerCategory", "2"); 
	        s4Payload.put("OrganizationBPName1", userEnt.getUserName()); 
	        s4Payload.put("SearchTerm1", "CUST_TEST"); 
	        s4Payload.put("LegalForm", "01");
	        s4Payload.put("BusinessPartnerIsBlocked", false); 

	        try {
	            String s4hanaApiUrl = "https://my403545-api.s4hana.cloud.sap/sap/opu/odata/sap/API_BUSINESS_PARTNER/A_BusinessPartner";
	            String username = "INTEGRATION"; 
	            String password = "gNQH>ydgzvCKfwuxQdcEDQzHMz5YPQfaLxzPHqLz"; 

	            RestTemplate restTemplate = new RestTemplate();

	            HttpHeaders getHeaders = new HttpHeaders();
	            getHeaders.setBasicAuth(username, password);
	            getHeaders.set("x-csrf-token", "fetch");

	            HttpEntity<Void> csrfRequest = new HttpEntity<>(getHeaders);
	            ResponseEntity<String> csrfResponse = restTemplate.exchange(
	                    s4hanaApiUrl, HttpMethod.GET, csrfRequest, String.class);

	            if (csrfResponse.getStatusCode() == HttpStatus.OK) {
	                String csrfToken = csrfResponse.getHeaders().getFirst("x-csrf-token");
	                List<String> cookies = csrfResponse.getHeaders().get(HttpHeaders.SET_COOKIE);

	                HttpHeaders postHeaders = new HttpHeaders();
	                postHeaders.setBasicAuth(username, password);
	                postHeaders.set("x-csrf-token", csrfToken);
	                postHeaders.setContentType(MediaType.APPLICATION_JSON);
	                postHeaders.put(HttpHeaders.COOKIE, cookies);

	                HttpEntity<Map<String, Object>> postRequest = new HttpEntity<>(s4Payload, postHeaders);
	                ResponseEntity<String> postResponse = restTemplate.exchange(
	                        s4hanaApiUrl, HttpMethod.POST, postRequest, String.class);

	                if (postResponse.getStatusCode() == HttpStatus.CREATED) {
	                    s4HanaResponse.put("status", "success");
	                    s4HanaResponse.put("message", "User added to S/4HANA successfully");
	                } else {
	                    s4HanaResponse.put("status", "failure");
	                    s4HanaResponse.put("message", "Failed to add user to S/4HANA");
	                    s4HanaResponse.put("response", postResponse.getBody());
	                }
	            } else {
	                s4HanaResponse.put("status", "error");
	                s4HanaResponse.put("message", "Failed to fetch x-csrf-token");
	            }
	        } catch (Exception e) {
	            s4HanaResponse.put("status", "error");
	            s4HanaResponse.put("message", "Error while communicating with S/4HANA: " + e.getMessage());
	        }
	    }

	    SimpleMailMessage message = new SimpleMailMessage();
	    message.setTo(userEnt.getEmail());
	    message.setSubject("Registration successfully completed!");
	    message.setText("Hi " + userEnt.getUserName() + ",\n\n" +
	            "Welcome to IKYAM Order Management System application!\n\n" +
	            "Username: " + userEnt.getUserName() + "\n\n" +
	            "Please verify your account by clicking the link below to set your password:\n\n" +
	           // "https://orderbooking.cfapps.us10-001.hana.ondemand.com/#/Create_Account");
	            "https://omswepapp.cfapps.us10-001.hana.ondemand.com/#/Create_Account");

	    mailSender.send(message);

	    Map<String, Object> response = new LinkedHashMap<>();
	    response.put("status", "success");
	    response.put("id", userEnt.getUserId());
	    response.put("s4HanaResponse", s4HanaResponse); 
	    return response;
	}



 
//	@PostMapping("/add-usermaster")
//	public Map<String, Object> AddUserMaster(@RequestBody UserMasterDTO userDTO) {
//		UserMasterEntity userEnt = new UserMasterEntity();
//		String trimmedUserName = userDTO.getUserName() != null ? userDTO.getUserName().trim() : null;
//	    userEnt.setUserName(trimmedUserName);
//		userEnt.setPassword("-");
//		userEnt.setEmail(userDTO.getEmail());
//		userEnt.setActive(userDTO.isActive());
//		userEnt.setRole(userDTO.getRole());
//		userEnt.setToken(userDTO.getToken());
//		userEnt.setTokenCreationDate(userDTO.getTokenCreationDate());
//		userEnt.setLocation(userDTO.getLocation());
//		userEnt.setCompanyName(userDTO.getCompanyName());
//		userEnt.setMobileNumber(userDTO.getMobileNumber());
//		userEnt.setReturnCredit(0);
//	    userEnt.setShippingAddress1("-");
//	    userEnt.setShippingAddress2("-");
// 
// 
//	//	UserMasterEntity ume = this.umDAO.findUserByNameBy(userEnt.getUserName());
//	//	if (ume != null) {
//	//		Map<String, Object> response = new LinkedHashMap<>();
//	//		response.put("status", "failed");
//	//		response.put("code", "509");
//	//		response.put("error", "user already exist");
//	//		return response;
//	//	}
//	
//			if (userEnt.getMobileNumber().length() != 10 || !userEnt.getMobileNumber().matches("\\d+")) {
//	        Map<String, Object> response = new LinkedHashMap<>();
//	        response.put("status", "failed");
//	        response.put("code", "510");
//	        response.put("error", "Please enter a valid 10-digit mobile number");
//	        return response;
//	    }
//		
////		if (!userEnt.getEmail().endsWith("@gmail.com")) {
////	        Map<String, Object> response = new LinkedHashMap<>();
////	        response.put("status", "failed");
////	        response.put("code", "400");
////	        response.put("error", "Email must contain '@gmail.com'");
////	        return response;
////	    }
//		
////		String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}$";
////	    if (!userDTO.getPassword().matches(passwordPattern)) {
////	        Map<String, Object> response = new LinkedHashMap<>();
////	        response.put("status", "failed");
////	        response.put("code", "400");
////	        response.put("error", "Password must be between 8 to 12 characters and include uppercase, lowercase, number, and special character.");
////	        return response;
////	    }
//		UserMasterEntity uEmail = this.umDAO.findUserByEmailBy(userEnt.getEmail());
// 
//		if (uEmail != null) {
//			Map<String, Object> response = new LinkedHashMap<>();
//			response.put("status", "failed");
//			response.put("code", "509");
//			response.put("error", "email already exist");
//			return response;
//		}
//		UserMasterEntity uPho = this.umDAO.findUserByPhoneNoBy(userEnt.getMobileNumber());
//		if (uPho != null) {
//			Map<String, Object> response = new LinkedHashMap<>();
//			response.put("status", "failed");
//			response.put("code", "509");
//			response.put("error", "mobile number already exists");
//			return response;
//		}
// 
//		else {
// 
//			umDAO.save(userEnt);
// 
////			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
////			userEnt.setPassword(passwordEncoder.encode(userDTO.getPassword()));
////			umDAO.save(userEnt);
// 
//			SimpleMailMessage message = new SimpleMailMessage();
//	        message.setTo(userEnt.getEmail());
//	        message.setSubject("Registration successfully completed!");
//	        message.setText("Hi " + userEnt.getUserName() + ",\n\n" +
//	                "Welcome to IKYAM Order Management System application!\n\n" +
//	        		"Username: " + userEnt.getUserName() + "\n\n" +
//	                "Please verify your account by clicking the link below to set your password:\n\n" +
//	                "https://orderbooking.cfapps.us10-001.hana.ondemand.com/#/Create_Account");
//	        mailSender.send(message);
//			Map<String, Object> response = new LinkedHashMap<>();
//			response.put("status", "success");
//			response.put("id", userEnt.getUserId());
//			return response;
//			
//		}
// 
//	}
	
	@PostMapping("/login-authenticate")
	public Map<String, Object> Login_Authenticate(@RequestBody UserMasterEntity jwtRequest) throws Exception {
 
	    Map<String, Object> response = new LinkedHashMap<>();
List<UserMasterEntity> users = umDAO.findUserByNameList(jwtRequest.getUserName());

 if (users.isEmpty()) {
	        response.put("status", "failed");
	        response.put("code", "404");
	        response.put("error", "Employee not found with name: " + jwtRequest.getUserName());
	        return response;
	    }
 
//	    if (!jwtRequest.getEmployeeName().equals(employee.get().getEmployeeName())) {
//			response.put("status", "failed");
//			response.put("code", "509");
//			response.put("error", "Employee name not matches");
//			return response;
//		}
	    
//	    Optional<EmployeeMasterEntity> emp = emDAO.findEmployeeByName(jwtRequest.getEmployeeName());
//	    if (!jwtRequest.getEmployeeName().equals(emp.get().getEmployeeName())) {
//	        response.put("status", "failed");
//	        response.put("code", "509");
//	        response.put("error", "Employee name not matches");
//	        return response;
//	    }
 
	    // Check if any of the found users match the provided password
	    for (UserMasterEntity user : users) {
	    	if(!user.isActive()) {
	    		response.put("status", "failed");
	    		response.put("code", "403");
	            response.put("error", "User account is inactive. Please contact support.");
	            return response;
	    	}
	    	if (!jwtRequest.getUserName().equals(user.getUserName())) {
				response.put("status", "failed");
				response.put("code", "509");
				response.put("error", "Employee name not matches");
				return response;
			}
	        if (new BCryptPasswordEncoder().matches(jwtRequest.getPassword(), user.getPassword())) {
	            // Generate JWT token if credentials match
	            final UserDetails employeeDetails = eService.loadUserByUsername(user.getUserName());
	            final String token = jwtUtility.generateToken(employeeDetails);
	            
	            // Store the token and return the response
	            jwtService.storeJWT(user.getUserId().toString(), token, user.getRole());
	            response.put("status", "success");
	            response.put("token", token);
	            response.put("role", user.getRole());
	            response.put("userId", user.getUserId());
	            response.put("company", user.getCompanyName());
	            response.put("mobileNumber", user.getMobileNumber());
	            response.put("company Name", user.getCompanyName().toLowerCase().replace(" ", "_"));
	            return response;
	        }
	    }
 
	    // If no match found
	    response.put("status", "failed");
	    response.put("code", "401");
	    response.put("error", "INVALID EMPLOYEE NAME or PASSWORD");
	    return response;
 
	}
 
//	@PostMapping("/login-authenticate")
//	public LoginTokenDTO Login_Authenticate(@RequestBody UserMasterEntity jwtRequest) throws Exception {
// 
//		LoginTokenDTO tok = new LoginTokenDTO();
// 
//		try {
//			authenticationManager.authenticate(
//					new UsernamePasswordAuthenticationToken(jwtRequest.getUserName(), jwtRequest.getPassword()));
//		} catch (BadCredentialsException e) {
//			throw new Exception("INVALID USER NAME Or PASSWORD", e);
// 
//		}
// 
//		final UserDetails userDetails = uService.loadUserByUsername(jwtRequest.getUserName());
// 
//		final String token = jwtUtility.generateToken(userDetails);
//		Optional<UserMasterEntity> user = umDAO.findUserByName(jwtRequest.getUserName());
// 
//		tok.setRole(user.get().getRole());
//		tok.setLocation(user.get().getLocation());
//		tok.setCompanyName(user.get().getCompanyName());
//		tok.setMobileNumber(user.get().getMobileNumber());
// 
//		tok.setUserId(user.get().getUserId());
//		jwtService.storeJWT(user.get().getUserId().toString(), token, user.get().getRole());
//		tok.setToken(token);
//		return tok;
// 
//	}
	
//	@DeleteMapping("/delete_usermaster_by_id/{userId}")
//	public Map<String, Object> deleteUserMasterById(@PathVariable String userId) {
//		if (umDAO.getUserById(userId) != null) {
//			umDAO.deleteById(userId);
//			Map<String, Object> response = new LinkedHashMap<>();
//			response.put("status", "success");
//			return response;
//		}
//		Map<String, Object> response = new LinkedHashMap<>();
//		response.put("status", "204");
//		response.put("id", "not_found");
//		return response;
//	}
	
//	@GetMapping("/get_all_user_master")
//	public List<UserMasterEntity> getAllUserMaster() {
//		return this.userMasterService.getAllUserMaster();
//	}
	
//	@PutMapping("/edit-usermaster")
//	public Map<String, Object> EditUserMaster(@RequestBody UserMasterDTO userDTO) {
//		UserMasterEntity userEnt = umDAO.getUserById(userDTO.getUserId());
//		userEnt.setUserName(userDTO.getUserName());
//		userEnt.setEmail(userDTO.getEmail());
//		userEnt.setActive(userDTO.isActive());
//		userEnt.setRole(userDTO.getRole());
//		userEnt.setToken(userDTO.getToken());
//		userEnt.setTokenCreationDate(userDTO.getTokenCreationDate());
//		userEnt.setLocation(userDTO.getLocation());
//		userEnt.setCompanyName(userDTO.getCompanyName());
//		userEnt.setMobileNumber(userDTO.getMobileNumber());
//
// 
//	//	UserMasterEntity ume = this.umDAO.findUserByNameBy(userEnt.getUserName());
//	//	if (ume != null) {
//	//		Map<String, Object> response = new LinkedHashMap<>();
//	//		response.put("status", "failed");
//	//		response.put("code", "509");
//	//		response.put("error", "user already exist");
//	//		return response;
//	//	}
//	
//			if (userEnt.getMobileNumber().length() != 10 || !userEnt.getMobileNumber().matches("\\d+")) {
//	        Map<String, Object> response = new LinkedHashMap<>();
//	        response.put("status", "failed");
//	        response.put("code", "510");
//	        response.put("error", "Please enter a valid 10-digit mobile number");
//	        return response;
//	    }
//		
////		if (!userEnt.getEmail().endsWith("@gmail.com")) {
////	        Map<String, Object> response = new LinkedHashMap<>();
////	        response.put("status", "failed");
////	        response.put("code", "400");
////	        response.put("error", "Email must contain '@gmail.com'");
////	        return response;
////	    }
//		
////		String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}$";
////	    if (!userDTO.getPassword().matches(passwordPattern)) {
////	        Map<String, Object> response = new LinkedHashMap<>();
////	        response.put("status", "failed");
////	        response.put("code", "400");
////	        response.put("error", "Password must be between 8 to 12 characters and include uppercase, lowercase, number, and special character.");
////	        return response;
////	    }
//		UserMasterEntity uEmail = this.umDAO.findUserByEmailByExcludeUserId(userDTO.getUserId(), userDTO.getEmail());
// 
//		if (uEmail != null) {
//			Map<String, Object> response = new LinkedHashMap<>();
//			response.put("status", "failed");
//			response.put("code", "509");
//			response.put("error", "email already exist");
//			return response;
//		}
//		UserMasterEntity uPho = this.umDAO.findUserByPhoneNoByExcludeUserId(userDTO.getUserId(), userDTO.getMobileNumber());
//		if (uPho != null) {
//			Map<String, Object> response = new LinkedHashMap<>();
//			response.put("status", "failed");
//			response.put("code", "509");
//			response.put("error", "mobile number already exists");
//			return response;
//		}
// 
//		//else {
// 
//			umDAO.save(userEnt);
// 
////			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
////			userEnt.setPassword(passwordEncoder.encode(userDTO.getPassword()));
////			umDAO.save(userEnt);
//			
//			if (!userEnt.getEmail().equals(userDTO.getEmail())) {
// 
//			SimpleMailMessage message = new SimpleMailMessage();
//	        message.setTo(userDTO.getEmail());
//	        message.setSubject("Registration successfully completed!");
//	        message.setText("\tYour account has been successfully created in the "+userEnt.getCompanyName()+ " system.\n\tBelow are your login credentials:\n"
//	        		+"\n\t\t\t\tUserName: " + userEnt.getUserName() +
//                    "\n\t\t\t\tPassword: " + userDTO.getPassword() +
//                    "\n\t\t\t\tRole: " + userEnt.getRole()+
//                    "\n\n\tPlease use the credentials above to log in to your account at "
//                    +"https://orderbooking.cfapps.us10-001.hana.ondemand.com/."
//                    +"\n\n\tFor security reasons, we recommend that you update your password after your first login."
//					+"\n\nThanks,\n");
//	        mailSender.send(message);
//			}
//			Map<String, Object> response = new LinkedHashMap<>();
//			response.put("status", "success");
//			response.put("id", userEnt.getUserId());
//			return response;
//		
// 
//	}
	
	
	@PostMapping("/show")
	public String show() {
		return "working fine";
	}
	
	@PutMapping("/update_user_status/{userId}/{active}")
	public Map<String, Object> updateUserStatus(@PathVariable String userId, @PathVariable Boolean active) {
		return this.userMasterService.updateUserStatus(userId, active);
	}
	
	@PutMapping("/add_password/{email}/{password}")
	public Map<String, Object> addPassword(@PathVariable String email, @PathVariable String password) {
		return this.userMasterService.addPassword(email, password);
	}
	
	@GetMapping("/get_customer_dashboard_data/{customerId}")
	public Map<String, Double> getCustomerDashboardData(@PathVariable String customerId) {
		return this.userMasterService.getCustomerDashboardData(customerId);
	}
	
	@GetMapping("/get_all_customer_data")
	public ResponseEntity<Object> getAllCustomerData() {
		return userMasterService.getAllCustomerData();
	}
	
	
	
	
	

 
}