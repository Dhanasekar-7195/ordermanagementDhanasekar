package com.ordermanagement.orders.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ordermanagement.orders.dto.OrderMasterDTO;
import com.ordermanagement.orders.entity.OrderMasterEntity;
import com.ordermanagement.orders.repository.OrderMasterRepository;

@Component
public class OrderMasterService {

	@Autowired
	private OrderMasterRepository omRepo;

	public Map<String, Object> addOrderMaster(OrderMasterDTO omDTO) {
		return this.omRepo.addOrderMaster(omDTO);
	}

//	public Map<String, Object> addUpdateDeleteOrderMaster(OrderMasterDTO omDTO) {
//		return this.omRepo.addUpdateDeleteOrderMaster(omDTO);
//	}

	public List<OrderMasterEntity> searchByOrderId(String orderId) {
		return this.omRepo.searchByProductName(orderId);
	}

	public List<OrderMasterEntity> getAllOrderMaster() {
		return this.omRepo.getAllOrderMaster();
	}

//	public Map<String, Object> updateOrderMaster(OrderMasterDTO omDTO) {
//		return this.omRepo.updateOrderMaster(omDTO);
//	}

	public List<OrderMasterEntity> getOrdersByCustomerId(String customerId) {
		return this.omRepo.getOrdersByCustomerId(customerId);
	}

//	public Map<String, Object> deleteByOrderId( String orderId) {
//		return this.omRepo.deleteByOrderId(orderId);
//	}

	public List<OrderMasterEntity> getAllDraftMaster() {
		return this.omRepo.getAllDraftMaster();

	}

	public Map<String, Double> getOrderCounts() {
		return this.omRepo.getOrderCounts();
	}

	public Map<String, Double> getCustomerOrderCounts(String customerId) {
		// TODO Auto-generated method stub
		return this.omRepo.getCustomerOrderCounts(customerId);
	}

//	public String deleteAllOrderMaster() {
//		return this.omRepo.deleteAllOrderMaster();
//	}

//	public OrderMasterEntity getDraftById(String draftId) {
//		return this.omRepo.getDraftById(draftId);
//	}

//	public void deleteById(String draftId) {
//		// TODO Auto-generated method stub
//		
//	}



}
