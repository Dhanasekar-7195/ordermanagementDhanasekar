package com.ordermanagement.company;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

//PHASE 1

public interface CompanyDAO extends JpaRepository<CompanyEntity, String> {

	@Query(value = "Select * FROM company where companyName=?1", nativeQuery = true)
	CompanyEntity findCompanyByCompanyName(String companyName);

	CompanyEntity findCompanyByEmail(String email);

	CompanyEntity findCompanyByMobileNumber(String mobileNumber);

	@Query(value = "SELECT * FROM company WHERE userName = ?1", nativeQuery = true)
	List<CompanyEntity> findNameByList(String userName);

}
