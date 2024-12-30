//package com.ordermanagement.payments.service;
//
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.ordermanagement.payments.dto.SavePaymentDTO;
//import com.ordermanagement.payments.entity.PaymentMasterEntity;
//import com.ordermanagement.payments.entity.TransactionsMasterEntity;
//import com.ordermanagement.payments.repository.PaymentMasterRepository;
//
//@Component
//public class PaymentMasterService {
//
//	@Autowired
//	private PaymentMasterRepository pmRepo;
//
//	public Map<String, Object> addPaymentMaster(SavePaymentDTO spDTO, double amountToPay) {
//		return this.pmRepo.addPaymentMaster(spDTO, amountToPay);
//
//	}
//
//	public List<PaymentMasterEntity> getAllPaymentMaster() {
//		return this.pmRepo.getAllPaymentMaster();
//	}
//
//	public List<TransactionsMasterEntity> getAllTransactionMaster(String orderId) {
//		return this.pmRepo.getAllTransactionMaster(orderId);
//	}
//
//}
