package com.ordermanagement.orders.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordermanagement.orders.dto.OrderMasterDTO;
import com.ordermanagement.orders.entity.OrderMasterEntity;
import com.ordermanagement.orders.service.OrderMasterService;
import com.ordermanagement.products.entity.ProductMasterEntity;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("api/order_master")
@Api(description = "Order Master Service", tags = { "OrderMasterAPI" })   

public class OrderMasterController {
	
	@Autowired
	private OrderMasterService omService;
	
	@PostMapping("/add_order_master")
	public Map<String, Object> AddOrderMaster(@RequestBody OrderMasterDTO omDTO) {
		return this.omService.AddOrderMaster(omDTO);
	}
	
	@GetMapping("/search_by_orderid/{orderId}")
	public List<OrderMasterEntity> SearchByOrderId(@PathVariable String orderId) {
		return this.omService.SearchByOrderId(orderId);
	}
	
	@GetMapping("/get_all_ordermaster")
	public List<OrderMasterEntity> GetAllOrderMaster() {
		return this.omService.GetAllOrderMaster();
	}
	
	@PutMapping("/add_update_delete_order_master")
	public Map<String, Object> AddUpdateDeleteOrderMaster(@RequestBody OrderMasterDTO omDTO) {
		return this.omService.AddUpdateDeleteOrderMaster(omDTO);
	}
	
	@PutMapping("/update_order_master")
	public Map<String, Object> UpdateOrderMaster(@RequestBody OrderMasterDTO omDTO) {
		return this.omService.UpdateOrderMaster(omDTO);
	}
	
	
	

}
