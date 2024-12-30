package com.ordermanagement.company;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//PHASE 1

@RestController
@RequestMapping("/api/public/company")
public class CompanyController {

	@Autowired
	private CompanyService cServ;

	@PostMapping("/add")
	public Map<String, Object> addCompany(@RequestBody CompanyDTO cDTO) {

		return this.cServ.addCompany(cDTO);
	}

}
