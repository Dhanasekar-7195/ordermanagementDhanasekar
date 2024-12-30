package com.ordermanagement.products.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ordermanagement.customers.entity.SaveAllCustomerEntity;
import com.ordermanagement.products.dto.ProductMasterDTO;
import com.ordermanagement.products.entity.ProductMasterEntity;
import com.ordermanagement.products.entity.SaveAllProductEntity;
import com.ordermanagement.products.repository.ProductMasterRepository;
import com.ordermanagement.sampleexception.ProductNotFoundException;

@Component
public class ProductMasterService {

	@Autowired
	private ProductMasterRepository pmRepo;

	public Map<String, Object> addProductMaster(ProductMasterDTO pmDTO) {
		return this.pmRepo.addProductMaster(pmDTO);
	}

	public Map<String, Object> updateProductMaster(ProductMasterDTO pmDTO) {
		return this.pmRepo.updateProductMaster(pmDTO);
	}

	public ProductMasterDTO getProductMasterById(String prodId) throws ProductNotFoundException {
		return this.pmRepo.getProductMasterById(prodId);
	}

	public List<ProductMasterEntity> getAllProductMaster() {
		return this.pmRepo.getAllProductDetails();
	}

	public Map<String, Object> deleteProductMasterById(String prodId) {
		return this.pmRepo.deleteProductMasterById(prodId);
	}

	public ProductMasterDTO getProductMasterByProductName(String productName) throws ProductNotFoundException {
		return this.pmRepo.getProductMasterByProductName(productName);
	}

	public List<ProductMasterEntity> searchByProductName(String productName) {
		return this.pmRepo.searchByProductName(productName);
	}

	public ResponseEntity<Object> getAllProductData() {
        return this.pmRepo.getAllProductData();
	}

	public String getAndSaveAllProductData() {
        return this.pmRepo.getAndSaveAllProductData();

	}

	public List<SaveAllProductEntity> getAllS4hanaProductMaster() {
		return this.pmRepo.getAllS4hanaProductMaster();
	}

}
