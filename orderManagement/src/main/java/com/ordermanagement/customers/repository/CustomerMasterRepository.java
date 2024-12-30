package com.ordermanagement.customers.repository;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordermanagement.customers.dao.CustomerMasterDAO;
import com.ordermanagement.customers.dao.SaveAllCustomerDAO;
import com.ordermanagement.customers.dto.CustomerMasterDTO;
import com.ordermanagement.customers.entity.CustomerMasterEntity;
import com.ordermanagement.customers.entity.SaveAllCustomerEntity;
import com.ordermanagement.products.entity.SaveAllProductEntity;
import com.ordermanagement.user.entity.UserMasterEntity;

@Component
public class CustomerMasterRepository {

	@Autowired
	private CustomerMasterDAO cmDAO;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@Autowired
	private SaveAllCustomerDAO sacDAO;
	
	public Map<String, Object> addCustomerMaster(CustomerMasterDTO cmDTO) {
		CustomerMasterEntity cmENT = new CustomerMasterEntity();
		cmENT.setCustomerName(cmDTO.getCustomerName());
		cmENT.setContactNo(cmDTO.getContactNo());
		cmENT.setEmail(cmDTO.getEmail());
		cmENT.setDeliveryLocation(cmDTO.getDeliveryLocation());
		cmENT.setBillingAddress(cmDTO.getBillingAddress());
		cmENT.setShippingAddress1(cmDTO.getShippingAddress1());
		cmENT.setShippingAddress2(cmDTO.getShippingAddress2());
		cmENT.setReturnCredit(0);
						
		if (cmENT.getContactNo().length() != 10 || !cmENT.getContactNo().matches("\\d+")) {
	        Map<String, Object> response = new LinkedHashMap<>();
	        response.put("status", "failed");
	        response.put("code", "510");
	        response.put("error", "Please enter a valid 10-digit mobile number");
	       return response;
	    }
		
		if (cmENT.getShippingAddress2().equalsIgnoreCase(cmENT.getShippingAddress1())) {
	        Map<String, Object> response = new LinkedHashMap<>();
	        response.put("status", "failed");
	        response.put("code", "511");
	        response.put("error", "Shipping Address 2 should not be the same as Shipping Address 1");
	        return response;
	    }
		
//		if (!cmENT.getEmail().endsWith("@gmail.com")) {
//	        Map<String, Object> response = new LinkedHashMap<>();
//	        response.put("status", "failed");
//	        response.put("code", "400");
//	        response.put("error", "Email must contain '@gmail.com'");
//	        return response;
//	    }
		CustomerMasterEntity custEmail = this.cmDAO.findCustomerByEmailBy(cmENT.getEmail());
		if (custEmail != null) {
			Map<String, Object> response = new LinkedHashMap<>();
			response.put("status", "failed");
			response.put("code", "509");
			response.put("error", "email already exist");
			return response;
		}
		
		CustomerMasterEntity custMble = this.cmDAO.findCustomerByPhoneNoBy(cmENT.getContactNo());
		if (custMble != null) {
			Map<String, Object> response = new LinkedHashMap<>();
			response.put("status", "failed");
			response.put("code", "509");
			response.put("error", "mobile number already exists");
			return response;
		}
		CustomerMasterEntity custName = this.cmDAO.findCustomerExists(cmDTO.getCustomerName());
				if(custName != null) {
				Map<String, Object> response = new LinkedHashMap<>();
		        response.put("status", "failed");
		        response.put("code", "508");
		        response.put("error", "Customer name already exists");
		        return response;
			}
		
		cmDAO.save(cmENT);
	    Map<String, Object> s4HanaResponse = new LinkedHashMap<>();

		 // Prepare the payload for S/4HANA API
	    Map<String, Object> s4Payload = new HashMap<>();
	    s4Payload.put("BusinessPartnerGrouping", "BP04"); // Customer Name
	    s4Payload.put("BusinessPartnerCategory", "2"); // Contact Number
	    s4Payload.put("OrganizationBPName1", cmENT.getCustomerName()); // Email
	    s4Payload.put("SearchTerm1", cmENT.getDeliveryLocation()); // Delivery Location
	    s4Payload.put("LegalForm", "01"); // Billing Address
	    s4Payload.put("BusinessPartnerIsBlocked", false); // Shipping Address 1

	    // Prepare a response map for S4Hana response

	    try {
            String s4hanaApiUrl = "https://my403545-api.s4hana.cloud.sap/sap/opu/odata/sap/API_BUSINESS_PARTNER/A_BusinessPartner";
            String username = "INTEGRATION"; // Replace with actual username
            String password = "gNQH>ydgzvCKfwuxQdcEDQzHMz5YPQfaLxzPHqLz"; // Replace with actual password

            RestTemplate restTemplate = new RestTemplate();

	        // Step 1: Fetch CSRF token and cookies
	        HttpHeaders getHeaders = new HttpHeaders();
	        getHeaders.setBasicAuth(username, password);
	        getHeaders.set("x-csrf-token", "fetch");

	        HttpEntity<Void> csrfRequest = new HttpEntity<>(getHeaders);
	        ResponseEntity<String> csrfResponse = restTemplate.exchange(
	                s4hanaApiUrl, HttpMethod.GET, csrfRequest, String.class);

	        if (csrfResponse.getStatusCode() == HttpStatus.OK) {
	            String csrfToken = csrfResponse.getHeaders().getFirst("x-csrf-token");
	            List<String> cookies = csrfResponse.getHeaders().get(HttpHeaders.SET_COOKIE);

	            // Step 2: Use CSRF token and cookies for POST request
	            HttpHeaders postHeaders = new HttpHeaders();
	            postHeaders.setBasicAuth(username, password);
	            postHeaders.set("x-csrf-token", csrfToken);
	            postHeaders.setContentType(MediaType.APPLICATION_JSON);
	            postHeaders.put(HttpHeaders.COOKIE, cookies);

	            HttpEntity<Map<String, Object>> postRequest = new HttpEntity<>(s4Payload, postHeaders);
	            ResponseEntity<String> postResponse = restTemplate.exchange(
	                    s4hanaApiUrl, HttpMethod.POST, postRequest, String.class);

	            if (postResponse.getStatusCode() == HttpStatus.CREATED) {
	                s4HanaResponse.put("status", "success");
	                s4HanaResponse.put("message", "Customer added to S/4HANA successfully");
	            } else {
	                s4HanaResponse.put("status", "failure");
	                s4HanaResponse.put("message", "Failed to add customer to S/4HANA");
	                s4HanaResponse.put("response", postResponse.getBody());
	            }
	        } else {
	            s4HanaResponse.put("status", "error");
	            s4HanaResponse.put("message", "Failed to fetch x-csrf-token");
	        }
	    } catch (Exception e) {
	        s4HanaResponse.put("status", "error");
	        s4HanaResponse.put("message", "Error while communicating with S/4HANA: " + e.getMessage());
	    }

	    // Final response
	    Map<String, Object> response = new LinkedHashMap<>();
	    response.put("status", "success");
	    response.put("id", cmENT.getCustomerId());
	    response.put("s4HanaResponse", s4HanaResponse); // Adding S/4HANA response
	    return response;
	}

//	public Map<String, Object> addCustomerMaster(CustomerMasterDTO cmDTO) {
//		CustomerMasterEntity cmENT = new CustomerMasterEntity();
//		cmENT.setCustomerName(cmDTO.getCustomerName());
//		cmENT.setContactNo(cmDTO.getContactNo());
//		cmENT.setEmail(cmDTO.getEmail());
//		cmENT.setDeliveryLocation(cmDTO.getDeliveryLocation());
//		cmENT.setBillingAddress(cmDTO.getBillingAddress());
//		cmENT.setShippingAddress1(cmDTO.getShippingAddress1());
//		cmENT.setShippingAddress2(cmDTO.getShippingAddress2());
//		cmENT.setReturnCredit(0);
//						
//		if (cmENT.getContactNo().length() != 10 || !cmENT.getContactNo().matches("\\d+")) {
//	        Map<String, Object> response = new LinkedHashMap<>();
//	        response.put("status", "failed");
//	        response.put("code", "510");
//	        response.put("error", "Please enter a valid 10-digit mobile number");
//	        return response;
//	    }
//		
//		if (!cmENT.getEmail().endsWith("@gmail.com")) {
//	        Map<String, Object> response = new LinkedHashMap<>();
//	        response.put("status", "failed");
//	        response.put("code", "400");
//	        response.put("error", "Email must contain '@gmail.com'");
//	        return response;
//	    }
//		CustomerMasterEntity custEmail = this.cmDAO.findCustomerByEmailBy(cmENT.getEmail());
//		if (custEmail != null) {
//			Map<String, Object> response = new LinkedHashMap<>();
//			response.put("status", "failed");
//			response.put("code", "509");
//			response.put("error", "email already exist");
//			return response;
//		}
//		
//		CustomerMasterEntity custMble = this.cmDAO.findCustomerByPhoneNoBy(cmENT.getContactNo());
//		if (custMble != null) {
//			Map<String, Object> response = new LinkedHashMap<>();
//			response.put("status", "failed");
//			response.put("code", "509");
//			response.put("error", "mobile number already exists");
//			return response;
//		}
//		CustomerMasterEntity custName = this.cmDAO.findCustomerExists(cmDTO.getCustomerName());
//				if(custName != null) {
//				Map<String, Object> response = new LinkedHashMap<>();
//		        response.put("status", "failed");
//		        response.put("code", "508");
//		        response.put("error", "Customer name already exists");
//		        return response;
//			}
//		
//		cmDAO.save(cmENT);
//
//		Map<String, Object> response = new LinkedHashMap<>();
//		response.put("status", "success");
//		response.put("id", cmENT.getCustomerId());
//		return response;
//	}
	

