package com.ordermanagement.orders.controller;

import java.util.LinkedHashMap;
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

import com.ordermanagement.orders.dao.OrderMasterDAO;
import com.ordermanagement.orders.dto.OrderMasterDTO;
import com.ordermanagement.orders.entity.OrderMasterEntity;
import com.ordermanagement.orders.service.OrderMasterService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("api/{tenantId}/order_master")
@Api(description = "Order Master Service", tags = { "OrderMasterAPI" })

public class OrderMasterController {

	@Autowired
	private OrderMasterService omService;
	
	
	@PostMapping("/add_order_master")
	public Map<String, Object> addOrderMaster(@RequestBody OrderMasterDTO omDTO) {
		return this.omService.addOrderMaster(omDTO);
	}

	@GetMapping("/search_by_orderid/{orderId}")
	public List<OrderMasterEntity> searchByOrderId(@PathVariable String tenantId,@PathVariable String orderId) {
		return this.omService.searchByOrderId(orderId);
	}

	@GetMapping("/get_all_ordermaster")
	public List<OrderMasterEntity> getAllOrderMaster(@PathVariable String tenantId) {
		return this.omService.getAllOrderMaster();
	}

//	@PutMapping("/add_update_delete_order_master")
//	public Map<String, Object> addUpdateDeleteOrderMaster(@RequestBody OrderMasterDTO omDTO) {
//		return this.omService.addUpdateDeleteOrderMaster(omDTO);
//	}

//	@PutMapping("/update_order_master")
//	public Map<String, Object> updateOrderMaster(@RequestBody OrderMasterDTO omDTO) {
//		return this.omService.updateOrderMaster(omDTO);
//	}

	@GetMapping("/get_all_ordermaster_by_customer/{customerId}")
	public List<OrderMasterEntity> getOrdersByCustomerId(@PathVariable String tenantId,@PathVariable String customerId) {
		return this.omService.getOrdersByCustomerId(customerId);
	}
	
	@GetMapping("/get_all_draft_master")
	public List<OrderMasterEntity> getAllDraftMaster(@PathVariable String tenantId) {
		return this.omService.getAllDraftMaster();
	}
	
//	@DeleteMapping("/delete_ordermaster_by_id/{orderId}")
//	public Map<String, Object> deleteByOrderId(@PathVariable String orderId) {
//		return this.omService.deleteByOrderId(orderId);
//	}
	
//	@DeleteMapping("/delete_ordermaster_by_id/{draftId}")
//	public Map<String, Object> deleteUserMasterById(@PathVariable String draftId) {
//		OrderMasterEntity getDraftById = omService.getDraftById(draftId);
//		if (getDraftById != null && !"-".equals(getDraftById.getStatus())) {
//			omDAO.deleteById(draftId);
//			Map<String, Object> response = new LinkedHashMap<>();
//			response.put("status", "success");
//			return response;
//		}
//		Map<String, Object> response = new LinkedHashMap<>();
//		response.put("status", "204");
//		response.put("id", "not_found");
//		return response;
//	}
	
	@GetMapping("/get_order_counts")
	public Map<String, Double> getOrderCounts() {
		return this.omService.getOrderCounts();
	}
	
	@GetMapping("/get_customer_order_counts/{customerId}")
	public Map<String, Double> getCustomerOrderCounts(@PathVariable String customerId) {
		return this.omService.getCustomerOrderCounts(customerId);
	}


}
