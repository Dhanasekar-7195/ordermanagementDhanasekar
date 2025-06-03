package com.ordermanagement.orders.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ordermanagement.dashboard.entity.DashBoardEntity;
import com.ordermanagement.orders.dao.OrderMasterDAO;
import com.ordermanagement.orders.dao.OrderMasterItemDAO;
import com.ordermanagement.orders.dto.OrderMasterDTO;
import com.ordermanagement.orders.dto.OrderMasterItemDTO;
import com.ordermanagement.orders.entity.OrderMasterEntity;
import com.ordermanagement.orders.entity.OrderMasterItemEntity;
import com.ordermanagement.payments.entity.PaymentMasterEntity;

@Component
public class OrderMasterRepository {

	@Autowired
	private OrderMasterDAO omDAO;

	@Autowired
	private OrderMasterItemDAO omiDAO;
	
//	 public Map<String, Object> addOrderMaster(OrderMasterDTO omDTO) {
//	        // Save OrderMasterEntity
//	        OrderMasterEntity omENT = new OrderMasterEntity();
//	        omENT.setOrderId(omDTO.getOrderId());
//	        omENT.setSalesOrderType("OR");
//	        omENT.setSalesOrganization("1810");
//	        omENT.setDistributionChannel("10");
//	        omENT.setOrganizationDivision("00");
//	        omENT.setCustomerId(omDTO.getCustomerId());
//	        omENT.setOrderDate(omDTO.getOrderDate());
//	        omENT.setIncotermsClassification("DAP");
//	        omENT.setIncotermsTransferLocation("Bangalore");
//	        omENT.setContactPerson(omDTO.getContactPerson());
//	        omENT.setDeliveryLocation(omDTO.getDeliveryLocation());
//	        omENT.setPostalCode(omDTO.getPostalCode());
//	        omENT.setStreetName(omDTO.getStreetName());
//	        omENT.setRegion(omDTO.getRegion());
//	        omENT.setTelephoneNumber(omDTO.getTelephoneNumber());
//	        omENT.setTotal(omDTO.getTotal());
//	        omENT.setStatus("Not Started");
//	        omDAO.save(omENT);
//
//	        // Save OrderMasterItemEntities and prepare S/4HANA to_Item payload
//	        List<Map<String, Object>> items = new ArrayList<>();
//	        for (OrderMasterItemDTO itemDto : omDTO.getItems()) {
//	            OrderMasterItemEntity item = new OrderMasterItemEntity();
//	            item.setOrderMasterItemId(itemDto.getOrderMasterItemId());
//	            item.setProduct(itemDto.getProduct());
//	            item.setCategoryName(itemDto.getCategoryName());
//	            item.setProductType(itemDto.getProductType());
//	            item.setBaseUnit(itemDto.getBaseUnit());
//	            item.setProductDescription(itemDto.getProductDescription());
//	            item.setStandardPrice(itemDto.getStandardPrice());
//	            item.setCurrency(itemDto.getCurrency());
//	            item.setQty(itemDto.getQty());
//	            item.setTotalAmount(itemDto.getTotalAmount());
//	            item.setOrderId(omENT.getOrderId());
//	            omiDAO.save(item);
//
//	            // Build S/4HANA item payload
//	            Map<String, Object> s4Item = new HashMap<>();
//	            s4Item.put("Material", item.getProduct());
//	            s4Item.put("RequestedQuantity", item.getQty());
//	            s4Item.put("RequestedQuantityUnit", item.getBaseUnit());
//	            items.add(s4Item);
//	        }
//
//	        // Prepare S/4HANA payload
//	        Map<String, Object> s4Payload = new LinkedHashMap<>();
//	        s4Payload.put("SalesOrderType", "OR");
//	        s4Payload.put("SalesOrganization", "1810");
//	        s4Payload.put("DistributionChannel", "10");
//	        s4Payload.put("OrganizationDivision", "00");
//	        s4Payload.put("SoldToParty", omENT.getCustomerId());
//	        s4Payload.put("RequestedDeliveryDate", omENT.getOrderDate());
//	        s4Payload.put("IncotermsClassification", "DAP");
//	        s4Payload.put("IncotermsTransferLocation", "Bangalore");
//	        s4Payload.put("to_Item", items);
//
//	        // Prepare S/4HANA response map
//	        Map<String, Object> s4HanaResponse = new LinkedHashMap<>();
//	        Map<String, Object> response = new LinkedHashMap<>();
//	        
//	        // Container for s4HanaMessage
//	        final Map<String, String> s4HanaMessage = new HashMap<>();
//	        
//	        // Send data to S/4HANA with delay using a scheduler
//	        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
//	        // Schedule the task to run once after 1 minute delay
//	        scheduler.schedule(() -> {
//	            try {
//	                String s4hanaApiUrl = "https://my403545-api.s4hana.cloud.sap/sap/opu/odata/sap/API_SALES_ORDER_SRV/A_SalesOrder";
//	                String username = "INTEGRATION";
//	                String password = "gNQH>ydgzvCKfwuxQdcEDQzHMz5YPQfaLxzPHqLz";
//
//	                RestTemplate restTemplate = new RestTemplate();
//	                HttpHeaders getHeaders = new HttpHeaders();
//	                getHeaders.setBasicAuth(username, password);
//	                getHeaders.set("x-csrf-token", "fetch");
//
//	                HttpEntity<Void> csrfRequest = new HttpEntity<>(getHeaders);
//	                ResponseEntity<String> csrfResponse = restTemplate.exchange(
//	                        s4hanaApiUrl, HttpMethod.GET, csrfRequest, String.class);
//
//	                if (csrfResponse.getStatusCode() == HttpStatus.OK) {
//	                    String csrfToken = csrfResponse.getHeaders().getFirst("x-csrf-token");
//	                    List<String> cookies = csrfResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
//
//	                    HttpHeaders postHeaders = new HttpHeaders();
//	                    postHeaders.setBasicAuth(username, password);
//	                    postHeaders.set("x-csrf-token", csrfToken);
//	                    postHeaders.setContentType(MediaType.APPLICATION_JSON);
//	                    postHeaders.put(HttpHeaders.COOKIE, cookies);
//
//	                    HttpEntity<Map<String, Object>> postRequest = new HttpEntity<>(s4Payload, postHeaders);
//	                    ResponseEntity<String> postResponse = restTemplate.exchange(
//	                            s4hanaApiUrl, HttpMethod.POST, postRequest, String.class);
//
//	                    if (postResponse.getStatusCode() == HttpStatus.CREATED) {
//	                        String salesOrderId = postResponse.getBody().split("'")[1];
//	                        omENT.setSalesOrderId(salesOrderId);
//	                        omDAO.save(omENT);
//
//	                        s4HanaMessage.put("message", "Order added to S/4HANA successfully");
//	                        s4HanaResponse.put("status", "success");
//	                    } else {
//	                        s4HanaMessage.put("message", "Failed to add order to S/4HANA");
//	                        s4HanaResponse.put("status", "failure");
//	                    }
//	                }
//	            } catch (Exception e) {
//	                s4HanaMessage.put("message", "Error while connecting to S/4HANA: " + e.getMessage());
//	                s4HanaResponse.put("status", "failure");
//	            }
//	        }, 1, TimeUnit.MINUTES);
//
//	        // Prepare API response
//	        response.put("status", "success");
//	        response.put("orderId", omENT.getOrderId());
//	        response.put("s4HanaStatus", s4HanaResponse.get("status"));
//	        response.put("s4HanaMessage", s4HanaMessage.get("message"));
//
//	        return response;
//	    }


	
	public Map<String, Object> addOrderMaster(OrderMasterDTO omDTO) {
	    // Save OrderMasterEntity
	    OrderMasterEntity omENT = new OrderMasterEntity();
	    omENT.setOrderId(omDTO.getOrderId());
	    omENT.setSalesOrderType("OR");
	    omENT.setSalesOrganization("1810");
	    omENT.setDistributionChannel("10");
	    omENT.setOrganizationDivision("00");
	    omENT.setCustomerId(omDTO.getCustomerId());
	    omENT.setOrderDate(omDTO.getOrderDate());
	    omENT.setIncotermsClassification("DAP");
	    omENT.setIncotermsTransferLocation("Bangalore");
	    omENT.setContactPerson(omDTO.getContactPerson());
	    omENT.setDeliveryLocation(omDTO.getDeliveryLocation());
	    omENT.setPostalCode(omDTO.getPostalCode());
	    omENT.setStreetName(omDTO.getStreetName());
	    omENT.setRegion(omDTO.getRegion());
	    omENT.setTelephoneNumber(omDTO.getTelephoneNumber());
	    omENT.setTotal(omDTO.getTotal());
	    omENT.setStatus("Not Started");
	    omDAO.save(omENT);

	    // Save OrderMasterItemEntities and prepare S/4HANA to_Item payload
	    List<Map<String, Object>> items = new ArrayList<>();
	    for (OrderMasterItemDTO itemDto : omDTO.getItems()) {
	        OrderMasterItemEntity item = new OrderMasterItemEntity();
	        item.setOrderMasterItemId(itemDto.getOrderMasterItemId());
	        item.setProduct(itemDto.getProduct());
	        item.setCategoryName(itemDto.getCategoryName());
	        item.setProductType(itemDto.getProductType());
	        item.setBaseUnit(itemDto.getBaseUnit());
	        item.setProductDescription(itemDto.getProductDescription());
	        item.setStandardPrice(itemDto.getStandardPrice());
	        item.setCurrency(itemDto.getCurrency());
	        item.setQty(itemDto.getQty());
	        item.setTotalAmount(itemDto.getTotalAmount());
	        item.setOrderId(omENT.getOrderId());
	        omiDAO.save(item);

	        // Build S/4HANA item payload
	        Map<String, Object> s4Item = new HashMap<>();
	        s4Item.put("Material", item.getProduct());
	        s4Item.put("RequestedQuantity", item.getQty());
	        s4Item.put("RequestedQuantityUnit", item.getBaseUnit());
	        items.add(s4Item);
	    }
	    
	   // String orderDate = omENT.getOrderDate();
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDate localDate = LocalDate.parse(omENT.getOrderDate(), inputFormatter);
        String formattedDate = localDate.atStartOfDay().format(outputFormatter);

	    // Prepare S/4HANA payload
	    Map<String, Object> s4Payload = new LinkedHashMap<>();
	    s4Payload.put("SalesOrderType", "OR");
	    s4Payload.put("SalesOrganization", "1810");
	    s4Payload.put("DistributionChannel", "10");
	    s4Payload.put("OrganizationDivision", "00");
	    s4Payload.put("SoldToParty", omENT.getCustomerId());
	    s4Payload.put("RequestedDeliveryDate", formattedDate);
	    s4Payload.put("IncotermsClassification", "DAP");
	    s4Payload.put("IncotermsTransferLocation", "Bangalore");
	    s4Payload.put("to_Item", items);

	    // Send data to S/4HANA
	    Map<String, Object> s4HanaResponse = new LinkedHashMap<>();
	    String s4HanaMessage = "";
	    try {
	        String s4hanaApiUrl = "https://my403545-api.s4hana.cloud.sap/sap/opu/odata/sap/API_SALES_ORDER_SRV/A_SalesOrder";
	        String username = "INTEGRATION";
	        String password = "UT8BsHhZkz-cPbMRcvCiaMRzqngFlSAQZTxZBvGM";
		      //  String password = "gNQH>ydgzvCKfwuxQdcEDQzHMz5YPQfaLxzPHqLz";

	        

	        RestTemplate restTemplate = new RestTemplate();
	        HttpHeaders getHeaders = new HttpHeaders();
	        getHeaders.setBasicAuth(username, password);
	        getHeaders.set("x-csrf-token", "fetch");

	        HttpEntity<Void> csrfRequest = new HttpEntity<>(getHeaders);
	        ResponseEntity<String> csrfResponse = restTemplate.exchange(
	                s4hanaApiUrl, HttpMethod.GET, csrfRequest, String.class);

	        if (csrfResponse.getStatusCode() == HttpStatus.OK) {
	            String csrfToken = csrfResponse.getHeaders().getFirst("x-csrf-token");
	            List<String> cookies = csrfResponse.getHeaders().get(HttpHeaders.SET_COOKIE);

	            HttpHeaders postHeaders = new HttpHeaders();
	            postHeaders.setBasicAuth(username, password);
	            postHeaders.set("x-csrf-token", csrfToken);
	            postHeaders.setContentType(MediaType.APPLICATION_JSON);
	            postHeaders.put(HttpHeaders.COOKIE, cookies);

	            HttpEntity<Map<String, Object>> postRequest = new HttpEntity<>(s4Payload, postHeaders);
	            ResponseEntity<String> postResponse = restTemplate.exchange(
	                    s4hanaApiUrl, HttpMethod.POST, postRequest, String.class);

	            if (postResponse.getStatusCode() == HttpStatus.CREATED) {
	                String salesOrderId = postResponse.getBody().split("'")[1];
	                omENT.setSalesOrderId(salesOrderId);
	                omDAO.save(omENT);

	                s4HanaMessage = "Order added to S/4HANA successfully";
	                s4HanaResponse.put("status", "success");
	            } else {
	                s4HanaMessage = "Failed to add order to S/4HANA";
	                s4HanaResponse.put("status", "failure");
	            }
	        }
	    } catch (Exception e) {
	        s4HanaMessage = "Error while connecting to S/4HANA: " + e.getMessage();
	        s4HanaResponse.put("status", "failure");
	    }

	    // Prepare API response
	    Map<String, Object> response = new LinkedHashMap<>();
	    response.put("status", "success");
	    response.put("orderId", omENT.getOrderId());
	    response.put("s4HanaStatus", s4HanaResponse.get("status"));
	    response.put("s4HanaMessage", s4HanaMessage);
	    response.put("sales", omENT.getSalesOrderId());

	    return response;
	}

	
//	public Map<String, Object> addOrderMaster(OrderMasterDTO omDTO) {
//	    OrderMasterEntity omENT = new OrderMasterEntity();
//	    omENT.setOrderId(omDTO.getOrderId());
//	    omENT.setOrderDate(omDTO.getOrderDate());
//	    omENT.setDeliveryLocation(omDTO.getDeliveryLocation());
//	    omENT.setDeliveryAddress(omDTO.getDeliveryAddress());
//	    omENT.setContactPerson(omDTO.getContactPerson());
//	    omENT.setContactNumber(omDTO.getContactNumber());
//	    omENT.setComments(omDTO.getComments());
//	    omENT.setPaymentId("-");
//	    omENT.setDeliveryId("-");
//	    omENT.setPayableAmount(0);
//	    omENT.setPaidAmount(0);
//	    omENT.setDeliveredDate("-");
//	    omENT.setPaymentDate("-");
//	    omENT.setPaymentMode("-");
//	    omENT.setPaymentStatus("-");
//	    omENT.setTotal(omDTO.getTotal());
//	    omENT.setStatus(omDTO.getStatus());
//	    omENT.setCustomerId(omDTO.getCustomerId());
//	    omDAO.save(omENT);
//
//	    OrderMasterEntity orderMasterEntity = omDAO.getOrderById(omENT.getOrderId());
//	    String orderId = omENT.getOrderId();
//	    String invoiceNo = orderId.replace("ORD_", "INV_");
//	    orderMasterEntity.setInvoiceNo(invoiceNo);
//
//	    if (omENT.getStatus().equals("-")) {
//	        String draftId = orderId.replace("ORD_", "DRFT_");
//	        orderMasterEntity.setDraftId(draftId);
//	    }
//	    omDAO.save(orderMasterEntity);
//
//	    for (OrderMasterItemDTO itemDto : omDTO.getItems()) {
//	        OrderMasterItemEntity item = new OrderMasterItemEntity();
//	        item.setOrderMasterItemId(itemDto.getOrderMasterItemId());
//	        item.setProductName(itemDto.getProductName());
//	        item.setCategory(itemDto.getCategory());
//	        item.setSubCategory(itemDto.getSubCategory());
//	        item.setPrice(itemDto.getPrice());
//	        item.setQty(itemDto.getQty());
//	        item.setTotalAmount(itemDto.getTotalAmount());
//	        item.setOrderId(omENT.getOrderId());
//	        item.setTax(itemDto.getTax());
//	        item.setDiscount(itemDto.getDiscount());
//	        item.setActualAmount(itemDto.getActualAmount());
//	        omiDAO.save(item);
//	    }
//if(!omENT.getStatus().equals("-")) {
//	    DashBoardEntity dashBoardEntity = new DashBoardEntity();
//	    dashBoardEntity.setOrderId(omENT.getOrderId());
//	    dashBoardEntity.setCreatedDate(omDTO.getOrderDate());
//	    dashBoardEntity.setDeliveredDate("-");
//	    dashBoardEntity.setTotalAmount(omDTO.getTotal());
//	    dashBoardEntity.setStatus("Not Started");
//	    dashBoardEntity.setPaymentStatus("To be Done");
//	    dashBoardEntity.setCustomerId(omDTO.getCustomerId());
//	    dbDAO.save(dashBoardEntity);
//}
//
//	    Map<String, Object> s4HanaResponse = new LinkedHashMap<>();
//
//	    Map<String, Object> s4Payload = new HashMap<>();
//	    s4Payload.put("SalesOrderType", "OR");
//	    s4Payload.put("SalesOrganization", "1810");
//	    s4Payload.put("DistributionChannel", "10");
//	    s4Payload.put("OrganizationDivision", "00");
//	    s4Payload.put("SoldToParty", "");
//	   // s4Payload.put("RequestedDeliveryDate", "2024-11-17T00:00:00");
//	    s4Payload.put("RequestedDeliveryDate", "");
//	    s4Payload.put("IncotermsClassification", "DAP");
//	    s4Payload.put("IncotermsTransferLocation", "Bangalore");
//
//	    List<Map<String, Object>> items = new ArrayList<>();
//	    Map<String, Object> item = new HashMap<>();
//	    item.put("Material", "");
//	    item.put("RequestedQuantity", "");
//	    item.put("RequestedQuantityUnit", "");
//
//	    List<Map<String, Object>> pricingElements = new ArrayList<>();
//	    Map<String, Object> pricingElement = new HashMap<>();
//	    pricingElement.put("ConditionType", "PPR0");
//	    pricingElement.put("ConditionRateValue", "");
//	    pricingElements.add(pricingElement);
//
//	    item.put("to_PricingElement", pricingElements);
//	    items.add(item);
//	    s4Payload.put("to_Item", items);
//
//	    String s4HanaMessage = "";
//	    try {
//	        String s4hanaApiUrl = "https://my403545-api.s4hana.cloud.sap/sap/opu/odata/sap/API_SALES_ORDER_SRV/A_SalesOrder";
//	        String username = "INTEGRATION";
//	        String password = "gNQH>ydgzvCKfwuxQdcEDQzHMz5YPQfaLxzPHqLz";
//
//	        RestTemplate restTemplate = new RestTemplate();
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
//	                String salesOrderId = postResponse.getBody().split("'")[1];
//	                omENT.setSalesOrderId(salesOrderId);
//	                omDAO.save(omENT);
//
// 
//	                s4HanaMessage = "Order added to S/4HANA successfully";
//	                s4HanaResponse.put("status", "success");
//	            } else {
//	                s4HanaMessage = "Failed to add order to S/4HANA";
//	                s4HanaResponse.put("status", "failure");
//	            }
//	        }
//	    } catch (Exception e) {
//	        s4HanaMessage = "Error while connecting to S/4HANA: " + e.getMessage();
//	        s4HanaResponse.put("status", "failure");
//	    }
//
//	    // Combine local and S/4HANA responses
//	    Map<String, Object> response = new LinkedHashMap<>();
//	    response.put("status", "success");
//	    response.put("orderId", omENT.getOrderId());
//	    response.put("invoiceNo", orderMasterEntity.getInvoiceNo());
//	    response.put("draftId", orderMasterEntity.getDraftId());
//	    response.put("s4HanaStatus", s4HanaResponse.get("status"));
//	    response.put("s4HanaMessage", s4HanaMessage);
//
//	    return response;
//	}


//	public Map<String, Object> addOrderMaster(OrderMasterDTO omDTO) {
//		OrderMasterEntity omENT = new OrderMasterEntity();
//		omENT.setOrderId(omDTO.getOrderId());
//		omENT.setOrderDate(omDTO.getOrderDate());
//		omENT.setDeliveryLocation(omDTO.getDeliveryLocation());
//		omENT.setDeliveryAddress(omDTO.getDeliveryAddress());
//		omENT.setContactPerson(omDTO.getContactPerson());
//		omENT.setContactNumber(omDTO.getContactNumber());
//		omENT.setComments(omDTO.getComments());
//		omENT.setPaymentId("-");
//		omENT.setDeliveryId("-");
//		omENT.setPayableAmount(0);
//		omENT.setPaidAmount(0);
//		omENT.setDeliveredDate("-");
//		omENT.setPaymentDate("-");
//		omENT.setPaymentMode("-");
//		omENT.setPaymentStatus("-");
//		omENT.setTotal(omDTO.getTotal());
//		omENT.setStatus(omDTO.getStatus());
//		omENT.setCustomerId(omDTO.getCustomerId());
//		omDAO.save(omENT);
//
//		OrderMasterEntity orderMasterEntity = omDAO.getOrderById(omENT.getOrderId());
//		String orderId = omENT.getOrderId();
//		String invoiceNo = orderId.replace("ORD_", "INV_");
//		orderMasterEntity.setInvoiceNo(invoiceNo);
//		
//		if(omENT.getStatus().equals("-")) {
//			String draftId = orderId.replace("ORD_", "DRFT_");
//			orderMasterEntity.setDraftId(draftId);
//		}
//		omDAO.save(orderMasterEntity);
//
//		for (OrderMasterItemDTO itemDto : omDTO.getItems()) {
//			OrderMasterItemEntity item = new OrderMasterItemEntity();
//			item.setOrderMasterItemId(itemDto.getOrderMasterItemId());
//			item.setProductName(itemDto.getProductName());
//			item.setCategory(itemDto.getCategory());
//			item.setSubCategory(itemDto.getSubCategory());
//			item.setPrice(itemDto.getPrice());
//			item.setQty(itemDto.getQty());
//			item.setTotalAmount(itemDto.getTotalAmount());
//			item.setOrderId(omENT.getOrderId());
//			item.setTax(itemDto.getTax());
//			item.setDiscount(itemDto.getDiscount());
//			item.setActualAmount(itemDto.getActualAmount());
//			omiDAO.save(item);
//		}
//
//		DashBoardEntity dashBoardEntity = new DashBoardEntity();
//		dashBoardEntity.setOrderId(omENT.getOrderId());
//		dashBoardEntity.setCreatedDate(omDTO.getOrderDate());
//		dashBoardEntity.setDeliveredDate("-");
//		dashBoardEntity.setTotalAmount(omDTO.getTotal());
//		dashBoardEntity.setStatus("Not Started");
//		dashBoardEntity.setPaymentStatus("To be Done");
//		dashBoardEntity.setCustomerId(omDTO.getCustomerId());
//		dbDAO.save(dashBoardEntity);
//
//		Map<String, Object> response = new LinkedHashMap<>();
//		response.put("status", "success");
//		response.put("id", omENT.getOrderId());
//		response.put("invoiceNo", omENT.getInvoiceNo());
//		response.put("draftId", omENT.getDraftId());
//		return response;
//
//	}

//	public Map<String, Object> updateOrderMaster(OrderMasterDTO omDTO) {
//		OrderMasterEntity omENT = new OrderMasterEntity();
//		omENT.setOrderId(omDTO.getOrderId());
//		omENT.setOrderDate(omDTO.getOrderDate());
//		omENT.setDeliveryLocation(omDTO.getDeliveryLocation());
//		omENT.setDeliveryAddress(omDTO.getDeliveryAddress());
//		omENT.setContactPerson(omDTO.getContactPerson());
//		omENT.setContactNumber(omDTO.getContactNumber());
//		omENT.setComments(omDTO.getComments());
//		omENT.setTotal(omDTO.getTotal());
//		omENT.setStatus("Not Started");
//		omDAO.save(omENT);
//		for (OrderMasterItemDTO itemDto : omDTO.getItems()) {
//			OrderMasterItemEntity item = new OrderMasterItemEntity();
//			item.setOrderMasterItemId(itemDto.getOrderMasterItemId());
//			item.setProductName(itemDto.getProductName());
//			item.setCategory(itemDto.getCategory());
//			item.setSubCategory(itemDto.getSubCategory());
//			item.setPrice(itemDto.getPrice());
//			item.setQty(itemDto.getQty());
//			item.setTotalAmount(itemDto.getTotalAmount());
//			item.setOrderId(omENT.getOrderId());
//			item.setTax(itemDto.getTax());
//			item.setDiscount(itemDto.getDiscount());
//			item.setActualAmount(itemDto.getActualAmount());
//			omiDAO.save(item);
//		}
//		Map<String, Object> response = new LinkedHashMap<>();
//		response.put("status", "success");
//		response.put("id", omENT.getOrderId());
//		return response;
//	}
	
//	@Transactional
//	public Map<String, Object> addUpdateDeleteOrderMaster(OrderMasterDTO omDTO) {
//	    OrderMasterEntity omENT;
//
//	    // Determine whether to use orderId or draftId
//	    if (omDTO.getOrderId() != null) {
//	        omENT = omDAO.findById(omDTO.getOrderId())
//	                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
//	    } else if (omDTO.getDraftId() != null) {
//	        omENT = omDAO.findByDraftId(omDTO.getDraftId())
//	                .orElseThrow(() -> new EntityNotFoundException("Draft not found"));
//	    } else {
//	        throw new IllegalArgumentException("Both orderId and draftId are null");
//	    }
//
//	    // Update order master fields
//	    omENT.setOrderDate(omDTO.getOrderDate());
//	    omENT.setDeliveryLocation(omDTO.getDeliveryLocation());
//	    omENT.setDeliveryAddress(omDTO.getDeliveryAddress());
//	    omENT.setContactPerson(omDTO.getContactPerson());
//	    omENT.setContactNumber(omDTO.getContactNumber());
//	    omENT.setComments(omDTO.getComments());
//	    omENT.setTotal(omDTO.getTotal());
//	    omENT.setStatus("Not Started");
//	    omENT.setPaymentId("-");
//	    omENT.setDeliveryId("-");
//	    omENT.setPayableAmount(0);
//	    omENT.setPaidAmount(0);
//	    omENT.setDeliveredDate("-");
//	    omENT.setPaymentDate("-");
//	    omENT.setPaymentMode("-");
//	    omENT.setPaymentStatus("-");
//	    omENT.setInvoiceNo(omDTO.getInvoiceNo());
//	    omENT.setCustomerId(omDTO.getCustomerId());
//	    if (omENT.getDraftId() != null) {
//	        omENT.setDraftId(null);
//	    }
//	    omDAO.save(omENT);
//
//	    // Fetch existing items
//	    List<OrderMasterItemEntity> existingItems = omiDAO.findByOrderId(omENT.getOrderId());
//	    Map<String, OrderMasterItemEntity> existingItemsMap = new HashMap<>();
//	    for (OrderMasterItemEntity item : existingItems) {
//	        existingItemsMap.put(item.getOrderMasterItemId(), item);
//	    }
//
//	    // Update or add new items
//	    for (OrderMasterItemDTO itemDto : omDTO.getItems()) {
//	        OrderMasterItemEntity item;
//	        if (existingItemsMap.containsKey(itemDto.getOrderMasterItemId())) {
//	            // Update existing item
//	            item = existingItemsMap.get(itemDto.getOrderMasterItemId());
//	        } else {
//	            // Add new item
//	            item = new OrderMasterItemEntity();
//	            item.setOrderMasterItemId(itemDto.getOrderMasterItemId());
//	            item.setOrderId(omENT.getOrderId());
//	        }
//	        item.setProductName(itemDto.getProductName());
//	        item.setCategory(itemDto.getCategory());
//	        item.setSubCategory(itemDto.getSubCategory());
//	        item.setPrice(itemDto.getPrice());
//	        item.setQty(itemDto.getQty());
//	        item.setTotalAmount(itemDto.getTotalAmount());
//	        item.setTax(itemDto.getTax());
//	        item.setDiscount(itemDto.getDiscount());
//	        item.setActualAmount(itemDto.getActualAmount());
//	        omiDAO.save(item);
//	        // Remove the processed item from the map
//	        existingItemsMap.remove(itemDto.getOrderMasterItemId());
//	    }
//
//	    // Delete items that are no longer in the order
//	    for (OrderMasterItemEntity existingItem : existingItemsMap.values()) {
//	        omiDAO.delete(existingItem);
//	    }
//
////	    DashBoardEntity dashBoardEntity = dbDAO.getDashBoardDetailsByOrderId(omENT.getOrderId());
////	    if(dashBoardEntity.getOrderId() != null) {	    
////	    dashBoardEntity.setTotalAmount(omDTO.getTotal());
////	    dbDAO.save(dashBoardEntity);
////	    }
////	  //  }
////	    else {
////	    	DashBoardEntity dashBoardEnt = dbDAO.getDashBoardDetailsByDraftId(omENT.getDraftId());
////	    	dashBoardEnt.setTotalAmount(omDTO.getTotal());
////		    dbDAO.save(dashBoardEnt);
//////	    	
////	    }
//	    
//	    DashBoardEntity dashBoardEntity = dbDAO.getDashBoardDetailsByOrderId(omENT.getOrderId());
//
//	    if (dashBoardEntity != null && dashBoardEntity.getOrderId() != null) {
//	        // Proceed to set the total amount and save
//	        dashBoardEntity.setTotalAmount(omDTO.getTotal());
//	        dbDAO.save(dashBoardEntity);
//	    } else {
//	    	 DashBoardEntity dbEnt = new DashBoardEntity();
//	 	    dashBoardEntity.setOrderId(omENT.getOrderId());
//	 	    dashBoardEntity.setCreatedDate(omDTO.getOrderDate());
//	 	    dashBoardEntity.setDeliveredDate("-");
//	 	    dashBoardEntity.setTotalAmount(omDTO.getTotal());
//	 	    dashBoardEntity.setStatus("Not Started");
//	 	    dashBoardEntity.setPaymentStatus("To be Done");
//	 	    dashBoardEntity.setCustomerId(omDTO.getCustomerId());
//	 	    dbDAO.save(dashBoardEntity);
//	        // Handle the case where dashBoardEntity is null
////	        DashBoardEntity dashBoardEnt = dbDAO.getDashBoardDetailsByDraftId(omENT.getDraftId());
////	        
////	        if (dashBoardEnt != null) {
////	            // Proceed to set the total amount and save
////	            dashBoardEnt.setTotalAmount(omDTO.getTotal());
////	            dbDAO.save(dashBoardEnt);
////	        } else {
////	            // Handle the case where dashBoardEnt is also null
////	            // You might want to log this situation or throw an exception
////	            System.out.println("No dashboard entity found for both order ID and draft ID.");
////	        }
//	    }
//
//	    Map<String, Object> response = new LinkedHashMap<>();
//	    response.put("status", "success");
//	    response.put("id", omENT.getOrderId());
//	    return response;
//	}


//	@Transactional
//	public Map<String, Object> addUpdateDeleteOrderMaster(OrderMasterDTO omDTO) {
//		// Find the order master entity by orderId
//		OrderMasterEntity omENT = omDAO.findById(omDTO.getOrderId())
//				.orElseThrow(() -> new EntityNotFoundException("Order not found"));
//
//		// Update order master fields
//		omENT.setOrderDate(omDTO.getOrderDate());
//		omENT.setDeliveryLocation(omDTO.getDeliveryLocation());
//		omENT.setDeliveryAddress(omDTO.getDeliveryAddress());
//		omENT.setContactPerson(omDTO.getContactPerson());
//		omENT.setContactNumber(omDTO.getContactNumber());
//		omENT.setComments(omDTO.getComments());
//		omENT.setTotal(omDTO.getTotal());
//		omENT.setStatus("Not Started");
//		omENT.setPaymentId("-");
//		omENT.setDeliveryId("-");
//		omENT.setPayableAmount(0);
//		omENT.setPaidAmount(0);
//		omENT.setDeliveredDate("-");
//		omENT.setPaymentDate("-");
//		omENT.setPaymentMode("-");
//		omENT.setPaymentStatus("-");
//		omENT.setInvoiceNo(omDTO.getInvoiceNo());
//		omENT.setCustomerId(omDTO.getCustomerId());
//		if(omENT.getDraftId()!=null)
//		{
//			omENT.setDraftId(null);
//		}
//		omDAO.save(omENT);
//
//		// Fetch existing items
//		List<OrderMasterItemEntity> existingItems = omiDAO.findByOrderId(omENT.getOrderId());
//		Map<String, OrderMasterItemEntity> existingItemsMap = new HashMap<>();
//		for (OrderMasterItemEntity item : existingItems) {
//			existingItemsMap.put(item.getOrderMasterItemId(), item);
//		}
//
//		// Update or add new items
//		for (OrderMasterItemDTO itemDto : omDTO.getItems()) {
//			OrderMasterItemEntity item;
//			if (existingItemsMap.containsKey(itemDto.getOrderMasterItemId())) {
//				// Update existing item
//				item = existingItemsMap.get(itemDto.getOrderMasterItemId());
//			} else {
//				// Add new item
//				item = new OrderMasterItemEntity();
//				item.setOrderMasterItemId(itemDto.getOrderMasterItemId());
//				item.setOrderId(omENT.getOrderId());
//			}
//			item.setProductName(itemDto.getProductName());
//			item.setCategory(itemDto.getCategory());
//			item.setSubCategory(itemDto.getSubCategory());
//			item.setPrice(itemDto.getPrice());
//			item.setQty(itemDto.getQty());
//			item.setTotalAmount(itemDto.getTotalAmount());
//			item.setTax(itemDto.getTax());
//			item.setDiscount(itemDto.getDiscount());
//			item.setActualAmount(itemDto.getActualAmount());
//			omiDAO.save(item);
//			// Remove the processed item from the map
//			existingItemsMap.remove(itemDto.getOrderMasterItemId());
//		}
//
//		// Delete items that are no longer in the order
//		for (OrderMasterItemEntity existingItem : existingItemsMap.values()) {
//			omiDAO.delete(existingItem);
//		}
//
//		DashBoardEntity dashBoardEntity = dbDAO.getDashBoardDetailsByOrderId(omENT.getOrderId());
//		dashBoardEntity.setTotalAmount(omDTO.getTotal());
//		dbDAO.save(dashBoardEntity);
//
//		Map<String, Object> response = new LinkedHashMap<>();
//		response.put("status", "success");
//		response.put("id", omENT.getOrderId());
//		return response;
//	}

