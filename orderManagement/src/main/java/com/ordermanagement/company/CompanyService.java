package com.ordermanagement.company;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//PHASE 1

@Component
public class CompanyService {

	@Autowired
	private CompanyRepository cRepo;

	public Map<String, Object> addCompany(CompanyDTO cDTO) {
		return this.cRepo.addCompany(cDTO);
	}

}
