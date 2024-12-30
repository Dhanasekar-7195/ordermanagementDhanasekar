package com.ordermanagement.products.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordermanagement.customers.entity.SaveAllCustomerEntity;
import com.ordermanagement.products.dto.ProductMasterDTO;
import com.ordermanagement.products.entity.ProductMasterEntity;
import com.ordermanagement.products.entity.SaveAllProductEntity;
import com.ordermanagement.products.service.ProductMasterService;
import com.ordermanagement.sampleexception.ProductNotFoundException;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("api/public/productmaster")
@Api(description = "Productmaster Service", tags = { "ProductmasterAPI" })

public class ProductMasterController {

	@Autowired
	private ProductMasterService pmService;

	@PostMapping("/add_productmaster")
	public Map<String, Object> addProductMaster(@PathVariable String tenantId,@RequestBody ProductMasterDTO pmDTO) {
		return this.pmService.addProductMaster(pmDTO);
	}

	@PutMapping("/update_productmaster")
	public Map<String, Object> updateProductMaster(@PathVariable String tenantId,@RequestBody ProductMasterDTO pmDTO) {
		return this.pmService.updateProductMaster(pmDTO);
	}

	@GetMapping("/get_productmaster_by_id/{prodId}")
	public ProductMasterDTO getProductMasterById(@PathVariable String tenantId,@PathVariable String prodId) throws ProductNotFoundException {
		return this.pmService.getProductMasterById(prodId);
	}

	@GetMapping("/get_all_productmaster")
	public List<ProductMasterEntity> getAllProductMaster(@PathVariable String tenantId) {
		return this.pmService.getAllProductMaster();
	}

	@DeleteMapping("/delete_productmaster_by_id/{prodId}")
	public Map<String, Object> deleteProductMasterById(@PathVariable String tenantId,@PathVariable String prodId) {
		return this.pmService.deleteProductMasterById(prodId);
	}

	@GetMapping("/get_productmaster_by_productname/{productName}")
	public ProductMasterDTO getProductMasterByProductName(@PathVariable String tenantId,@PathVariable String productName)
			throws ProductNotFoundException {
		return this.pmService.getProductMasterByProductName(productName);
	}

	@GetMapping("/search_by_productname/{productName}")
	public List<ProductMasterEntity> searchByProductName(@PathVariable String tenantId,@PathVariable String productName) {
		return this.pmService.searchByProductName(productName);
	}

	@GetMapping("/get_all_product_data")
	public ResponseEntity<Object> getAllProductData(@PathVariable String tenantId) {
		return pmService.getAllProductData();
	}
	
	
	
	@GetMapping("/get_all_s4hana_productmaster")
	public List<SaveAllProductEntity> getAllS4hanaProductMaster() {
		return pmService.getAllS4hanaProductMaster();
	}

}