	public List<OrderMasterEntity> searchByProductName(String orderId) {
		List<OrderMasterEntity> vList = omDAO.getAllOrderById(orderId);
		return vList;
	}

	public List<OrderMasterEntity> getAllOrderMaster() {
		List<OrderMasterEntity> orderList = omDAO.findAll();
		return orderList;
	}

	public List<OrderMasterEntity> getOrdersByCustomerId(String customerId) {
		List<OrderMasterEntity> orderList = omDAO.getOrdersByCustomerId(customerId);
		return orderList;
	}

//	public Map<String, Object> deleteByOrderId(String orderId) {
//		OrderMasterEntity sval = omDAO.getOrderById(orderId);
//		if ("-".equals(sval.getStatus())) {
//			omDAO.deleteById(orderId);
//			Map<String, Object> response = new LinkedHashMap<>();
//			response.put("status", "success");
//			return response;
//		}
//		else if(sval.getStatus()!=null)
//		{
//			sval.setDraftId(null);
//			omDAO.save(sval);
//			Map<String, Object> response = new LinkedHashMap<>();
//			response.put("status", "success");
//			return response;
//		}
//		Map<String, Object> response = new LinkedHashMap<>();
//		response.put("status", "204");
//		response.put("id", "not_found");
//		return response;
//	}

