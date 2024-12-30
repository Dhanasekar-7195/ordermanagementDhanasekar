package com.ordermanagement.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class LoginTokenDTO {

	private String token;

	private String role;

	private String userId;

	private String location;

	private String companyName;

	private String mobileNumber;

}
