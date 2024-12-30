package com.ordermanagement.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class ChangePasswordDTO {
	private String otp;
	private String newPassword;
	private String confirmPassword;

}
