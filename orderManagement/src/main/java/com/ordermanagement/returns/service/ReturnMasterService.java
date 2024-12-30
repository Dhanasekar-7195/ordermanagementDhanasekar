//package com.ordermanagement.returns.service;
//
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.ordermanagement.returns.dto.ReturnMasterDTO;
//import com.ordermanagement.returns.entity.ReturnMasterEntity;
//import com.ordermanagement.returns.repository.ReturnMasterRepository;
//
//@Component
//public class ReturnMasterService {
//
//	@Autowired
//	private ReturnMasterRepository rmRepo;
//
//	public Map<String, Object> addReturnMaster(ReturnMasterDTO rmDTO) {
//		return this.rmRepo.addReturnMaster(rmDTO);
//	}
//
//	public List<ReturnMasterEntity> getAllReturnMaster() {
//		return this.rmRepo.getAllReturnMaster();
//	}
//
//}
