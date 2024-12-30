package com.ordermanagement.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//PHASE 1

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {

	private String tenantId;
	private String companyName;
	private String email;
	private String userName;
	private String password;
	private String mobileNumber;

}