	public List<CustomerMasterEntity> getAllCustomerMaster() {
		List<CustomerMasterEntity> customerList = cmDAO.findAll();
		return customerList;
	}

	public List<CustomerMasterEntity> getAllCustomerMasterById() {
		List<CustomerMasterEntity> customerList = cmDAO.findAll();
		return customerList;
	}

	public ResponseEntity<Object> getAllCustomerData() {
		 RestTemplate restTemplate = new RestTemplate();

		    String url = "https://my403545-api.s4hana.cloud.sap/sap/opu/odata/sap/ZGETBPDATA/ZGETBPDATA?$format=json&$top=500";
		    String username = "INTEGRATION";
		    String password = "gNQH>ydgzvCKfwuxQdcEDQzHMz5YPQfaLxzPHqLz";

		    String auth = username + ":" + password;
		    String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
		    String authHeader = "Basic " + encodedAuth;

		    HttpHeaders headers = new HttpHeaders();
		    headers.set("Authorization", authHeader);

		    HttpEntity<String> request = new HttpEntity<>(headers);

		    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

		    ObjectMapper objectMapper = new ObjectMapper();
		    try {
		        Object jsonResponse = objectMapper.readValue(response.getBody(), Object.class);
		        return ResponseEntity.ok(jsonResponse);
		    } catch (JsonProcessingException e) {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error parsing JSON response");
		    }
	}