	public List<OrderMasterEntity> getAllDraftMaster() {
		List<OrderMasterEntity> orderList = omDAO.getAllDraftMaster();
		return orderList;	
		}


	public Map<String, Double> getOrderCounts() {
	    List<OrderMasterEntity> getOrder = omDAO.findAll();
	    
	    Map<String, Double> statusCounts = new HashMap<>();
	    int notStartedCount = 0;

	    // Count statuses from DashBoardEntity
	    for (OrderMasterEntity omEnt : getOrder) {
	        String status = omEnt.getStatus();

	        if ("Not Started".equals(status)) {
	            notStartedCount++;
	        } 

	    // Populate the statusCounts map
	    statusCounts.put("Not Started", (double) notStartedCount);
	    }

	    return statusCounts;
	}


	public Map<String, Double> getCustomerOrderCounts(String customerId) {
List<OrderMasterEntity> getOrder = omDAO.getOrdersByCustomerId(customerId);
	    
	    Map<String, Double> statusCounts = new HashMap<>();
	    int notStartedCount = 0;

	    // Count statuses from DashBoardEntity
	    for (OrderMasterEntity omEnt : getOrder) {
	        String status = omEnt.getStatus();

	        if ("Not Started".equals(status)) {
	            notStartedCount++;
	        } 

	    // Populate the statusCounts map
	    statusCounts.put("Not Started", (double) notStartedCount);
	    }

	    return statusCounts;
	}

//	public String deleteAllOrderMaster() {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	public OrderMasterEntity getDraftById(String draftId) {
//		return omDAO.getDraftById(draftId);
//	}

}
