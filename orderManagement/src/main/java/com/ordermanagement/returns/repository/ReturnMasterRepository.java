//package com.ordermanagement.returns.repository;
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
//import com.ordermanagement.payments.entity.PaymentMasterEntity;
//import com.ordermanagement.payments.entity.TransactionsMasterEntity;
//import com.ordermanagement.returns.dao.ReturnMasterDAO;
//import com.ordermanagement.returns.dao.ReturnMasterItemDAO;
//import com.ordermanagement.returns.dto.ReturnMasterDTO;
//import com.ordermanagement.returns.dto.ReturnMasterItemDTO;
//import com.ordermanagement.returns.entity.ReturnMasterEntity;
//import com.ordermanagement.returns.entity.ReturnMasterItemEntity;
//import com.ordermanagement.user.dao.UserMasterDAO;
//import com.ordermanagement.user.entity.UserMasterEntity;
//
//@Component
//public class ReturnMasterRepository {
//
//	@Autowired
//	private ReturnMasterDAO rmDAO;
//
//	@Autowired
//	private ReturnMasterItemDAO rmiDAO;
//
//	@Autowired
//	private CustomerMasterDAO cmDAO;
//
//	@Autowired
//	private PaymentMasterDAO pmDAO;
//
//	@Autowired
//	private OrderMasterDAO omDAO;
//
//	@Autowired
//	private DashBoardDAO dbDAO;
//	
//	@Autowired
//	private TransactionsMasterDAO tmDAO;
//	
//	@Autowired
//	private UserMasterDAO umDAO;
//	
//	@Autowired
//	private InvoiceMasterDAO imDAO;
//
//	public Map<String, Object> addReturnMaster(ReturnMasterDTO rmDTO) {
//		ReturnMasterEntity rmENT = new ReturnMasterEntity();
//		rmENT.setReturnId(rmDTO.getReturnId());
//		rmENT.setReturnDate(rmDTO.getReturnDate());
//		rmENT.setInvoiceNumber(rmDTO.getInvoiceNumber());
//		rmENT.setReason(rmDTO.getReason());
//		rmENT.setContactPerson(rmDTO.getContactPerson());
//		rmENT.setEmail(rmDTO.getEmail());
//		rmENT.setReturnCredit(rmDTO.getReturnCredit());
//		rmENT.setNotes(rmDTO.getNotes());
//		rmENT.setCustomerId(rmDTO.getCustomerId());
//		rmENT.setOrderId(rmDTO.getOrderId());
//		rmENT.setContactNumber(rmDTO.getContactNumber());
//		rmENT.setShippingAddress(rmDTO.getShippingAddress());
//		if(rmDTO.getUserId() != "") {
//			rmENT.setInitiatedBy("Customer");
//			}
//			else {
//				rmENT.setInitiatedBy("Employee");
//			}
//		rmDAO.save(rmENT);
//
//		for (ReturnMasterItemDTO itemDTO : rmDTO.getItems()) {
//			ReturnMasterItemEntity item = new ReturnMasterItemEntity();
//			item.setReturnMasterItemId(itemDTO.getReturnMasterItemId());
//			item.setProductName(itemDTO.getProductName());
//			item.setCategory(itemDTO.getCategory());
//			item.setSubCategory(itemDTO.getSubCategory());
//			item.setPrice(itemDTO.getPrice());
//			item.setQty(itemDTO.getQty());
//			item.setReturnQty(itemDTO.getReturnQty());
//			item.setInvoiceAmount(itemDTO.getInvoiceAmount());
//			item.setCreditRequest(itemDTO.getCreditRequest());
//			item.setImageId(itemDTO.getImageId());
//			item.setReturnId(rmENT.getReturnId());
//			item.setTax(itemDTO.getTax());
//			item.setDiscount(itemDTO.getDiscount());
//			rmiDAO.save(item);
//		}
//
//		PaymentMasterEntity pmENT = pmDAO.getPaymentListByInvoiceNo(rmDTO.getInvoiceNumber());
//		double oldReturnCredit = pmENT.getReturnCredit();
//		double newReturnCredit = rmDTO.getReturnCredit();
//		double returnCredit = oldReturnCredit + newReturnCredit;
//		double existGrossAmount = pmENT.getGrossAmount();
//		double existPayableAmount = pmENT.getPayableAmount();
//		double existPaidAmount = pmENT.getPaidAmount();
//		if (existGrossAmount > returnCredit && existPayableAmount == 0) {
//
//			double amount = existGrossAmount - returnCredit;
//			pmENT.setPayableAmount(amount);
//			pmENT.setReturnCredit(0);
//			pmENT.setReturnCreditUsed(returnCredit);
//			double paidAmount = existPaidAmount + returnCredit;
//			pmENT.setPaidAmount(paidAmount);
//			pmENT.setPaymentStatus("partial payment");
//			pmDAO.save(pmENT);
//			
//			TransactionsMasterEntity tmENT = new TransactionsMasterEntity();
//			tmENT.setPaidAmount(paidAmount);
//			tmENT.setInvoiceNo(pmENT.getInvoiceNo());
//			tmENT.setOrderId(pmENT.getOrderId());
//			tmENT.setDeliveryId(pmENT.getDeliveryId());
//			tmENT.setGrossAmount(pmENT.getGrossAmount());
//			tmENT.setPayableAmount(pmENT.getPayableAmount());
//			tmENT.setDeliveredDate(pmENT.getDeliveredDate());
//			LocalDate currentDate = LocalDate.now();
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//			String formattedDate = currentDate.format(formatter);
//			tmENT.setPaymentDate(formattedDate);
//			tmENT.setPaymentMode("return credit");
//			tmENT.setPaymentStatus("partial payment");
//			tmENT.setCustomerName(pmENT.getCustomerName());
//			tmENT.setExactPaidAmount(returnCredit);
//			tmENT.setCustomerId(pmENT.getCustomerId());
//			tmENT.setPaidBy("Credit Used");
//			tmDAO.save(tmENT);
//			
//			if(rmDTO.getCustomerId().startsWith("CUST")) {
//
//			CustomerMasterEntity cmENT = cmDAO.getAllCustomerById(rmDTO.getCustomerId());
//			cmENT.setReturnCredit(0);
//			cmDAO.save(cmENT);
//			}
//			else {
//			UserMasterEntity umENT = umDAO.getUserById(rmDTO.getCustomerId());
//			umENT.setReturnCredit(0);
//			umDAO.save(umENT);
//	}
//
//			OrderMasterEntity omENT = omDAO.getOrderById(pmENT.getOrderId());
//			LocalDate current_date = LocalDate.now();
//			DateTimeFormatter formatteR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//			String formatted_date = current_date.format(formatteR);
//			omENT.setPaymentDate(formatted_date);
//			omENT.setPaymentStatus(pmENT.getPaymentStatus());
//			omENT.setPaymentMode("credit used");
//			omDAO.save(omENT);
//			
//			InvoiceMasterEntity imENT = imDAO.getInvoiceByOrderId(pmENT.getOrderId());
//			LocalDate current_date1 = LocalDate.now();
//			DateTimeFormatter formatteR1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//			String formatted_date1 = current_date1.format(formatteR1);
//			imENT.setPaymentDate(formatted_date1);
//			imENT.setPaymentStatus(pmENT.getPaymentStatus());
//			imENT.setPaymentMode("credit used");
//			imDAO.save(imENT);
//
//			DashBoardEntity dbENT = dbDAO.getDashBoardDetailsByOrderId(pmENT.getOrderId());
//			dbENT.setPaymentStatus(pmENT.getPaymentStatus());
//			dbDAO.save(dbENT);
//			
//			PaymentMasterEntity pmEnt = pmDAO.getPaymentListByInvoiceNo(rmDTO.getInvoiceNumber());
//			pmEnt.setPaymentMode("credit used");
//
//		} else if (existGrossAmount == returnCredit && existPayableAmount == 0) {
//
//			pmENT.setReturnCredit(0);
//			pmENT.setReturnCreditUsed(returnCredit);
//			pmENT.setPaymentStatus("cleared");
//			double paidAmount = existPaidAmount + returnCredit;
//			pmENT.setPaidAmount(paidAmount);
//			pmDAO.save(pmENT);
//			
//			TransactionsMasterEntity tmENT = new TransactionsMasterEntity();
//			tmENT.setPaidAmount(paidAmount);
//			tmENT.setInvoiceNo(pmENT.getInvoiceNo());
//			tmENT.setOrderId(pmENT.getOrderId());
//			tmENT.setDeliveryId(pmENT.getDeliveryId());
//			tmENT.setGrossAmount(pmENT.getGrossAmount());
//			tmENT.setPayableAmount(pmENT.getPayableAmount());
//			tmENT.setDeliveredDate(pmENT.getDeliveredDate());
//			LocalDate currentDate = LocalDate.now();
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//			String formattedDate = currentDate.format(formatter);
//			tmENT.setPaymentDate(formattedDate);
//			tmENT.setPaymentMode("return credit");
//			tmENT.setPaymentStatus("cleared");
//			tmENT.setCustomerName(pmENT.getCustomerName());
//			tmENT.setExactPaidAmount(returnCredit);
//			tmENT.setCustomerId(pmENT.getCustomerId());
//			tmENT.setPaidBy("Credit Used");
//			tmDAO.save(tmENT);
//
//			if(rmDTO.getCustomerId().startsWith("CUST")) {
//
//				CustomerMasterEntity cmENT = cmDAO.getAllCustomerById(rmDTO.getCustomerId());
//				cmENT.setReturnCredit(0);
//				cmDAO.save(cmENT);
//				}
//				else {
//				UserMasterEntity umENT = umDAO.getUserById(rmDTO.getCustomerId());
//				umENT.setReturnCredit(0);
//				umDAO.save(umENT);
//				}
//
//			OrderMasterEntity omENT = omDAO.getOrderById(pmENT.getOrderId());
//			LocalDate current_date = LocalDate.now();
//			DateTimeFormatter formatteR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//			String formatted_date = current_date.format(formatteR);
//			omENT.setPaymentDate(formatted_date);
//			omENT.setPaymentStatus(pmENT.getPaymentStatus());
//			omENT.setPaymentMode("credit used");
//			omDAO.save(omENT);
//			
//			InvoiceMasterEntity imENT = imDAO.getInvoiceByOrderId(pmENT.getOrderId());
//			LocalDate current_date1 = LocalDate.now();
//			DateTimeFormatter formatteR1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//			String formatted_date1 = current_date1.format(formatteR1);
//			imENT.setPaymentDate(formatted_date1);
//			imENT.setPaymentStatus(pmENT.getPaymentStatus());
//			imENT.setPaymentMode("credit used");
//			imDAO.save(imENT);
//
//			DashBoardEntity dbENT = dbDAO.getDashBoardDetailsByOrderId(pmENT.getOrderId());
//			dbENT.setPaymentStatus(pmENT.getPaymentStatus());
//			dbDAO.save(dbENT);
//			
//			PaymentMasterEntity pmEnt = pmDAO.getPaymentListByInvoiceNo(rmDTO.getInvoiceNumber());
//			pmEnt.setPaymentMode("credit used");
//			
//			
//
//		} else if (existPayableAmount < returnCredit) {
//			double remainingCredit = returnCredit - existPayableAmount;
//
//			pmENT.setPayableAmount(0);
//			pmENT.setReturnCredit(remainingCredit);
//			pmENT.setReturnCreditUsed(existPayableAmount);
//			pmENT.setPaymentStatus("cleared");
//			double paidAmount = existPaidAmount + existPayableAmount;
//			//double paidAmount = returnCredit - existPaidAmount;
//
//			pmENT.setPaidAmount(paidAmount);
//			pmDAO.save(pmENT);
//			
//			TransactionsMasterEntity tmENT = new TransactionsMasterEntity();
//			tmENT.setPaidAmount(paidAmount);
//			tmENT.setInvoiceNo(pmENT.getInvoiceNo());
//			tmENT.setOrderId(pmENT.getOrderId());
//			tmENT.setDeliveryId(pmENT.getDeliveryId());
//			tmENT.setGrossAmount(pmENT.getGrossAmount());
//			tmENT.setPayableAmount(pmENT.getPayableAmount());
//			tmENT.setDeliveredDate(pmENT.getDeliveredDate());
//			LocalDate currentDate = LocalDate.now();
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//			String formattedDate = currentDate.format(formatter);
//			tmENT.setPaymentDate(formattedDate);
//			tmENT.setPaymentMode("return credit");
//			tmENT.setPaymentStatus("cleared");
//			tmENT.setCustomerName(pmENT.getCustomerName());
//			tmENT.setExactPaidAmount(pmENT.getReturnCreditUsed());
//			tmENT.setCustomerId(pmENT.getCustomerId());
//			tmENT.setPaidBy("Credit Used");
//			tmDAO.save(tmENT);
//			
//			if(rmDTO.getCustomerId().startsWith("CUST")) {
//
//				CustomerMasterEntity cmENT = cmDAO.getAllCustomerById(rmDTO.getCustomerId());
//				cmENT.setReturnCredit(remainingCredit);
//				cmDAO.save(cmENT);
//				}
//				else {
//				UserMasterEntity umENT = umDAO.getUserById(rmDTO.getCustomerId());
//				umENT.setReturnCredit(remainingCredit);
//				umDAO.save(umENT);
//				}
//
//			OrderMasterEntity omENT = omDAO.getOrderById(pmENT.getOrderId());
//			LocalDate current_date = LocalDate.now();
//			DateTimeFormatter formatteR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//			String formatted_date = current_date.format(formatteR);
//			omENT.setPaymentDate(formatted_date);
//			omENT.setPaymentStatus(pmENT.getPaymentStatus());
//			omENT.setPaymentMode("credit used");
//			omDAO.save(omENT);
//			
//			InvoiceMasterEntity imENT = imDAO.getInvoiceByOrderId(pmENT.getOrderId());
//			LocalDate current_date1 = LocalDate.now();
//			DateTimeFormatter formatteR1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//			String formatted_date1 = current_date1.format(formatteR1);
//			imENT.setPaymentDate(formatted_date1);
//			imENT.setPaymentStatus(pmENT.getPaymentStatus());
//			imENT.setPaymentMode("credit used");
//			imDAO.save(imENT);
//
//			DashBoardEntity dbENT = dbDAO.getDashBoardDetailsByOrderId(pmENT.getOrderId());
//			dbENT.setPaymentStatus(pmENT.getPaymentStatus());
//			dbDAO.save(dbENT);
//			
//			PaymentMasterEntity pmEnt = pmDAO.getPaymentListByInvoiceNo(rmDTO.getInvoiceNumber());
//			pmEnt.setPaymentMode("credit used");
//		}
//
//		else if (existPayableAmount > returnCredit && existPayableAmount != 0) {
//
//			double amount = existPayableAmount - returnCredit;
//			pmENT.setPayableAmount(amount);
//			pmENT.setReturnCredit(0);
//			pmENT.setReturnCreditUsed(returnCredit);
//			pmENT.setPaymentStatus("partial payment");
//			double paidAmount = existPaidAmount + returnCredit;
//			pmENT.setPaidAmount(paidAmount);
//			pmDAO.save(pmENT);
//			
//			TransactionsMasterEntity tmENT = new TransactionsMasterEntity();
//			tmENT.setPaidAmount(paidAmount);
//			tmENT.setInvoiceNo(pmENT.getInvoiceNo());
//			tmENT.setOrderId(pmENT.getOrderId());
//			tmENT.setDeliveryId(pmENT.getDeliveryId());
//			tmENT.setGrossAmount(pmENT.getGrossAmount());
//			tmENT.setPayableAmount(pmENT.getPayableAmount());
//			tmENT.setDeliveredDate(pmENT.getDeliveredDate());
//			LocalDate currentDate = LocalDate.now();
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//			String formattedDate = currentDate.format(formatter);
//			tmENT.setPaymentDate(formattedDate);
//			tmENT.setPaymentMode("return credit");
//			tmENT.setPaymentStatus("partial payment");
//			tmENT.setCustomerName(pmENT.getCustomerName());
//			tmENT.setExactPaidAmount(returnCredit);
//			tmENT.setCustomerId(pmENT.getCustomerId());
//			tmENT.setPaidBy("Credit Used");
//			tmDAO.save(tmENT);
//
//			if(rmDTO.getCustomerId().startsWith("CUST")) {
//
//				CustomerMasterEntity cmENT = cmDAO.getAllCustomerById(rmDTO.getCustomerId());
//				cmENT.setReturnCredit(0);
//				cmDAO.save(cmENT);
//				}
//				else {
//				UserMasterEntity umENT = umDAO.getUserById(rmDTO.getCustomerId());
//				umENT.setReturnCredit(0);
//				umDAO.save(umENT);
//				}
//
//			OrderMasterEntity omENT = omDAO.getOrderById(pmENT.getOrderId());
//			LocalDate current_date = LocalDate.now();
//			DateTimeFormatter formatteR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//			String formatted_date = current_date.format(formatteR);
//			omENT.setPaymentDate(formatted_date);
//			omENT.setPaymentStatus(pmENT.getPaymentStatus());
//			omENT.setPaymentMode("credit used");
//			omDAO.save(omENT);
//			
//			InvoiceMasterEntity imENT = imDAO.getInvoiceByOrderId(pmENT.getOrderId());
//			LocalDate current_date1 = LocalDate.now();
//			DateTimeFormatter formatteR1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//			String formatted_date1 = current_date1.format(formatteR1);
//			imENT.setPaymentDate(formatted_date1);
//			imENT.setPaymentStatus(pmENT.getPaymentStatus());
//			imENT.setPaymentMode("credit used");
//			imDAO.save(imENT);
//
//			DashBoardEntity dbENT = dbDAO.getDashBoardDetailsByOrderId(pmENT.getOrderId());
//			dbENT.setPaymentStatus(pmENT.getPaymentStatus());
//			dbDAO.save(dbENT);
//			
//			PaymentMasterEntity pmEnt = pmDAO.getPaymentListByInvoiceNo(rmDTO.getInvoiceNumber());
//			pmEnt.setPaymentMode("credit used");
//		}
//
//		else if (existPayableAmount == returnCredit && existPayableAmount != 0) {
//
//			pmENT.setPayableAmount(0);
//			pmENT.setReturnCredit(0);
//			pmENT.setReturnCreditUsed(returnCredit);
//			pmENT.setPaymentStatus("cleared");
//			double paidAmount = existPaidAmount + returnCredit;
//			pmENT.setPaidAmount(paidAmount);
//			pmDAO.save(pmENT);
//			
//			TransactionsMasterEntity tmENT = new TransactionsMasterEntity();
//			tmENT.setPaidAmount(paidAmount);
//			tmENT.setInvoiceNo(pmENT.getInvoiceNo());
//			tmENT.setOrderId(pmENT.getOrderId());
//			tmENT.setDeliveryId(pmENT.getDeliveryId());
//			tmENT.setGrossAmount(pmENT.getGrossAmount());
//			tmENT.setPayableAmount(pmENT.getPayableAmount());
//			tmENT.setDeliveredDate(pmENT.getDeliveredDate());
//			LocalDate currentDate = LocalDate.now();
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//			String formattedDate = currentDate.format(formatter);
//			tmENT.setPaymentDate(formattedDate);
//			tmENT.setPaymentMode("return credit");
//			tmENT.setPaymentStatus("cleared");
//			tmENT.setCustomerName(pmENT.getCustomerName());
//			tmENT.setExactPaidAmount(returnCredit);
//			tmENT.setCustomerId(pmENT.getCustomerId());
//			tmENT.setPaidBy("Credit Used");
//			tmDAO.save(tmENT);
//
//			if(rmDTO.getCustomerId().startsWith("CUST")) {
//
//				CustomerMasterEntity cmENT = cmDAO.getAllCustomerById(rmDTO.getCustomerId());
//				cmENT.setReturnCredit(0);
//				cmDAO.save(cmENT);
//				}
//				else {
//				UserMasterEntity umENT = umDAO.getUserById(rmDTO.getCustomerId());
//				umENT.setReturnCredit(0);
//				umDAO.save(umENT);
//				}
//
//			OrderMasterEntity omENT = omDAO.getOrderById(pmENT.getOrderId());
//			LocalDate current_date = LocalDate.now();
//			DateTimeFormatter formatteR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//			String formatted_date = current_date.format(formatteR);
//			omENT.setPaymentDate(formatted_date);
//			omENT.setPaymentStatus(pmENT.getPaymentStatus());
//			omENT.setPaymentMode("credit used");
//			omDAO.save(omENT);
//			
//			InvoiceMasterEntity imENT = imDAO.getInvoiceByOrderId(pmENT.getOrderId());
//			LocalDate current_date1 = LocalDate.now();
//			DateTimeFormatter formatteR1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//			String formatted_date1 = current_date1.format(formatteR1);
//			imENT.setPaymentDate(formatted_date1);
//			imENT.setPaymentStatus(pmENT.getPaymentStatus());
//			imENT.setPaymentMode("credit used");
//			imDAO.save(imENT);
//
//			DashBoardEntity dbENT = dbDAO.getDashBoardDetailsByOrderId(pmENT.getOrderId());
//			dbENT.setPaymentStatus(pmENT.getPaymentStatus());
//			dbDAO.save(dbENT);
//			
//			PaymentMasterEntity pmEnt = pmDAO.getPaymentListByInvoiceNo(rmDTO.getInvoiceNumber());
//			pmEnt.setPaymentMode("credit used");
//
//		}
//
//		Map<String, Object> response = new LinkedHashMap<>();
//		response.put("status", "success");
//		response.put("id", rmENT.getReturnId());
//		return response;
//	}
//
//	public List<ReturnMasterEntity> getAllReturnMaster() {
//		List<ReturnMasterEntity> returnList = rmDAO.findAll();
//		return returnList;
//	}
//
//}
