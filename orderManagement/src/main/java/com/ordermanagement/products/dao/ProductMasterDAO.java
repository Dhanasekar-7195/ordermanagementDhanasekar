package com.ordermanagement.products.dao;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ordermanagement.products.entity.ProductMasterEntity;


public interface ProductMasterDAO extends JpaRepository<ProductMasterEntity, String>{
	
	@Query(value = "Select * From Product_Master where prodId=?1", nativeQuery = true)
	ProductMasterEntity getCuDById(String prodId);
	    
    @Query(value = "Select * From Product_Master where Product_Name=?1", nativeQuery = true)
	Optional<ProductMasterEntity> findByProductName(String productName);

	


	


}



	