	public String getAndSaveAllCustomerData() {
		 
		 RestTemplate restTemplate = new RestTemplate();
		 
	        String url = "https://my403545-api.s4hana.cloud.sap/sap/opu/odata/sap/ZGETBPDATA/ZGETBPDATA?$format=json&$top=500";
	        String username = "INTEGRATION";
	        String password = "gNQH>ydgzvCKfwuxQdcEDQzHMz5YPQfaLxzPHqLz";

	        try {
	            // Create Authorization Header
	            String auth = username + ":" + password;
	            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
	            String authHeader = "Basic " + encodedAuth;

	            HttpHeaders headers = new HttpHeaders();
	            headers.set("Authorization", authHeader);

	            // Send GET Request
	            HttpEntity<String> request = new HttpEntity<>(headers);
	            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

	            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
	                // Parse Response
	                JsonNode rootNode = objectMapper.readTree(response.getBody());
	                JsonNode resultsNode = rootNode.path("d").path("results");

	                // Map to Entity and Save
	                List<SaveAllCustomerEntity> customerEntities = new ArrayList<>();
	                resultsNode.forEach(result -> {
	                	SaveAllCustomerEntity customer = new SaveAllCustomerEntity();
	                 //   product.setProduct(result.path("Product").asText());
	                	customer.setCustomer(result.path("Customer").asText());
	                	customer.setCustomerName(result.path("CustomerName").asText());
	                	customer.setAddressID(result.path("AddressID").asText());
	                	customer.setCityName(result.path("CityName").asText());
	                	customer.setPostalCode(result.path("PostalCode").asText());
	                	customer.setStreetName(result.path("StreetName").asText());
	                	customer.setRegion(result.path("Region").asText());
	                	customer.setTelephoneNumber1(result.path("TelephoneNumber1").asText());
	                	customer.setCountry(result.path("Country").asText());
	                	customer.setDistrictName(result.path("DistrictName").asText());
	                	customer.setEmailAddress(result.path("EmailAddress").asText());
	                	customer.setMobilePhoneNumber(result.path("MobilePhoneNumber").asText());
	                   
	                    
	                    JsonNode metadata = result.path("__metadata");
	                    customer.setMetadataId(metadata.path("id").asText());
	                    customer.setMetadataUri(metadata.path("uri").asText());
	                    customer.setMetadataType(metadata.path("type").asText());
	                    
	                    customerEntities.add(customer);
	                });

	                sacDAO.saveAll(customerEntities);
	                return "Customer data saved successfully!";
	            } else {
	                return "Failed to fetch data: HTTP status " + response.getStatusCode();
	            }
	        } catch (Exception e) {
	            return "Error occurred: " + e.getMessage();
	        }
	    }

	public List<SaveAllCustomerEntity> getAllS4hanaCustomerMaster() {
		List<SaveAllCustomerEntity> customerList = sacDAO.findAll();
		return customerList;
	}

}
