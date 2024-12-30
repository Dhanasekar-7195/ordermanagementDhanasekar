//package com.ordermanagement.returns.controller;
//
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.ordermanagement.returns.dto.ReturnMasterDTO;
//import com.ordermanagement.returns.entity.ReturnMasterEntity;
//import com.ordermanagement.returns.service.ReturnMasterService;
//
//import io.swagger.annotations.Api;
//
//@RestController
//@RequestMapping("api/return_master")
//@Api(description = "Return Master Service", tags = { "ReturnMasterAPI" })
//public class ReturnMasterController {
//
//	@Autowired
//	private ReturnMasterService rmService;
//
//	@PostMapping("/add_return_master")
//	public Map<String, Object> addReturnMaster(@RequestBody ReturnMasterDTO rmDTO) {
//		return this.rmService.addReturnMaster(rmDTO);
//	}
//
//	@GetMapping("/get_all_returnmaster")
//	public List<ReturnMasterEntity> getAllReturnMaster() {
//		return this.rmService.getAllReturnMaster();
//	}
//
//}
