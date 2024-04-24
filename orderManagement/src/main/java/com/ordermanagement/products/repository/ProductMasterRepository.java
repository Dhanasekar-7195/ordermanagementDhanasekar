package com.ordermanagement.products.repository;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ordermanagement.products.dao.ProductMasterDAO;
import com.ordermanagement.products.dto.ProductMasterDTO;
import com.ordermanagement.products.entity.ProductMasterEntity;
import com.ordermanagement.sampleexception.UserNotFoundException;




@Component
public class ProductMasterRepository {
	
	@Autowired
	private ProductMasterDAO pmDAO;
	
	public Map<String, Object> AddProductMaster(ProductMasterDTO productDTO) {
		ProductMasterEntity prodEnt= new ProductMasterEntity();
			if(productDTO != null &&
					productDTO.getProductName() != null && 
			        productDTO.getCategory() != null && 
			        productDTO.getTAX() != null && 
			        productDTO.getUnit() != null && 
			        productDTO.getPrice() > 0 &&
			        productDTO.getDiscount() != null) {
		prodEnt.setProductName(productDTO.getProductName());
		prodEnt.setCategory(productDTO.getCategory());
		prodEnt.setSubCategory(productDTO.getSubCategory());
		prodEnt.setTAX(productDTO.getTAX());
		prodEnt.setUnit(productDTO.getUnit());
		prodEnt.setPrice(productDTO.getPrice());
		prodEnt.setDiscount(productDTO.getDiscount());
		
		pmDAO.save(prodEnt);
		
			Map<String, Object> response = new LinkedHashMap<>();
			response.put("status", "success");
			response.put("id", prodEnt.getProdId());
			return response;
			}
			else {
			Map<String, Object> response = new LinkedHashMap<>();
	        response.put("error", "Failed to process the request");
	        return response;
		}
		
	}
	
	
	
	public Map<String, Object> UpdateProductMaster(ProductMasterDTO productDTO) {
	    String productName = productDTO.getProductName(); // Get the product name from the DTO
	    Optional<ProductMasterEntity> optionalProduct = pmDAO.findByProductName(productName); // Assuming findByProductName method exists in productMasterDAO
	    if (optionalProduct.isPresent()) {
	        ProductMasterEntity prodEnt = optionalProduct.get();
	        // Update product details as before
	        prodEnt.setCategory(productDTO.getCategory());
	        prodEnt.setSubCategory(productDTO.getSubCategory());
	        prodEnt.setTAX(productDTO.getTAX());
	        prodEnt.setUnit(productDTO.getUnit());
	        prodEnt.setPrice(productDTO.getPrice());
	        prodEnt.setDiscount(productDTO.getDiscount());

	        pmDAO.save(prodEnt);

	        Map<String, Object> response = new LinkedHashMap<>();
	        response.put("status", "success");
	        response.put("id", prodEnt.getProdId()); // If needed, return the database ID of the updated product
	        return response;
	    } else {
	        Map<String, Object> response = new LinkedHashMap<>();
	        response.put("error", "Product not found with name: " + productName);
	        return response;
	    }
	}
	
	public ProductMasterDTO GetProductMasterById(String prodId)throws UserNotFoundException {
		ProductMasterEntity pmENT = pmDAO.getCuDById(prodId);
		ProductMasterDTO pmDTO= new ProductMasterDTO();
		if (pmENT!=null) {
		pmDTO.setProdId(pmENT.getProdId());
		pmDTO.setProductName(pmENT.getProductName());
		pmDTO.setCategory(pmENT.getCategory());
		pmDTO.setSubCategory(pmENT.getSubCategory());
		pmDTO.setTAX(pmENT.getTAX());
		pmDTO.setUnit(pmENT.getUnit());
		pmDTO.setPrice(pmENT.getPrice());
		pmDTO.setDiscount(pmENT.getDiscount());
		return pmDTO;
		}
		else
		{
			throw new UserNotFoundException("user not found");				
		}
	}

	                                            																	
	public List<ProductMasterEntity> GetAllProductDetails() {
		List<ProductMasterEntity> vmList= pmDAO.findAll();
		return vmList;
	}
	public Map<String, Object> DeleteProductMasterById(String prodId) {
		if(pmDAO.getCuDById(prodId)!= null) {
			pmDAO.deleteById(prodId);
			Map<String, Object> response = new LinkedHashMap<>();
			response.put("status","success");
			return response;
			}
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("status","204");
		response.put("id","not_found");
		return response;	
		}
	

	
	

	

	

	


}
