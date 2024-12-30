//package com.ordermanagement.payments.repository;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.ordermanagement.customers.dao.CustomerMasterDAO;
//import com.ordermanagement.customers.entity.CustomerMasterEntity;
//import com.ordermanagement.dashboard.dao.DashBoardDAO;
//import com.ordermanagement.dashboard.entity.DashBoardEntity;
//import com.ordermanagement.invoices.dao.InvoiceMasterDAO;
//import com.ordermanagement.invoices.entity.InvoiceMasterEntity;
//import com.ordermanagement.orders.dao.OrderMasterDAO;
//import com.ordermanagement.orders.entity.OrderMasterEntity;
//import com.ordermanagement.payments.dao.PaymentMasterDAO;
//import com.ordermanagement.payments.dao.TransactionsMasterDAO;
//import com.ordermanagement.payments.dto.SavePaymentDTO;
//import com.ordermanagement.payments.entity.PaymentMasterEntity;
//import com.ordermanagement.payments.entity.TransactionsMasterEntity;
//
//@Component
//public class PaymentMasterRepository {
//
//	@Autowired
//	private PaymentMasterDAO pmDAO;
//
//	@Autowired
//	private OrderMasterDAO omDAO;
//
//	@Autowired
//	private TransactionsMasterDAO tmDAO;
//
//	@Autowired
//	private DashBoardDAO dbDAO;
//
//	@Autowired
//	private CustomerMasterDAO cmDAO;
//	
//	@Autowired
//	private InvoiceMasterDAO imDAO;
//
//	public Map<String, Object> addPaymentMaster(SavePaymentDTO spDTO, double amountToPay) {
//		String invoiceNo = spDTO.getInvoice();
//		PaymentMasterEntity pmENT = pmDAO.getPaymentListByInvoiceNo(invoiceNo);
//		TransactionsMasterEntity tmENT = tmDAO.getLatestPaymentListByInvoiceNo(invoiceNo);
//		TransactionsMasterEntity tmEntity = new TransactionsMasterEntity();
//		TransactionsMasterEntity tmDEL = tmDAO.getInitialPaymentListByInvoiceNo(invoiceNo);
//
//		if (pmENT == null && tmENT == null) {
//			Map<String, Object> errorResponse = new LinkedHashMap<>();
//			errorResponse.put("status", "failure");
//			errorResponse.put("message", "Payment record not found for invoice number: " + invoiceNo);
//			return errorResponse;
//
//		}
//
//		double paidAmount = tmENT.getPaidAmount() + amountToPay;
//
//		if (paidAmount > tmENT.getGrossAmount()) {
//			Map<String, Object> errorResponse = new LinkedHashMap<>();
//			errorResponse.put("status", "failure");
//			errorResponse.put("message", "Paid amount exceeds the gross amount.");
//			return errorResponse;
//		}
//
//		tmEntity.setOrderId(tmENT.getOrderId());
//		tmEntity.setDeliveryId(tmENT.getDeliveryId());
//		tmEntity.setDeliveredDate(tmENT.getDeliveredDate());
//		tmEntity.setCustomerName(tmENT.getCustomerName());
//		tmEntity.setInvoiceNo(spDTO.getInvoice());
//		tmEntity.setGrossAmount(spDTO.getGrossAmount());
//		tmEntity.setPaymentMode(spDTO.getPaymentMode());
//		tmEntity.setCustomerId(pmENT.getCustomerId());
//		if(spDTO.getUserId() != "") {
//		tmEntity.setPaidBy("Customer");
//		}
//		else {
//			tmEntity.setPaidBy("Employee");
//		}
//		LocalDate currentDate = LocalDate.now();
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//		String formattedDate = currentDate.format(formatter);
//		tmEntity.setPaymentDate(formattedDate);
//
//		double exactPaidAmount = paidAmount - (tmENT.getPaidAmount());
//		tmEntity.setExactPaidAmount(exactPaidAmount);
//
//		tmEntity.setPaidAmount(paidAmount);
//
//		double payableAmount = tmENT.getGrossAmount() - paidAmount;
//		tmEntity.setPayableAmount(payableAmount);
//
//		if (payableAmount == 0) {
//			tmEntity.setPaymentStatus("cleared");
//			tmDAO.delete(tmDEL);
//		} else {
//			tmEntity.setPaymentStatus("partial payment");
//		}
//
//		tmDAO.save(tmEntity);
//
//		//pmENT.setPayableAmount(payableAmount);
//		pmENT.setPayableAmount(tmEntity.getPayableAmount());
//		pmENT.setPaidAmount(paidAmount);
//		pmENT.setPaymentDate(formattedDate);
//		pmENT.setPaymentMode(spDTO.getPaymentMode());
//		pmENT.setPaymentStatus(tmEntity.getPaymentStatus());
//		if (pmENT.getPaymentMode().equals("creditLimit")) {
//			pmENT.setReturnCredit(pmENT.getReturnCredit() - pmENT.getPaidAmount());
//			CustomerMasterEntity cmENT = cmDAO.getAllCustomerById(pmENT.getCustomerId());
//			cmENT.setReturnCredit(pmENT.getReturnCredit() - pmENT.getPaidAmount());
//		}
//		pmDAO.save(pmENT);
//
//		OrderMasterEntity omENT = omDAO.getOrderById(tmENT.getOrderId());
//		omENT.setPaymentId(pmENT.getPaymentId());
//		omENT.setPayableAmount(payableAmount);
//		omENT.setPaidAmount(paidAmount);
//		omENT.setPaymentDate(formattedDate);
//		omENT.setPaymentMode(spDTO.getPaymentMode());
//		omENT.setPaymentStatus(tmEntity.getPaymentStatus());
//		omDAO.save(omENT);
//		
//		InvoiceMasterEntity imENT = imDAO.getInvoiceByOrderId(tmENT.getOrderId());
//		imENT.setPaymentId(pmENT.getPaymentId());
//		imENT.setPayableAmount(payableAmount);
//		imENT.setPaidAmount(paidAmount);
//		imENT.setPaymentDate(formattedDate);
//		imENT.setPaymentMode(spDTO.getPaymentMode());
//		imENT.setPaymentStatus(tmEntity.getPaymentStatus());
//		imDAO.save(imENT);
//
//		DashBoardEntity dbENT = dbDAO.getDashBoardDetailsByOrderId(tmENT.getOrderId());
//		dbENT.setPaymentStatus(tmEntity.getPaymentStatus());
//		dbDAO.save(dbENT);
//
//		Map<String, Object> response = new LinkedHashMap<>();
//		response.put("status", "success");
//		response.put("id", pmENT.getPaymentId());
//		return response;
//	}
//
//	public List<PaymentMasterEntity> getAllPaymentMaster() {
//		List<PaymentMasterEntity> paymentList = pmDAO.findAll();
//		return paymentList;
//	}
//
//	public List<TransactionsMasterEntity> getAllTransactionMaster(String orderId) {
//		List<TransactionsMasterEntity> transactionList = tmDAO.getTransactionMasterByOrderId(orderId);
//		return transactionList;
//	}
//
//}
