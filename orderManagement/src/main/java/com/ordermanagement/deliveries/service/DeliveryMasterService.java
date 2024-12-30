//package com.ordermanagement.deliveries.service;
//
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.ordermanagement.deliveries.dto.DeliveryMasterDTO;
//import com.ordermanagement.deliveries.entity.DeliveryMasterEntity;
//import com.ordermanagement.deliveries.repository.DeliveryMasterRepository;
//
//@Component
//public class DeliveryMasterService {
//
//	@Autowired
//	private DeliveryMasterRepository dmRepo;
//
//	public Map<String, Object> addDeliveryMaster(DeliveryMasterDTO dmDTO) {
//		return this.dmRepo.addDeliveryMaster(dmDTO);
//	}
//
//	public Map<String, Object> updateDeliveryMaster(DeliveryMasterDTO dmDTO) {
//		return this.dmRepo.updateDeliveryMaster(dmDTO);
//	}
//
//	public List<DeliveryMasterEntity> getAllDeliveryMaster() {
//		return this.dmRepo.getAllDeliveryMaster();
//	}
//
//	public Map<String, Object> updateDelivered(DeliveryMasterDTO dmDTO) {
//		return this.dmRepo.updateDelivered(dmDTO);
//	}
//
//}
