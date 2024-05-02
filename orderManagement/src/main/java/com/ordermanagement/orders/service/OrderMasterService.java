package com.ordermanagement.orders.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ordermanagement.orders.dto.OrderMasterDTO;
import com.ordermanagement.orders.entity.OrderMasterEntity;
import com.ordermanagement.orders.repository.OrderMasterRepository;
import com.ordermanagement.products.entity.ProductMasterEntity;

@Component
public class OrderMasterService {
	
	@Autowired
	private OrderMasterRepository omRepo;

	public Map<String, Object> AddOrderMaster(OrderMasterDTO omDTO) {
		return this.omRepo.AddOrderMaster(omDTO);
	}

	public List<OrderMasterEntity> SearchByOrderId(String orderId) {
		return this.omRepo.SearchByProductName(orderId);
	}

}
