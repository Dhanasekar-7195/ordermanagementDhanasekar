package com.ordermanagement.products.service;






import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ordermanagement.products.dto.ProductMasterDTO;
import com.ordermanagement.products.entity.ProductMasterEntity;
import com.ordermanagement.products.repository.ProductMasterRepository;
import com.ordermanagement.sampleexception.UserNotFoundException;


@Component
public class ProductMasterService {
	
	@Autowired
	private ProductMasterRepository pmRepo;
	
	public Map<String, Object> AddProductMaster(ProductMasterDTO pmDTO) {
		return this.pmRepo.AddProductMaster(pmDTO);
	}
	
	public Map<String, Object> UpdateProductMaster(ProductMasterDTO pmDTO) {
		return this.pmRepo.UpdateProductMaster(pmDTO);
	}
	
	public ProductMasterDTO GetProductMasterById(String prodId)throws UserNotFoundException {
		return this.pmRepo.GetProductMasterById(prodId);
	}
	
	public List<ProductMasterEntity> GetAllProductMaster() {
		return this.pmRepo.GetAllProductDetails();
	}
	
	public Map<String, Object> DeleteProductMasterById(String prodId) {
		return this.pmRepo.DeleteProductMasterById(prodId);
	}
	
	
	
	
}

