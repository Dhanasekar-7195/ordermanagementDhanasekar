//package com.ordermanagement.deliveries.controller;
//
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.ordermanagement.deliveries.dto.DeliveryMasterDTO;
//import com.ordermanagement.deliveries.entity.DeliveryMasterEntity;
//import com.ordermanagement.deliveries.service.DeliveryMasterService;
//
//import io.swagger.annotations.Api;
//
//@RestController
//@RequestMapping("api/delivery_master")
//@Api(description = "Delivery Master Service", tags = { "DeliveryMasterAPI" })
//public class DeliveryMasterController {
//
//	@Autowired
//	private DeliveryMasterService dmService;
//
//	@PostMapping("/add_delivery_master")
//	public Map<String, Object> addDeliveryMaster(@RequestBody DeliveryMasterDTO dmDTO) {
//		return this.dmService.addDeliveryMaster(dmDTO);
//	}
//
//	@PutMapping("/update_delivery_master")
//	public Map<String, Object> updateDeliveryMaster(@RequestBody DeliveryMasterDTO dmDTO) {
//		return this.dmService.updateDeliveryMaster(dmDTO);
//	}
//	
//	@PutMapping("/update_delivered")
//	public Map<String, Object> updateDelivered(@RequestBody DeliveryMasterDTO dmDTO) {
//		return this.dmService.updateDelivered(dmDTO);
//	}
//
//	@GetMapping("/get_all_deliverymaster")
//	public List<DeliveryMasterEntity> getAllDeliveryMaster() {
//		return this.dmService.getAllDeliveryMaster();
//	}
//
//}
