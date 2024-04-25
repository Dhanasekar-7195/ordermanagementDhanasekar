package com.ordermanagement.products.controller;





import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordermanagement.products.dto.ProductMasterDTO;
import com.ordermanagement.products.entity.ProductMasterEntity;
import com.ordermanagement.products.service.ProductMasterService;
import com.ordermanagement.sampleexception.ProductNotFoundException;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("api/productmaster")
@Api(description = "Productmaster Service", tags = { "ProductmasterAPI" })  

public class ProductMasterController {
	
	@Autowired
	private ProductMasterService pmService;
	
	@PostMapping("/add_productmaster")
	public Map<String, Object> AddProductMaster(@RequestBody ProductMasterDTO pmDTO) {
		return this.pmService.AddProductMaster(pmDTO);
	}
	
	@PutMapping("/update_productmaster")
	public Map<String, Object> UpdateProductMaster(@RequestBody ProductMasterDTO pmDTO) {
		return this.pmService.UpdateProductMaster(pmDTO);        
	}
	
	@GetMapping("/get_productmaster_by_id/{prodId}")
	public ProductMasterDTO GetProductMasterById(@PathVariable String prodId) throws ProductNotFoundException{
		return this.pmService.GetProductMasterById(prodId);
	}
	
	@GetMapping("/get_all_productmaster")
	public List<ProductMasterEntity> GetAllProductMaster() {
		return this.pmService.GetAllProductMaster();
	}
	
	
	@DeleteMapping("/delete_productmaster_by_id/{prodId}")
	public Map<String, Object> DeleteProductMasterById(@PathVariable String prodId) {
		return this.pmService.DeleteProductMasterById(prodId);
	}
	
	@GetMapping("/get_productmaster_by_productname/{productName}")
	public ProductMasterDTO GetProductMasterByProductName(@PathVariable String productName) throws ProductNotFoundException{
		return this.pmService.GetProductMasterByProductName(productName);
	}
	
	@GetMapping("/search_by_productname/{productName}")
	public List<ProductMasterEntity> SearchByProductName(@PathVariable String productName) {
		return this.pmService.SearchByProductName(productName);
	}
	
	
	
	
	
	


	
	
	
	
}
