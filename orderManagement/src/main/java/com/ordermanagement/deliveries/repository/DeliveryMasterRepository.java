//package com.ordermanagement.deliveries.repository;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import com.ordermanagement.customers.dao.CustomerMasterDAO;
//import com.ordermanagement.customers.entity.CustomerMasterEntity;
//import com.ordermanagement.dashboard.dao.DashBoardDAO;
//import com.ordermanagement.dashboard.entity.DashBoardEntity;
//import com.ordermanagement.deliveries.dao.DeliveryMasterDAO;
//import com.ordermanagement.deliveries.dao.DeliveryMasterItemDAO;
//import com.ordermanagement.deliveries.dto.DeliveryMasterDTO;
//import com.ordermanagement.deliveries.dto.DeliveryMasterItemDTO;
//import com.ordermanagement.deliveries.entity.DeliveryMasterEntity;
//import com.ordermanagement.deliveries.entity.DeliveryMasterItemEntity;
//import com.ordermanagement.orders.dao.OrderMasterDAO;
//import com.ordermanagement.orders.entity.OrderMasterEntity;
//import com.ordermanagement.payments.dao.PaymentMasterDAO;
//import com.ordermanagement.payments.dao.TransactionsMasterDAO;
//import com.ordermanagement.payments.entity.PaymentMasterEntity;
//import com.ordermanagement.payments.entity.TransactionsMasterEntity;
//import com.ordermanagement.user.dao.UserMasterDAO;
//import com.ordermanagement.user.entity.UserMasterEntity;
//
//@Component
//public class DeliveryMasterRepository {
//
//	@Autowired
//	private DeliveryMasterDAO dmDAO;
//
//	@Autowired
//	private DeliveryMasterItemDAO dmiDAO;
//	
//
//	@Autowired
//	private OrderMasterDAO omDAO;
//
//	@Autowired
//	private DashBoardDAO dbDAO;
//
//	@Autowired
//	private PaymentMasterDAO pmDAO;
//
//	@Autowired
//	private TransactionsMasterDAO tmDAO;
//
//	@Autowired
//	private CustomerMasterDAO cmDAO;
//	
//	@Autowired
//	private UserMasterDAO umDAO;
//	
//	public Map<String, Object> addDeliveryMaster(DeliveryMasterDTO dmDTO) {
//	    DeliveryMasterEntity dmENT = new DeliveryMasterEntity();
//	    dmENT.setDeliveryId(dmDTO.getDeliveryId());
//	    dmENT.setOrderId(dmDTO.getOrderId());
//	    dmENT.setInvoiceNo(dmDTO.getInvoiceNo());
//	   // dmENT.setModifiedAt(dmDTO.getModifiedAt());
//	    dmENT.setCreatedDate(dmDTO.getCreatedDate());
//	    dmENT.setPickedDate("-");
//	    dmENT.setDeliveredDate("-");
//	    dmENT.setDeliveryLocation(dmDTO.getDeliveryLocation());
//	    dmENT.setStatus("Created");
//	    dmENT.setDeliveryAddress(dmDTO.getDeliveryAddress());
//	    dmENT.setContactPerson(dmDTO.getContactPerson());
//	    dmENT.setContactNumber(dmDTO.getContactNumber());
//	    dmENT.setComments(dmDTO.getComments());
//	    dmENT.setCustomerId(dmDTO.getCustomerId());
//	    dmENT.setTotal(dmDTO.getTotal());
//	    dmDAO.save(dmENT);
//
//	    for (DeliveryMasterItemDTO itemDto : dmDTO.getItems()) {
//	        DeliveryMasterItemEntity item = new DeliveryMasterItemEntity();
//	        item.setDeliveryMasterItemId(itemDto.getDeliveryMasterItemId());
//	        item.setProductName(itemDto.getProductName());
//	        item.setCategory(itemDto.getCategory());
//	        item.setSubCategory(itemDto.getSubCategory());
//	        item.setPrice(itemDto.getPrice());
//	        item.setQty(itemDto.getQty());
//	        item.setTotalAmount(itemDto.getTotalAmount());
//	        item.setDeliveryId(dmENT.getDeliveryId());
//	        item.setTax(itemDto.getTax());
//	        item.setDiscount(itemDto.getDiscount());
//	        item.setActualAmount(itemDto.getActualAmount());
//	        item.setOrderMasterItemId(itemDto.getOrderMasterItemId());
//	        dmiDAO.save(item);
//
//	        OrderMasterEntity omENT = omDAO.getOrderById(dmENT.getOrderId());
//	        if (omENT != null) {
//	            omENT.setStatus("Created");
//	            omENT.setDeliveryLocation(dmDTO.getDeliveryLocation());
//	            omENT.setDeliveryAddress(dmDTO.getDeliveryAddress());
//	            omENT.setContactPerson(dmDTO.getContactPerson());
//	            omENT.setContactNumber(dmDTO.getContactNumber());
//	            omENT.setComments(dmDTO.getComments());
//	            omENT.setDeliveryId(dmENT.getDeliveryId());
//	            omENT.setDeliveredDate(dmDTO.getCreatedDate());
//	            omDAO.save(omENT);
//	        }
//	    }
//
//	    DashBoardEntity dbENT = dbDAO.getDashBoardDetailsByOrderId(dmENT.getOrderId());
//	    if (dbENT != null) {
//	        dbENT.setDeliveredDate("-");
//	        dbENT.setStatus("Created");
//	        dbDAO.save(dbENT);
//	    }
//
//	    Map<String, Object> s4HanaResponse = new LinkedHashMap<>();
//	    try {
//	        // Prepare S/4HANA payload
//	    	Map<String, Object> s4Payload = new HashMap<>();
//	    	Map<String, Object> toDeliveryDocumentItem = new HashMap<>();
//
//	    	// Create the nested "results" list
//	    	List<Map<String, Object>> resultsList = new ArrayList<>();
//	    	Map<String, Object> resultEntry = new HashMap<>();
//	    	resultEntry.put("ReferenceSDDocument", "373"); // Add ReferenceSDDocument
//	    	resultsList.add(resultEntry);
//
//	    	// Add the "results" list to the "to_DeliveryDocumentItem" map
//	    	toDeliveryDocumentItem.put("results", resultsList);
//	    	s4Payload.put("to_DeliveryDocumentItem", toDeliveryDocumentItem);
//
//
//	        // Fetch x-csrf-token and send data to S/4HANA
//	        String s4hanaApiUrl = "https://my403545-api.s4hana.cloud.sap/sap/opu/odata/sap/API_OUTBOUND_DELIVERY_ SRV;v=0002/A_OutbDeliveryHeader";
//	        String username = "INTEGRATION"; // Replace with actual username
//	        String password = "gNQH>ydgzvCKfwuxQdcEDQzHMz5YPQfaLxzPHqLz"; // Replace with actual password
//
//	        RestTemplate restTemplate = new RestTemplate();
//
//	        // Step 1: Fetch CSRF token and cookies
//	        HttpHeaders getHeaders = new HttpHeaders();
//	        getHeaders.setBasicAuth(username, password);
//	        getHeaders.set("x-csrf-token", "fetch");
//
//	        HttpEntity<Void> csrfRequest = new HttpEntity<>(getHeaders);
//	        ResponseEntity<String> csrfResponse = restTemplate.exchange(
//	                s4hanaApiUrl, HttpMethod.GET, csrfRequest, String.class);
//
//	        if (csrfResponse.getStatusCode() == HttpStatus.OK) {
//	            String csrfToken = csrfResponse.getHeaders().getFirst("x-csrf-token");
//	            List<String> cookies = csrfResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
//
//	            // Step 2: Use CSRF token and cookies for POST request
//	            HttpHeaders postHeaders = new HttpHeaders();
//	            postHeaders.setBasicAuth(username, password);
//	            postHeaders.set("x-csrf-token", csrfToken);
//	            postHeaders.setContentType(MediaType.APPLICATION_JSON);
//	            postHeaders.put(HttpHeaders.COOKIE, cookies);
//
//	            HttpEntity<Map<String, Object>> postRequest = new HttpEntity<>(s4Payload, postHeaders);
//	            ResponseEntity<String> postResponse = restTemplate.exchange(
//	                    s4hanaApiUrl, HttpMethod.POST, postRequest, String.class);
//
//	            if (postResponse.getStatusCode() == HttpStatus.CREATED) {
//	                s4HanaResponse.put("status", "success");
//	                s4HanaResponse.put("message", "Delivery added to S/4HANA successfully");
//	            } else {
//	                s4HanaResponse.put("status", "failure");
//	                s4HanaResponse.put("message", "Failed to add delivery to S/4HANA");
//	                s4HanaResponse.put("response", postResponse.getBody());
//	            }
//	        } else {
//	            s4HanaResponse.put("status", "error");
//	            s4HanaResponse.put("message", "Failed to fetch x-csrf-token");
//	        }
//	    } catch (Exception e) {
//	        s4HanaResponse.put("status", "error");
//	        s4HanaResponse.put("message", "Error while communicating with S/4HANA: " + e.getMessage());
//	    }
//
//	    Map<String, Object> response = new LinkedHashMap<>();
//	    response.put("status", "success");
//	    response.put("id", dmENT.getDeliveryId());
//	    response.put("s4HanaResponse", s4HanaResponse);
//	    return response;
//	}
//
//
////	public Map<String, Object> addDeliveryMaster(DeliveryMasterDTO dmDTO) {
////		DeliveryMasterEntity dmENT = new DeliveryMasterEntity();
////		dmENT.setDeliveryId(dmDTO.getDeliveryId());
////		dmENT.setOrderId(dmDTO.getOrderId());
////		dmENT.setInvoiceNo(dmDTO.getInvoiceNo());
////		dmENT.setModifiedAt(dmDTO.getModifiedAt());
////		dmENT.setDeliveryLocation(dmDTO.getDeliveryLocation());
////		dmENT.setStatus("In Progress");
////		dmENT.setDeliveryAddress(dmDTO.getDeliveryAddress());
////		dmENT.setContactPerson(dmDTO.getContactPerson());
////		dmENT.setContactNumber(dmDTO.getContactNumber());
////		dmENT.setComments(dmDTO.getComments());
////		dmENT.setCustomerId(dmDTO.getCustomerId());
////		dmENT.setTotal(dmDTO.getTotal());
////		dmDAO.save(dmENT);
////
////		for (DeliveryMasterItemDTO itemDto : dmDTO.getItems()) {
////			DeliveryMasterItemEntity item = new DeliveryMasterItemEntity();
////			item.setDeliveryMasterItemId(itemDto.getDeliveryMasterItemId());
////			item.setProductName(itemDto.getProductName());
////			item.setCategory(itemDto.getCategory());
////			item.setSubCategory(itemDto.getSubCategory());
////			item.setPrice(itemDto.getPrice());
////			item.setQty(itemDto.getQty());
////			item.setTotalAmount(itemDto.getTotalAmount());
////			item.setDeliveryId(dmENT.getDeliveryId());
////			item.setTax(itemDto.getTax());
////			item.setDiscount(itemDto.getDiscount());
////			item.setActualAmount(itemDto.getActualAmount());
////			item.setOrderMasterItemId(itemDto.getOrderMasterItemId());
////			dmiDAO.save(item);
////
////			OrderMasterEntity omENT = omDAO.getOrderById(dmENT.getOrderId());
////			if (omENT != null) {
////				omENT.setStatus("In Progress");
////				omENT.setDeliveryLocation(dmDTO.getDeliveryLocation());
////				omENT.setDeliveryAddress(dmDTO.getDeliveryAddress());
////				omENT.setContactPerson(dmDTO.getContactPerson());
////				omENT.setContactNumber(dmDTO.getContactNumber());
////				omENT.setComments(dmDTO.getComments());
////				omENT.setDeliveryId(dmENT.getDeliveryId());
////				omENT.setDeliveredDate(dmDTO.getModifiedAt());
////				omDAO.save(omENT);
////			}
////		}
////
////		DashBoardEntity dbENT = dbDAO.getDashBoardDetailsByOrderId(dmENT.getOrderId());
////		if (dbENT != null) {
////			dbENT.setDeliveredDate("-");
////			dbENT.setStatus("In Progress");
////			dbDAO.save(dbENT);
////		}
////
////		Map<String, Object> response = new LinkedHashMap<>();
////		response.put("status", "success");
////		response.put("id", dmENT.getDeliveryId());
////		return response;
////	}
//
////	public Map<String, Object> updateDeliveryMaster(DeliveryMasterDTO dmDTO) {
////		DeliveryMasterEntity dmENT = new DeliveryMasterEntity();
////		dmENT.setDeliveryId(dmDTO.getDeliveryId());
////		dmENT.setOrderId(dmDTO.getOrderId());
////		dmENT.setInvoiceNo(dmDTO.getInvoiceNo());
////		//dmENT.setModifiedAt(dmDTO.getModifiedAt());
////		DeliveryMasterEntity dmEnt = dmDAO.findByDeliveryId(dmDTO.getDeliveryId());
////		String createdDate = dmEnt.getCreatedDate();
////		dmENT.setCreatedDate(createdDate);
////		dmENT.setPickedDate(dmDTO.getPickedDate());
////		dmENT.setDeliveredDate("-");
////		dmENT.setDeliveryLocation(dmDTO.getDeliveryLocation());
////		dmENT.setStatus("Picked");
////		dmENT.setDeliveryAddress(dmDTO.getDeliveryAddress());
////		dmENT.setContactPerson(dmDTO.getContactPerson());
////		dmENT.setContactNumber(dmDTO.getContactNumber());
////		dmENT.setComments(dmDTO.getComments());
////		dmENT.setTotal(dmDTO.getTotal());
////		dmENT.setCustomerId(dmDTO.getCustomerId());
////		dmDAO.save(dmENT);
////
////		for (DeliveryMasterItemDTO itemDto : dmDTO.getItems()) {
////			DeliveryMasterItemEntity item = new DeliveryMasterItemEntity();
////			item.setDeliveryMasterItemId(itemDto.getDeliveryMasterItemId());
////			item.setProductName(itemDto.getProductName());
////			item.setCategory(itemDto.getCategory());
////			item.setSubCategory(itemDto.getSubCategory());
////			item.setPrice(itemDto.getPrice());
////			item.setQty(itemDto.getQty());
////			item.setTotalAmount(itemDto.getTotalAmount());
////			item.setDeliveryId(dmENT.getDeliveryId());
////			item.setTax(itemDto.getTax());
////			item.setDiscount(itemDto.getDiscount());
////			item.setActualAmount(itemDto.getActualAmount());
////			item.setOrderMasterItemId(itemDto.getOrderMasterItemId());
////			dmiDAO.save(item);
////		}
////		// dmDAO.save(dmENT);
////		OrderMasterEntity omENT = omDAO.getOrderById(dmENT.getOrderId());
////		if (omENT != null) {
////			omENT.setStatus("Picked");
////			omENT.setDeliveryLocation(dmDTO.getDeliveryLocation());
////			omENT.setDeliveryAddress(dmDTO.getDeliveryAddress());
////			omENT.setContactPerson(dmDTO.getContactPerson());
////			omENT.setContactNumber(dmDTO.getContactNumber());
////			omENT.setComments(dmDTO.getComments());
////			omENT.setDeliveryId(dmENT.getDeliveryId());
////			omENT.setDeliveredDate(dmDTO.getPickedDate());
////			omDAO.save(omENT);
////		}
////
////		DashBoardEntity dbENT = dbDAO.getDashBoardDetailsByOrderId(dmENT.getOrderId());
////		if (dbENT != null) {
////			dbENT.setDeliveredDate(dmENT.getPickedDate());
////			dbENT.setStatus("Picked");
////			dbENT.setPaymentStatus("open");
////			dbDAO.save(dbENT);
////		}
////
////		PaymentMasterEntity pmENT = new PaymentMasterEntity();
////		pmENT.setCustomerName(dmDTO.getContactPerson());
////		pmENT.setDeliveredDate(dmDTO.getPickedDate());
////		pmENT.setDeliveryId(dmENT.getDeliveryId());
////		pmENT.setOrderId(dmDTO.getOrderId());
////		pmENT.setPaidAmount(0);
////		pmENT.setGrossAmount(dmDTO.getTotal());
////		pmENT.setPaymentDate("-");
////		pmENT.setPaymentMode("-");
////		pmENT.setPaymentStatus("open");
////		pmENT.setPayableAmount(0);
////		pmENT.setInvoiceNo(omENT.getInvoiceNo());
////		pmENT.setCustomerId(dmDTO.getCustomerId());
////		pmENT.setReturnCreditUsed(0);
////		if(dmDTO.getCustomerId().startsWith("CUST")) {
////		CustomerMasterEntity cmENT = cmDAO.getAllCustomerById(dmDTO.getCustomerId());
////		pmENT.setReturnCredit(cmENT.getReturnCredit());
////		}
////		else {
////			String userId = dmDTO.getCustomerId();
////			UserMasterEntity umENT = umDAO.getUserById(userId);
////			pmENT.setReturnCredit(umENT.getReturnCredit());
////		}
////		pmDAO.save(pmENT);
////		
////		OrderMasterEntity omEnt = omDAO.getOrderById(dmENT.getOrderId());
////		if (omEnt != null) {
////			omENT.setPaymentId(pmENT.getPaymentId());
////			omDAO.save(omENT);
////		}
////		
////		TransactionsMasterEntity tmENT = new TransactionsMasterEntity();
////		tmENT.setCustomerName(dmDTO.getContactPerson());
////		tmENT.setDeliveredDate(dmDTO.getPickedDate());
////		tmENT.setDeliveryId(dmENT.getDeliveryId());
////		tmENT.setOrderId(dmDTO.getOrderId());
////		tmENT.setPaidAmount(0);
////		tmENT.setGrossAmount(dmDTO.getTotal());
////		tmENT.setPaymentDate("-");
////		tmENT.setPaymentMode("-");
////		tmENT.setPaymentStatus("open");
////		tmENT.setPayableAmount(0);
////		tmENT.setInvoiceNo(omENT.getInvoiceNo());
////		tmENT.setCustomerId(omENT.getCustomerId());
////		tmENT.setPaidBy("-");
////		tmDAO.save(tmENT);
////
////		if(pmENT.getReturnCredit() != 0) {
////			TransactionsMasterEntity tmEnt = new TransactionsMasterEntity();
////			tmEnt.setCustomerName(dmDTO.getContactPerson());
////			tmEnt.setDeliveredDate(dmDTO.getPickedDate());
////			tmEnt.setDeliveryId(dmENT.getDeliveryId());
////			tmEnt.setOrderId(dmDTO.getOrderId());
////			tmEnt.setPaidAmount(pmENT.getReturnCredit());
////			tmEnt.setGrossAmount(dmDTO.getTotal());
////			LocalDate currentDate = LocalDate.now();
////			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
////			String formattedDate = currentDate.format(formatter);
////			tmEnt.setPaymentDate(formattedDate);
////			tmEnt.setPaymentMode("return credit");
////			tmEnt.setPaymentStatus("partial payment");
////			double payableAmount = dmDTO.getTotal() - pmENT.getReturnCredit();
////			tmEnt.setPayableAmount(payableAmount);
////			tmEnt.setInvoiceNo(omENT.getInvoiceNo());
////			tmEnt.setCustomerId(omENT.getCustomerId());
////			tmEnt.setPaidBy("-");
////			tmEnt.setExactPaidAmount(pmENT.getReturnCredit());
////			tmEnt.setPaidBy("Credit Used");
////			tmDAO.save(tmEnt);
////			
////			System.out.println(tmEnt.getTransactionsId());
////
////			
////			PaymentMasterEntity pmEnt = pmDAO.getPaymentListByInvoiceNo(dmDTO.getInvoiceNo());
////			pmEnt.setPayableAmount(tmEnt.getPayableAmount());
////			pmENT.setPaidAmount(tmEnt.getPaidAmount());
////			pmENT.setPaymentDate(tmEnt.getPaymentDate());
////			pmENT.setPaymentMode(tmEnt.getPaymentMode());
////			pmENT.setPaymentStatus(tmEnt.getPaymentStatus());
////			pmENT.setReturnCredit(0);
////			pmENT.setReturnCreditUsed(tmEnt.getPaidAmount());
////			pmDAO.save(pmENT);
////
////			if (tmENT.getCustomerId() != null && tmENT.getCustomerId().startsWith("CUST")) {
////			CustomerMasterEntity cmENT = cmDAO.getAllCustomerById(tmEnt.getCustomerId());
////			cmENT.setReturnCredit(0);
////			cmDAO.save(cmENT);
////			} else {
////				UserMasterEntity umENT = umDAO.getAllUserById(tmENT.getCustomerId());
////				umENT.setReturnCredit(0);
////				umDAO.save(umENT);
////			}
////
////		}
////			
////		Map<String, Object> response = new LinkedHashMap<>();
////		response.put("status", "success");
////		response.put("id", dmENT.getDeliveryId());
////		response.put("check", tmENT.getTransactionsId());
////		return response;
////
////	}
//	
//	public Map<String, Object> updateDeliveryMaster(DeliveryMasterDTO dmDTO) {
//		DeliveryMasterEntity dmENT = new DeliveryMasterEntity();
//		dmENT.setDeliveryId(dmDTO.getDeliveryId());
//		dmENT.setOrderId(dmDTO.getOrderId());
//		dmENT.setInvoiceNo(dmDTO.getInvoiceNo());
//		DeliveryMasterEntity dmEnt = dmDAO.findByDeliveryId(dmDTO.getDeliveryId());
//		String createdDate = dmEnt.getCreatedDate();
//		dmENT.setCreatedDate(createdDate);
//		dmENT.setPickedDate(dmDTO.getPickedDate());
//		dmENT.setDeliveredDate("-");
//		dmENT.setDeliveryLocation(dmDTO.getDeliveryLocation());
//		dmENT.setStatus("Picked");
//		dmENT.setDeliveryAddress(dmDTO.getDeliveryAddress());
//		dmENT.setContactPerson(dmDTO.getContactPerson());
//		dmENT.setContactNumber(dmDTO.getContactNumber());
//		dmENT.setComments(dmDTO.getComments());
//		dmENT.setTotal(dmDTO.getTotal());
//		dmENT.setCustomerId(dmDTO.getCustomerId());
//		dmDAO.save(dmENT);
//
//		for (DeliveryMasterItemDTO itemDto : dmDTO.getItems()) {
//			DeliveryMasterItemEntity item = new DeliveryMasterItemEntity();
//			item.setDeliveryMasterItemId(itemDto.getDeliveryMasterItemId());
//			item.setProductName(itemDto.getProductName());
//			item.setCategory(itemDto.getCategory());
//			item.setSubCategory(itemDto.getSubCategory());
//			item.setPrice(itemDto.getPrice());
//			item.setQty(itemDto.getQty());
//			item.setTotalAmount(itemDto.getTotalAmount());
//			item.setDeliveryId(dmENT.getDeliveryId());
//			item.setTax(itemDto.getTax());
//			item.setDiscount(itemDto.getDiscount());
//			item.setActualAmount(itemDto.getActualAmount());
//			item.setOrderMasterItemId(itemDto.getOrderMasterItemId());
//			dmiDAO.save(item);
//		}
//
//		OrderMasterEntity omENT = omDAO.getOrderById(dmENT.getOrderId());
//		if (omENT != null) {
//			omENT.setStatus("Picked");
//			omENT.setDeliveryLocation(dmDTO.getDeliveryLocation());
//			omENT.setDeliveryAddress(dmDTO.getDeliveryAddress());
//			omENT.setContactPerson(dmDTO.getContactPerson());
//			omENT.setContactNumber(dmDTO.getContactNumber());
//			omENT.setComments(dmDTO.getComments());
//			omENT.setDeliveryId(dmENT.getDeliveryId());
//			omENT.setDeliveredDate(dmDTO.getPickedDate());
//			omDAO.save(omENT);
//		}
//
//		DashBoardEntity dbENT = dbDAO.getDashBoardDetailsByOrderId(dmENT.getOrderId());
//		if (dbENT != null) {
//			dbENT.setDeliveredDate(dmENT.getPickedDate());
//			dbENT.setStatus("Picked");
//			dbENT.setPaymentStatus("open");
//			dbDAO.save(dbENT);
//		}
//
//		Map<String, Object> response = new LinkedHashMap<>();
//		response.put("status", "success");
//		response.put("id", dmENT.getDeliveryId());
//		return response;
//	}
//
//	
//	public Map<String, Object> updateDelivered(DeliveryMasterDTO dmDTO) {
//		DeliveryMasterEntity dmENT = new DeliveryMasterEntity();
//		dmENT.setDeliveryId(dmDTO.getDeliveryId());
//		dmENT.setOrderId(dmDTO.getOrderId());
//		dmENT.setInvoiceNo(dmDTO.getInvoiceNo());
//		//dmENT.setModifiedAt(dmDTO.getModifiedAt());
//		//dmENT.setCreatedDate("-");
//		//dmENT.setPickedDate(dmDTO.getPickedDate());
//		DeliveryMasterEntity dmEnt = dmDAO.findByDeliveryId(dmDTO.getDeliveryId());
//		String createdDate = dmEnt.getCreatedDate();
//		String pickedDate = dmEnt.getPickedDate();
//		dmENT.setCreatedDate(createdDate);
//		dmENT.setPickedDate(pickedDate);
//		dmENT.setDeliveredDate(dmDTO.getDeliveredDate());
//		dmENT.setDeliveryLocation(dmDTO.getDeliveryLocation());
//		dmENT.setStatus("Delivered");
//		dmENT.setDeliveryAddress(dmDTO.getDeliveryAddress());
//		dmENT.setContactPerson(dmDTO.getContactPerson());
//		dmENT.setContactNumber(dmDTO.getContactNumber());
//		dmENT.setComments(dmDTO.getComments());
//		dmENT.setTotal(dmDTO.getTotal());
//		dmENT.setCustomerId(dmDTO.getCustomerId());
//		dmDAO.save(dmENT);
//
//		for (DeliveryMasterItemDTO itemDto : dmDTO.getItems()) {
//			DeliveryMasterItemEntity item = new DeliveryMasterItemEntity();
//			item.setDeliveryMasterItemId(itemDto.getDeliveryMasterItemId());
//			item.setProductName(itemDto.getProductName());
//			item.setCategory(itemDto.getCategory());
//			item.setSubCategory(itemDto.getSubCategory());
//			item.setPrice(itemDto.getPrice());
//			item.setQty(itemDto.getQty());
//			item.setTotalAmount(itemDto.getTotalAmount());
//			item.setDeliveryId(dmENT.getDeliveryId());
//			item.setTax(itemDto.getTax());
//			item.setDiscount(itemDto.getDiscount());
//			item.setActualAmount(itemDto.getActualAmount());
//			item.setOrderMasterItemId(itemDto.getOrderMasterItemId());
//			dmiDAO.save(item);
//		}
//		// dmDAO.save(dmENT);
//		OrderMasterEntity omENT = omDAO.getOrderById(dmENT.getOrderId());
//		if (omENT != null) {
//			omENT.setStatus("Delivered");
//			omENT.setDeliveryLocation(dmDTO.getDeliveryLocation());
//			omENT.setDeliveryAddress(dmDTO.getDeliveryAddress());
//			omENT.setContactPerson(dmDTO.getContactPerson());
//			omENT.setContactNumber(dmDTO.getContactNumber());
//			omENT.setComments(dmDTO.getComments());
//			omENT.setDeliveryId(dmENT.getDeliveryId());
//			omENT.setDeliveredDate(dmDTO.getDeliveredDate());
//			omDAO.save(omENT);
//		}
//
//		DashBoardEntity dbENT = dbDAO.getDashBoardDetailsByOrderId(dmENT.getOrderId());
//		if (dbENT != null) {
//			dbENT.setDeliveredDate(dmENT.getDeliveredDate());
//			dbENT.setStatus("Delivered");
//			dbENT.setPaymentStatus("open");
//			dbDAO.save(dbENT);
//		}
//
//		PaymentMasterEntity pmENT = new PaymentMasterEntity();
//		pmENT.setCustomerName(dmDTO.getContactPerson());
//		pmENT.setDeliveredDate(dmDTO.getDeliveredDate());
//		pmENT.setDeliveryId(dmENT.getDeliveryId());
//		pmENT.setOrderId(dmDTO.getOrderId());
//		pmENT.setPaidAmount(0);
//		pmENT.setGrossAmount(dmDTO.getTotal());
//		pmENT.setPaymentDate("-");
//		pmENT.setPaymentMode("-");
//		pmENT.setPaymentStatus("open");
//		pmENT.setPayableAmount(0);
//		pmENT.setInvoiceNo(omENT.getInvoiceNo());
//		pmENT.setCustomerId(dmDTO.getCustomerId());
//		pmENT.setReturnCreditUsed(0);
//		if(dmDTO.getCustomerId().startsWith("CUST")) {
//		CustomerMasterEntity cmENT = cmDAO.getAllCustomerById(dmDTO.getCustomerId());
//		pmENT.setReturnCredit(cmENT.getReturnCredit());
//		}
//		else {
//			String userId = dmDTO.getCustomerId();
//			UserMasterEntity umENT = umDAO.getUserById(userId);
//			pmENT.setReturnCredit(umENT.getReturnCredit());
//		}
//		pmDAO.save(pmENT);
//		
//		OrderMasterEntity omEnt = omDAO.getOrderById(dmENT.getOrderId());
//		if (omEnt != null) {
//			omENT.setPaymentId(pmENT.getPaymentId());
//			omDAO.save(omENT);
//		}
//		
//		TransactionsMasterEntity tmENT = new TransactionsMasterEntity();
//		tmENT.setCustomerName(dmDTO.getContactPerson());
//		tmENT.setDeliveredDate(dmDTO.getDeliveredDate());
//		tmENT.setDeliveryId(dmENT.getDeliveryId());
//		tmENT.setOrderId(dmDTO.getOrderId());
//		tmENT.setPaidAmount(0);
//		tmENT.setGrossAmount(dmDTO.getTotal());
//		tmENT.setPaymentDate("-");
//		tmENT.setPaymentMode("-");
//		tmENT.setPaymentStatus("open");
//		tmENT.setPayableAmount(0);
//		tmENT.setInvoiceNo(omENT.getInvoiceNo());
//		tmENT.setCustomerId(omENT.getCustomerId());
//		tmENT.setPaidBy("-");
//		tmDAO.save(tmENT);
//
//		if(pmENT.getReturnCredit() != 0) {
//			TransactionsMasterEntity tmEnt = new TransactionsMasterEntity();
//			tmEnt.setCustomerName(dmDTO.getContactPerson());
//			tmEnt.setDeliveredDate(dmDTO.getDeliveredDate());
//			tmEnt.setDeliveryId(dmENT.getDeliveryId());
//			tmEnt.setOrderId(dmDTO.getOrderId());
//			tmEnt.setPaidAmount(pmENT.getReturnCredit());
//			tmEnt.setGrossAmount(dmDTO.getTotal());
//			LocalDate currentDate = LocalDate.now();
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//			String formattedDate = currentDate.format(formatter);
//			tmEnt.setPaymentDate(formattedDate);
//			tmEnt.setPaymentMode("return credit");
//			tmEnt.setPaymentStatus("partial payment");
//			double payableAmount = dmDTO.getTotal() - pmENT.getReturnCredit();
//			tmEnt.setPayableAmount(payableAmount);
//			tmEnt.setInvoiceNo(omENT.getInvoiceNo());
//			tmEnt.setCustomerId(omENT.getCustomerId());
//			tmEnt.setPaidBy("-");
//			tmEnt.setExactPaidAmount(pmENT.getReturnCredit());
//			tmEnt.setPaidBy("Credit Used");
//			tmDAO.save(tmEnt);
//			
//			System.out.println(tmEnt.getTransactionsId());
//
//			
//			PaymentMasterEntity pmEnt = pmDAO.getPaymentListByInvoiceNo(dmDTO.getInvoiceNo());
//			pmEnt.setPayableAmount(tmEnt.getPayableAmount());
//			pmENT.setPaidAmount(tmEnt.getPaidAmount());
//			pmENT.setPaymentDate(tmEnt.getPaymentDate());
//			pmENT.setPaymentMode(tmEnt.getPaymentMode());
//			pmENT.setPaymentStatus(tmEnt.getPaymentStatus());
//			pmENT.setReturnCredit(0);
//			pmENT.setReturnCreditUsed(tmEnt.getPaidAmount());
//			pmDAO.save(pmENT);
//
//			if (tmENT.getCustomerId() != null && tmENT.getCustomerId().startsWith("CUST")) {
//			CustomerMasterEntity cmENT = cmDAO.getAllCustomerById(tmEnt.getCustomerId());
//			cmENT.setReturnCredit(0);
//			cmDAO.save(cmENT);
//			} else {
//				UserMasterEntity umENT = umDAO.getAllUserById(tmENT.getCustomerId());
//				umENT.setReturnCredit(0);
//				umDAO.save(umENT);
//			}
//
//		}
//			
//		Map<String, Object> response = new LinkedHashMap<>();
//		response.put("status", "success");
//		response.put("id", dmENT.getDeliveryId());
//		response.put("check", tmENT.getTransactionsId());
//		return response;
//
//	}
//
//	public List<DeliveryMasterEntity> getAllDeliveryMaster() {
//		List<DeliveryMasterEntity> deliveryList = dmDAO.findAll();
//		return deliveryList;
//	}
//
//}