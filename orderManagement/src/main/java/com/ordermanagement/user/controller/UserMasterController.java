package com.ordermanagement.user.controller;


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordermanagement.JWTService.JWTService;
import com.ordermanagement.JWTUtility.JWTUtility;
import com.ordermanagement.user.dao.UserMasterDAO;
import com.ordermanagement.user.dto.LoginTokenDTO;
import com.ordermanagement.user.dto.UserMasterDTO;
import com.ordermanagement.user.entity.UserMasterEntity;
import com.ordermanagement.user.service.GroupUserDetailsService;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("api/user_master")
@Api(description = "User Master Controller", tags = { "User Master API" })
public class UserMasterController {
	
	public static final String USER_ROLE = "user";
	public static final String MANAGER_ROLE = "manager";
	public static final String ADMIN_ROLE = "admin";
	
	@Autowired
	private GroupUserDetailsService uService;
	
	@Autowired
	private UserMasterDAO umDAO;
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private JWTUtility jwtUtility;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	
	@PostMapping("/add-usermaster")
	public Map<String, Object> AddUserMaster(@RequestBody UserMasterDTO userDTO) {
		UserMasterEntity userEnt= new UserMasterEntity();
		userEnt.setUserName(userDTO.getUserName());
		userEnt.setPassword(userDTO.getPassword());
		userEnt.setEmail(userDTO.getEmail());
		userEnt.setActive(userDTO.isActive());
		userEnt.setRole(userDTO.getRole());
		userEnt.setToken(userDTO.getToken());
		userEnt.setTokenCreationDate(userDTO.getTokenCreationDate());
		userEnt.setOrganisationId(userDTO.getOrganisationId());
		userEnt.setOrganisationName(userDTO.getOrganisationName());
		userEnt.setPhoneNumber(userDTO.getPhoneNumber());


		
		UserMasterEntity ume=this.umDAO.findUserByNameBy(userEnt.getUserName());
		if (ume != null) {
			Map<String, Object> response = new LinkedHashMap<>();
	        response.put("status","failed");
	        response.put("code","509");
	        response.put("error","user already exist");
	        return response;
	        }
		UserMasterEntity uEmail=this.umDAO.findUserByEmailBy(userEnt.getEmail());

		if(uEmail!= null) {
			Map<String, Object> response = new LinkedHashMap<>();
	        response.put("status","failed");
	        response.put("code","509");
	        response.put("error","email already exist");
	        return response;
	        }
		UserMasterEntity uPho=this.umDAO.findUserByPhoneNoBy(userEnt.getPhoneNumber());
		if (uPho != null) {
			Map<String, Object> response = new LinkedHashMap<>();
	        response.put("status","failed");
	        response.put("code","509");
	        response.put("error","phone number already exist");
	        return response;
	        }
		
		else {
			
		umDAO.save(userEnt);

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		userEnt.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		umDAO.save(userEnt);
                        
		Map<String, Object> response = new LinkedHashMap<>();
        response.put("status","success");
        response.put("id",userEnt.getUserId());
        return response;
		}
		
	}
	

	@PostMapping("/login-authenticate")
	public LoginTokenDTO Login_Authenticate(@RequestBody UserMasterEntity jwtRequest) throws Exception {

		LoginTokenDTO tok = new LoginTokenDTO();
	
	
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(jwtRequest.getUserName(), jwtRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID USER NAME Or PASSWORD",e);
			
		}

	
		final UserDetails userDetails = uService.loadUserByUsername(jwtRequest.getUserName());

		
		final String token = jwtUtility.generateToken(userDetails);
		Optional<UserMasterEntity> user = umDAO.findUserByName(jwtRequest.getUserName());
		

		tok.setRole(user.get().getRole());
		tok.setOrganisationId(user.get().getOrganisationId());
		tok.setOrganisationName(user.get().getOrganisationName());
		tok.setPhoneNumber(user.get().getPhoneNumber());

		tok.setUserId(user.get().getUserId());
		jwtService.storeJWT(user.get().getUserId().toString(), token,user.get().getRole());
		tok.setToken(token);
		return tok;
		
	}
	

	
	
	
	
	
	
}
