//package com.ordermanagement.payments.controller;
//
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.ordermanagement.payments.dto.SavePaymentDTO;
//import com.ordermanagement.payments.entity.PaymentMasterEntity;
//import com.ordermanagement.payments.entity.TransactionsMasterEntity;
//import com.ordermanagement.payments.service.PaymentMasterService;
//
//import io.swagger.annotations.Api;
//
//@RestController
//@RequestMapping("api/payment_master")
//@Api(description = "Payment Master Service", tags = { "PaymentMasterAPI" })
//public class PaymentMasterController {
//
//	@Autowired
//	private PaymentMasterService pmService;
//
//	@PostMapping("/add_payment_master/{amountToPay}")
//	public Map<String, Object> addPaymentMaster(@RequestBody SavePaymentDTO spDTO, @PathVariable double amountToPay) {
//		return this.pmService.addPaymentMaster(spDTO, amountToPay);
//	}
//
//	@GetMapping("/get_all_paymentmaster")
//	public List<PaymentMasterEntity> getAllPaymentMaster() {
//		return this.pmService.getAllPaymentMaster();
//	}
//
//	@GetMapping("/get_all_transactionmaster/{orderId}")
//	public List<TransactionsMasterEntity> getAllTransactionMaster(@PathVariable String orderId) {
//		return this.pmService.getAllTransactionMaster(orderId);
//	}
//
//}
