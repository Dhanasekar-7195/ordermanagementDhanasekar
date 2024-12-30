package com.ordermanagement.products.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ordermanagement.products.entity.ProductMasterEntity;

public interface ProductMasterDAO extends JpaRepository<ProductMasterEntity, String> {

	@Query(value = "Select * From Product_Master where prodId=?1", nativeQuery = true)
	ProductMasterEntity getPrDById(String prodId);

	@Query(value = "Select * From Product_Master where Product_Name=?1", nativeQuery = true)
	Optional<ProductMasterEntity> findByProductName(String productName);

	@Query(value = "Select * From Product_Master where Product_Name=?1", nativeQuery = true)
	ProductMasterEntity getPrDByProductName(String prodId);

	@Query(value = "SELECT * FROM product_master WHERE Product_Name LIKE ?1%", nativeQuery = true)
	List<ProductMasterEntity> getAllProductById(String productName);

	@Query(value = "SELECT * FROM product_master WHERE Product_Name = :productName AND Category = :category AND Sub_Category = :subCategory", nativeQuery = true)
	ProductMasterEntity findByProductNameAndCategoryAndSubCategory(String productName, String category,
			String subCategory);
	
	@Query(value = "SELECT * FROM product_master WHERE Product_Name = ?2 AND Category = ?3 AND Sub_Category = ?4 AND prodId != ?1", nativeQuery = true)
	ProductMasterEntity findByProductNameAndCategoryAndSubCategoryExcludeProdId(String prodId, String productName, String category, String subCategory);

}
