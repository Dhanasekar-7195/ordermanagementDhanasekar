package com.ordermanagement.user.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class UserMasterDTO {
	
	private String userId;
	
	private String userName;
	
	private String password;
	
	private boolean active;
	
	private String role;
	
	private String email;
	
	private String token;
	
	private String OrganisationId;
	
	private String OrganisationName;

	private LocalDateTime tokenCreationDate;
	
	private String phoneNumber;

}
