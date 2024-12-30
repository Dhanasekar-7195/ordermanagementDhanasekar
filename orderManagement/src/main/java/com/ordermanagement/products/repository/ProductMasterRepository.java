package com.ordermanagement.products.repository;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordermanagement.customers.entity.SaveAllCustomerEntity;
import com.ordermanagement.products.dao.ProductMasterDAO;
import com.ordermanagement.products.dao.SaveAllProductDAO;
import com.ordermanagement.products.dto.ProductMasterDTO;
import com.ordermanagement.products.entity.ProductMasterEntity;
import com.ordermanagement.products.entity.SaveAllProductEntity;
import com.ordermanagement.sampleexception.ProductNotFoundException;

@Component
public class ProductMasterRepository {

	@Autowired
	private ProductMasterDAO pmDAO;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@Autowired
	private SaveAllProductDAO sapDAO;
	
	private static final String S4HANA_API_URL = "https://my403545-api.s4hana.cloud.sap/sap/opu/odata/sap/ZGETPRDATA_SRV_BND/ZGETPRDATA?$format=json";
    private static final String USERNAME = "INTEGRATION";
    private static final String PASSWORD = "gNQH>ydgzvCKfwuxQdcEDQzHMz5YPQfaLxzPHqLz";

	
//	public Map<String, Object> addProductMaster(ProductMasterDTO productDTO) {
//	    String productName = productDTO.getProductName();
//	    String category = productDTO.getCategory();
//	    String subCategory = productDTO.getSubCategory();
//	    ProductMasterEntity pmENT = pmDAO.findByProductNameAndCategoryAndSubCategory(productName, category, subCategory);
//	    Map<String, Object> response = new LinkedHashMap<>();
//	    Map<String, Object> s4HanaResponse = new LinkedHashMap<>();
//
//	    if (pmENT == null) {
//	        DecimalFormat df = new DecimalFormat("#.######");
//	        ProductMasterEntity prodEnt = new ProductMasterEntity();
//
//	        prodEnt.setProductName(productDTO.getProductName());
//	        prodEnt.setCategory(productDTO.getCategory());
//	        prodEnt.setSubCategory(productDTO.getSubCategory());
//	        prodEnt.setTAX(productDTO.getTAX());
//	        prodEnt.setUnit(productDTO.getUnit());
//	        prodEnt.setPrice(productDTO.getPrice());
//	        prodEnt.setDiscount(productDTO.getDiscount());
//	        prodEnt.setImageId(productDTO.getImageId());
//
//	        int priceValue = prodEnt.getPrice();
//	        double price = Double.valueOf(priceValue);
//	        double priceRound = Double.parseDouble(df.format(price));
//
//	        String discount = prodEnt.getDiscount();
//	        String discountOnly = discount.replace("%", "");
//	        double discountConv = Double.parseDouble(discountOnly);
//	        double discountAmount = price * (discountConv / 100);
//	        double discountAmountRound = Double.parseDouble(df.format(discountAmount));
//
//	        double discountPrice = priceRound - discountAmountRound;
//	        double discountPriceRound = Double.parseDouble(df.format(discountPrice));
//	        String tax = prodEnt.getTAX();
//	        String taxOnly = tax.replace("%", "");
//	        double taxConv = Double.parseDouble(taxOnly);
//	        double taxAmount = discountPriceRound * (taxConv / 100);
//	        double taxAmountRound = Double.parseDouble(df.format(taxAmount));
//
//	        double totalAmount = discountPriceRound + taxAmountRound;
//	        double totalAmountRound = Double.parseDouble(df.format(totalAmount));
//	        prodEnt.setTotalAmount(totalAmountRound);
//
//	        pmDAO.save(prodEnt);
//
//	        // Schedule the S/4HANA API call for 3:20 PM
//	        LocalTime now = LocalTime.now();
//	        LocalTime targetTime = LocalTime.of(15, 35); // 3:20 PM
//	        long delay = Duration.between(now, targetTime).toMillis();
//	        if (delay < 0) {
//	            // If the target time is in the past for today, schedule for tomorrow
//	            delay += Duration.ofDays(1).toMillis();
//	        }
//
//	        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//	        scheduler.schedule(() -> {
//	            // Create the payload for S/4HANA API
//	            Map<String, Object> s4Payload = new HashMap<>();
//	            s4Payload.put("Product", prodEnt.getProductName()); // Product Name
//	            s4Payload.put("ProductType", "SERV"); // Hardcoded as 'SERV'
//	            s4Payload.put("ProductGroup", "P002"); // Add Product Group
//	            s4Payload.put("BaseUnit", "EA"); // Base Unit from DTO
//	            s4Payload.put("IndustrySector", "M"); // Hardcoded as 'M'
//
//	            // Create the _ProductDescription array
//	            List<Map<String, Object>> productDescriptions = new ArrayList<>();
//	            Map<String, Object> productDescription = new HashMap<>();
//	            productDescription.put("Product", prodEnt.getProductName()); // Product Name
//	            productDescription.put("Language", "EN"); // Language
//	            productDescription.put("ProductDescription", prodEnt.getProductName() + "_Description"); // Product Description
//	            productDescriptions.add(productDescription);
//
//	            // Add _ProductDescription to the payload
//	            s4Payload.put("_ProductDescription", productDescriptions);
//
//	            // Fetch x-csrf-token and send data to S/4HANA
//	            try {
//	                String s4hanaApiUrl = "https://my403545-api.s4hana.cloud.sap/sap/opu/odata4/sap/api_product/srvd_a2x/sap/product/0001/Product";
//	                String username = "INTEGRATION"; // Replace with actual username
//	                String password = "gNQH>ydgzvCKfwuxQdcEDQzHMz5YPQfaLxzPHqLz"; // Replace with actual password
//
//	                RestTemplate restTemplate = new RestTemplate();
//
//	                // Step 1: Fetch CSRF token and cookies
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
//	                    // Step 2: Use CSRF token and cookies for POST request
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
//	                        s4HanaResponse.put("status", "success");
//	                        s4HanaResponse.put("message", "Product added to S/4HANA successfully");
//	                    } else {
//	                        s4HanaResponse.put("status", "failure");
//	                        s4HanaResponse.put("message", "Failed to add product to S/4HANA");
//	                        s4HanaResponse.put("response", postResponse.getBody());
//	                    }
//	                } else {
//	                    s4HanaResponse.put("status", "error");
//	                    s4HanaResponse.put("message", "Failed to fetch x-csrf-token");
//	                }
//	            } catch (Exception e) {
//	                s4HanaResponse.put("status", "error");
//	                s4HanaResponse.put("message", "Error while communicating with S/4HANA: " + e.getMessage());
//	            }
//	        }, delay, TimeUnit.MILLISECONDS);
//
//	        // Final response
//	        response.put("localStatus", "success");
//	        response.put("localProductId", prodEnt.getProdId());
//	        response.put("s4HanaResponse", s4HanaResponse);
//	    } else {
//	        response.put("localStatus", "failure");
//	        response.put("message", "Product already exists locally");
//	    }
//
//	    return response;
//	}

	
//	public Map<String, Object> addProductMaster(ProductMasterDTO productDTO) {
//	    String productName = productDTO.getProductName();
//	    String category = productDTO.getCategory();
//	    String subCategory = productDTO.getSubCategory();
//	    ProductMasterEntity pmENT = pmDAO.findByProductNameAndCategoryAndSubCategory(productName, category, subCategory);
//	    Map<String, Object> response = new LinkedHashMap<>();
//	    Map<String, Object> s4HanaResponse = new LinkedHashMap<>();
//
//	    if (pmENT == null) {
//	        DecimalFormat df = new DecimalFormat("#.######");
//	        ProductMasterEntity prodEnt = new ProductMasterEntity();
//
//	        prodEnt.setProductName(productDTO.getProductName());
//	        prodEnt.setCategory(productDTO.getCategory());
//	        prodEnt.setSubCategory(productDTO.getSubCategory());
//	        prodEnt.setTAX(productDTO.getTAX());
//	        prodEnt.setUnit(productDTO.getUnit());
//	        prodEnt.setPrice(productDTO.getPrice());
//	        prodEnt.setDiscount(productDTO.getDiscount());
//	        prodEnt.setImageId(productDTO.getImageId());
//
//	        int priceValue = prodEnt.getPrice();
//	        double price = Double.valueOf(priceValue);
//	        double priceRound = Double.parseDouble(df.format(price));
//
//	        String discount = prodEnt.getDiscount();
//	        String discountOnly = discount.replace("%", "");
//	        double discountConv = Double.parseDouble(discountOnly);
//	        double discountAmount = price * (discountConv / 100);
//	        double discountAmountRound = Double.parseDouble(df.format(discountAmount));
//
//	        double discountPrice = priceRound - discountAmountRound;
//	        double discountPriceRound = Double.parseDouble(df.format(discountPrice));
//	        String tax = prodEnt.getTAX();
//	        String taxOnly = tax.replace("%", "");
//	        double taxConv = Double.parseDouble(taxOnly);
//	        double taxAmount = discountPriceRound * (taxConv / 100);
//	        double taxAmountRound = Double.parseDouble(df.format(taxAmount));
//
//	        double totalAmount = discountPriceRound + taxAmountRound;
//	        double totalAmountRound = Double.parseDouble(df.format(totalAmount));
//	        prodEnt.setTotalAmount(totalAmountRound);
//
//	        pmDAO.save(prodEnt);
//
//	        // Schedule the S/4HANA API call after 30 minutes (1800 seconds)
//	        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//	        scheduler.schedule(() -> {
//	            // Create the payload for S/4HANA API
//	            Map<String, Object> s4Payload = new HashMap<>();
//	            s4Payload.put("Product", prodEnt.getProductName()); // Product Name
//	            s4Payload.put("ProductType", "SERV"); // Hardcoded as 'SERV'
//	            s4Payload.put("ProductGroup", "P002"); // Add Product Group
//	            s4Payload.put("BaseUnit", "EA"); // Base Unit from DTO
//	            s4Payload.put("IndustrySector", "M"); // Hardcoded as 'M'
//
//	            // Create the _ProductDescription array
//	            List<Map<String, Object>> productDescriptions = new ArrayList<>();
//	            Map<String, Object> productDescription = new HashMap<>();
//	            productDescription.put("Product", prodEnt.getProductName()); // Product Name
//	            productDescription.put("Language", "EN"); // Language
//	            productDescription.put("ProductDescription", prodEnt.getProductName() + "_Description"); // Product Description
//	            productDescriptions.add(productDescription);
//
//	            // Add _ProductDescription to the payload
//	            s4Payload.put("_ProductDescription", productDescriptions);
//
//	            // Fetch x-csrf-token and send data to S/4HANA
//	            try {
//	                String s4hanaApiUrl = "https://my403545-api.s4hana.cloud.sap/sap/opu/odata4/sap/api_product/srvd_a2x/sap/product/0001/Product";
//	                String username = "INTEGRATION"; // Replace with actual username
//	                String password = "gNQH>ydgzvCKfwuxQdcEDQzHMz5YPQfaLxzPHqLz"; // Replace with actual password
//
//	                RestTemplate restTemplate = new RestTemplate();
//
//	                // Step 1: Fetch CSRF token and cookies
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
//	                    // Step 2: Use CSRF token and cookies for POST request
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
//	                        s4HanaResponse.put("status", "success");
//	                        s4HanaResponse.put("message", "Product added to S/4HANA successfully");
//	                    } else {
//	                        s4HanaResponse.put("status", "failure");
//	                        s4HanaResponse.put("message", "Failed to add product to S/4HANA");
//	                        s4HanaResponse.put("response", postResponse.getBody());
//	                    }
//	                } else {
//	                    s4HanaResponse.put("status", "error");
//	                    s4HanaResponse.put("message", "Failed to fetch x-csrf-token");
//	                }
//	            } catch (Exception e) {
//	                s4HanaResponse.put("status", "error");
//	                s4HanaResponse.put("message", "Error while communicating with S/4HANA: " + e.getMessage());
//	            }
//	        }, 10, TimeUnit.MINUTES); // Delay of 30 minutes
//
//	        // Final response
//	        response.put("localStatus", "success");
//	        response.put("localProductId", prodEnt.getProdId());
//	        response.put("s4HanaResponse", s4HanaResponse);
//	    } else {
//	        response.put("localStatus", "failure");
//	        response.put("message", "Product already exists locally");
//	    }
//
//	    return response;
//	}
	
	public Map<String, Object> addProductMaster(ProductMasterDTO productDTO) {
	    String productName = productDTO.getProductName();
	    String category = productDTO.getCategory();
	    String subCategory = productDTO.getSubCategory();
	    ProductMasterEntity pmENT = pmDAO.findByProductNameAndCategoryAndSubCategory(productName, category, subCategory);
	    Map<String, Object> response = new LinkedHashMap<>();
	    Map<String, Object> s4HanaResponse = new LinkedHashMap<>();

	    if (pmENT == null) {
	        DecimalFormat df = new DecimalFormat("#.######");
	        ProductMasterEntity prodEnt = new ProductMasterEntity();

	        prodEnt.setProductName(productDTO.getProductName());
	        prodEnt.setCategory(productDTO.getCategory());
	        prodEnt.setSubCategory(productDTO.getSubCategory());
	        prodEnt.setTAX(productDTO.getTAX());
	        prodEnt.setUnit(productDTO.getUnit());
	        prodEnt.setPrice(productDTO.getPrice());
	        prodEnt.setDiscount(productDTO.getDiscount());
	        prodEnt.setImageId(productDTO.getImageId());

	        int priceValue = prodEnt.getPrice();
	        double price = Double.valueOf(priceValue);
	        double priceRound = Double.parseDouble(df.format(price));

	        String discount = prodEnt.getDiscount();
	        String discountOnly = discount.replace("%", "");
	        double discountConv = Double.parseDouble(discountOnly);
	        double discountAmount = price * (discountConv / 100);
	        double discountAmountRound = Double.parseDouble(df.format(discountAmount));

	        double discountPrice = priceRound - discountAmountRound;
	        double discountPriceRound = Double.parseDouble(df.format(discountPrice));
	        String tax = prodEnt.getTAX();
	        String taxOnly = tax.replace("%", "");
	        double taxConv = Double.parseDouble(taxOnly);
	        double taxAmount = discountPriceRound * (taxConv / 100);
	        double taxAmountRound = Double.parseDouble(df.format(taxAmount));

	        double totalAmount = discountPriceRound + taxAmountRound;
	        double totalAmountRound = Double.parseDouble(df.format(totalAmount));
	        prodEnt.setTotalAmount(totalAmountRound);

	        pmDAO.save(prodEnt);
	        
	        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	        scheduler.schedule(() -> {

	        Map<String, Object> s4Payload = new HashMap<>();
	        s4Payload.put("Product", prodEnt.getProdId()); 
	        s4Payload.put("ProductType", "SERV"); 
	        s4Payload.put("ProductGroup", "P001"); 
	        s4Payload.put("BaseUnit", "EA"); 
	        s4Payload.put("IndustrySector", "M"); 

	        List<Map<String, Object>> productDescriptions = new ArrayList<>();
	        Map<String, Object> productDescription = new HashMap<>();
	        productDescription.put("Product", prodEnt.getProdId()); 
	        productDescription.put("Language", "EN");
	        productDescription.put("ProductDescription", prodEnt.getProductName());
	        productDescriptions.add(productDescription);

	        s4Payload.put("_ProductDescription", productDescriptions);


	        try {
	            String s4hanaApiUrl = "https://my403545-api.s4hana.cloud.sap/sap/opu/odata4/sap/api_product/srvd_a2x/sap/product/0001/Product";
	            String username = "INTEGRATION"; 
	            String password = "gNQH>ydgzvCKfwuxQdcEDQzHMz5YPQfaLxzPHqLz"; 

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
	                    s4HanaResponse.put("status", "success");
	                    s4HanaResponse.put("message", "Product added to S/4HANA successfully");
	                } else {
	                    s4HanaResponse.put("status", "failure");
	                    s4HanaResponse.put("message", "Failed to add product to S/4HANA");
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
	        }, 1, TimeUnit.MINUTES);

	        response.put("status", "success");
	        response.put("id", prodEnt.getProdId());
	        response.put("s4HanaResponse", s4HanaResponse);
	    } else {
	        response.put("status", "failure");
	        response.put("message", "Product already exists locally");
	    }

	    return response;
	}

	
//	public Map<String, Object> addProductMaster(ProductMasterDTO productDTO) {
//	    String productName = productDTO.getProductName();
//	    String category = productDTO.getCategory();
//	    String subCategory = productDTO.getSubCategory();
//	    ProductMasterEntity pmENT = pmDAO.findByProductNameAndCategoryAndSubCategory(productName, category, subCategory);
//	    Map<String, Object> response = new LinkedHashMap<>();
//	    Map<String, Object> s4HanaResponse = new LinkedHashMap<>();
//
//	    if (pmENT == null) {
//	        DecimalFormat df = new DecimalFormat("#.######");
//	        ProductMasterEntity prodEnt = new ProductMasterEntity();
//
//	        prodEnt.setProductName(productDTO.getProductName());
//	        prodEnt.setCategory(productDTO.getCategory());
//	        prodEnt.setSubCategory(productDTO.getSubCategory());
//	        prodEnt.setTAX(productDTO.getTAX());
//	        prodEnt.setUnit(productDTO.getUnit());
//	        prodEnt.setPrice(productDTO.getPrice());
//	        prodEnt.setDiscount(productDTO.getDiscount());
//	        prodEnt.setImageId(productDTO.getImageId());
//
//	        int priceValue = prodEnt.getPrice();
//	        double price = Double.valueOf(priceValue);
//	        double priceRound = Double.parseDouble(df.format(price));
//
//	        String discount = prodEnt.getDiscount();
//	        String discountOnly = discount.replace("%", "");
//	        double discountConv = Double.parseDouble(discountOnly);
//	        double discountAmount = price * (discountConv / 100);
//	        double discountAmountRound = Double.parseDouble(df.format(discountAmount));
//
//	        double discountPrice = priceRound - discountAmountRound;
//	        double discountPriceRound = Double.parseDouble(df.format(discountPrice));
//	        String tax = prodEnt.getTAX();
//	        String taxOnly = tax.replace("%", "");
//	        double taxConv = Double.parseDouble(taxOnly);
//	        double taxAmount = discountPriceRound * (taxConv / 100);
//	        double taxAmountRound = Double.parseDouble(df.format(taxAmount));
//
//	        double totalAmount = discountPriceRound + taxAmountRound;
//	        double totalAmountRound = Double.parseDouble(df.format(totalAmount));
//	        prodEnt.setTotalAmount(totalAmountRound);
//
//	        pmDAO.save(prodEnt);
//
//	        // Prepare the payload for S/4HANA API using common fields
//	        Map<String, Object> s4Payload = new HashMap<>();
//	        s4Payload.put("Product", prodEnt.getProductName());
//	        s4Payload.put("BaseUnit", productDTO.getUnit()); // Common field
//	        s4Payload.put("ProductType", "SERV"); // Static field
//	        s4Payload.put("IndustrySector", "M"); // Static field
//
//	        // Add nested _ProductDescription if applicable
//	        List<Map<String, Object>> productDescriptions = new ArrayList<>();
//	        Map<String, Object> productDescription = new HashMap<>();
//	        productDescription.put("Product", prodEnt.getProductName());
//	        productDescription.put("Language", "EN");
//	        productDescription.put("ProductDescription", prodEnt.getProductName() + "_Description");
//	        productDescriptions.add(productDescription);
//
//	        s4Payload.put("_ProductDescription", productDescriptions);
//
//	        // Fetch x-csrf-token and send data to S/4HANA
//	        try {
//	            String s4hanaApiUrl = "https://my403545-api.s4hana.cloud.sap/sap/opu/odata4/sap/api_product/srvd_a2x/sap/product/0001/Product";
//	            String username = "INTEGRATION"; // Replace with actual username
//	            String password = "gNQH>ydgzvCKfwuxQdcEDQzHMz5YPQfaLxzPHqLz"; // Replace with actual password
//
//	            RestTemplate restTemplate = new RestTemplate();
//	            HttpHeaders headers = new HttpHeaders();
//	            headers.setBasicAuth(username, password);
//	            headers.set("x-csrf-token", "fetch"); // Request token
//
//	            // GET request to fetch x-csrf-token
//	            HttpEntity<Void> csrfEntity = new HttpEntity<>(headers);
//	            ResponseEntity<String> csrfResponse = restTemplate.exchange(s4hanaApiUrl, HttpMethod.GET, csrfEntity, String.class);
//
//	            if (csrfResponse.getStatusCode() == HttpStatus.OK) {
//	                String csrfToken = csrfResponse.getHeaders().getFirst("x-csrf-token");
//
//	                // POST request to send product data
//	                headers.set("x-csrf-token", csrfToken);
//	                headers.setContentType(MediaType.APPLICATION_JSON);
//
//	                HttpEntity<Map<String, Object>> postEntity = new HttpEntity<>(s4Payload, headers);
//	                ResponseEntity<String> s4Response = restTemplate.exchange(s4hanaApiUrl, HttpMethod.POST, postEntity, String.class);
//
//	                if (s4Response.getStatusCode() == HttpStatus.CREATED) {
//	                    s4HanaResponse.put("status", "success");
//	                    s4HanaResponse.put("message", "Product added to S/4HANA successfully");
//	                } else {
//	                    s4HanaResponse.put("status", "failure");
//	                    s4HanaResponse.put("message", "Failed to add product to S/4HANA");
//	                    s4HanaResponse.put("response", s4Response.getBody());
//	                }
//	            } else {
//	                s4HanaResponse.put("status", "error");
//	                s4HanaResponse.put("message", "Failed to fetch x-csrf-token");
//	            }
//	        } catch (Exception e) {
//	            s4HanaResponse.put("status", "error");
//	            s4HanaResponse.put("message", "Error while communicating with S/4HANA: " + e.getMessage());
//	        }
//
//	        // Final response
//	        response.put("localStatus", "success");
//	        response.put("localProductId", prodEnt.getProdId());
//	        response.put("s4HanaResponse", s4HanaResponse);
//	    } else {
//	        response.put("localStatus", "failure");
//	        response.put("message", "Product already exists locally");
//	    }
//
//	    return response;
//	}


//	public Map<String, Object> addProductMaster(ProductMasterDTO productDTO) {
//		String productName = productDTO.getProductName();
//		String Category = productDTO.getCategory();
//		String subCategory = productDTO.getSubCategory();
//		ProductMasterEntity pmENT = pmDAO.findByProductNameAndCategoryAndSubCategory(productName, Category,
//				subCategory);
//		Map<String, Object> response = new LinkedHashMap<>();
//
//		if (pmENT == null) {
//			DecimalFormat df = new DecimalFormat("#.######");
//			ProductMasterEntity prodEnt = new ProductMasterEntity();
//
//			prodEnt.setProductName(productDTO.getProductName());
//			prodEnt.setCategory(productDTO.getCategory());
//			prodEnt.setSubCategory(productDTO.getSubCategory());
//			prodEnt.setTAX(productDTO.getTAX());
//			prodEnt.setUnit(productDTO.getUnit());
//			prodEnt.setPrice(productDTO.getPrice());
//			prodEnt.setDiscount(productDTO.getDiscount());
//			prodEnt.setImageId(productDTO.getImageId());
//
//			int priceValue = prodEnt.getPrice();
//			double price = Double.valueOf(priceValue);
//			double priceRound = Double.parseDouble(df.format(price));
//
//			String discount = prodEnt.getDiscount();
//			String discountOnly = discount.replace("%", "");
//			double discountConv = Double.parseDouble(discountOnly);
//			double discountAmount = price * (discountConv / 100);
//			double discountAmountRound = Double.parseDouble(df.format(discountAmount));
//
//			double discountPrice = priceRound - discountAmountRound;
//			double discountPriceRound = Double.parseDouble(df.format(discountPrice));
//			String tax = prodEnt.getTAX();
//			String taxOnly = tax.replace("%", "");
//			double taxConv = Double.parseDouble(taxOnly);
//			double taxAmount = discountPriceRound * (taxConv / 100);
//			double taxAmountRound = Double.parseDouble(df.format(taxAmount));
//
//			double totalAmount = discountPriceRound + taxAmountRound;
//			double totalAmountRound = Double.parseDouble(df.format(totalAmount));
//			prodEnt.setTotalAmount(totalAmountRound);
//
//			pmDAO.save(prodEnt);
//
//			response.put("status", "success");
//			response.put("id", prodEnt.getProdId());
//		} else {
//			response.put("status", "product already exists");
//		}
//
//		return response;
//	}

	public Map<String, Object> updateProductMaster(ProductMasterDTO productDTO) {
		String productId=productDTO.getProdId();
		String productName = productDTO.getProductName();
				String Category = productDTO.getCategory();
				String subCategory = productDTO.getSubCategory();
				ProductMasterEntity pmENT = pmDAO.findByProductNameAndCategoryAndSubCategoryExcludeProdId(productId, productName, Category,
						subCategory);
				Map<String, Object> response = new LinkedHashMap<>();

				if (pmENT == null) {
					DecimalFormat df = new DecimalFormat("#.######");
				ProductMasterEntity prodEnt = new ProductMasterEntity();
				prodEnt.setProdId(productDTO.getProdId());
				prodEnt.setProductName(productDTO.getProductName());
				prodEnt.setCategory(productDTO.getCategory());
				prodEnt.setSubCategory(productDTO.getSubCategory());
				prodEnt.setTAX(productDTO.getTAX());
				prodEnt.setUnit(productDTO.getUnit());
				prodEnt.setPrice(productDTO.getPrice());
				prodEnt.setDiscount(productDTO.getDiscount());
				prodEnt.setImageId(productDTO.getImageId());

				int priceValue = prodEnt.getPrice();
				double price = Double.valueOf(priceValue);
			
			double priceRound = Double.parseDouble(df.format(price)); 
				String discount = prodEnt.getDiscount();
				String discountOnly = discount.replace("%", "");
				double discountConv = Double.parseDouble(discountOnly);
				double discountAmount = price * (discountConv / 100);
		double discountAmountRound = Double.parseDouble(df.format(discountAmount));

					double discountPrice = priceRound - discountAmountRound;
					double discountPriceRound = Double.parseDouble(df.format(discountPrice));
					String tax = prodEnt.getTAX();
					String taxOnly = tax.replace("%", "");
					double taxConv = Double.parseDouble(taxOnly);
					double taxAmount = discountPriceRound * (taxConv / 100);
					double taxAmountRound = Double.parseDouble(df.format(taxAmount));

				double totalAmount = discountPriceRound + taxAmountRound;
					double totalAmountRound = Double.parseDouble(df.format(totalAmount));
					prodEnt.setTotalAmount(totalAmountRound);

				pmDAO.save(prodEnt);

				response.put("status", "success");
				response.put("id", prodEnt.getProdId()); // If needed, return the database ID of the updated product
				} else {
				response.put("status", "product already exists");
				}
				return response;

			}

	public ProductMasterDTO getProductMasterById(String prodId) throws ProductNotFoundException {
		ProductMasterEntity pmENT = pmDAO.getPrDById(prodId);
		ProductMasterDTO pmDTO = new ProductMasterDTO();
		if (pmENT != null) {
			pmDTO.setProdId(pmENT.getProdId());
			pmDTO.setProductName(pmENT.getProductName());
			pmDTO.setCategory(pmENT.getCategory());
			pmDTO.setSubCategory(pmENT.getSubCategory());
			pmDTO.setTAX(pmENT.getTAX());
			pmDTO.setUnit(pmENT.getUnit());
			pmDTO.setPrice(pmENT.getPrice());
			pmDTO.setDiscount(pmENT.getDiscount());
			pmDTO.setImageId(pmENT.getImageId());
			pmDTO.setTotalAmount(pmENT.getTotalAmount());

			return pmDTO;
		} else {
			throw new ProductNotFoundException("product not found");
		}
	}

	public List<ProductMasterEntity> getAllProductDetails() {
		List<ProductMasterEntity> vmList = pmDAO.findAll();
		return vmList;
	}

	public Map<String, Object> deleteProductMasterById(String prodId) {
		if (pmDAO.getPrDById(prodId) != null) {
			pmDAO.deleteById(prodId);
			Map<String, Object> response = new LinkedHashMap<>();
			response.put("status", "success");
			return response;
		}
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("status", "204");
		response.put("id", "not_found");
		return response;
	}

	public ProductMasterDTO getProductMasterByProductName(String productName) throws ProductNotFoundException {
		ProductMasterEntity pmENT = pmDAO.getPrDByProductName(productName);
		ProductMasterDTO pmDTO = new ProductMasterDTO();
		if (pmENT != null) {
			pmDTO.setProdId(pmENT.getProdId());
			pmDTO.setProductName(pmENT.getProductName());
			pmDTO.setCategory(pmENT.getCategory());
			pmDTO.setSubCategory(pmENT.getSubCategory());
			pmDTO.setTAX(pmENT.getTAX());
			pmDTO.setUnit(pmENT.getUnit());
			pmDTO.setPrice(pmENT.getPrice());
			pmDTO.setDiscount(pmENT.getDiscount());
			pmDTO.setImageId(pmENT.getImageId());
			pmDTO.setTotalAmount(pmENT.getTotalAmount());

			return pmDTO;
		} else {
			throw new ProductNotFoundException("product not found");
		}
	}

	public List<ProductMasterEntity> searchByProductName(String productName) {
		List<ProductMasterEntity> vList = pmDAO.getAllProductById(productName);
		return vList;
	}
	
	public ResponseEntity<Object> getAllProductData() {
		 RestTemplate restTemplate = new RestTemplate();

		    String url = "https://my403545-api.s4hana.cloud.sap/sap/opu/odata/sap/YY1_GETPRODUCTDATA_CDS/YY1_GetProductData?$format=json";
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
	
	 public String getAndSaveAllProductData() {
		 
		 RestTemplate restTemplate = new RestTemplate();
		 
	        String url = "https://my403545-api.s4hana.cloud.sap/sap/opu/odata/sap/YY1_GETPRODUCTDATA_CDS/YY1_GetProductData?$format=json";
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
	                List<SaveAllProductEntity> productEntities = new ArrayList<>();
	                resultsNode.forEach(result -> {
	                	SaveAllProductEntity product = new SaveAllProductEntity();
	                    product.setProduct(result.path("Product").asText());
	                    product.setCategoryName(result.path("CategoryName").asText());
	                    product.setProductType(result.path("ProductType").asText());
	                    product.setBaseUnit(result.path("BaseUnit").asText());
	                    product.setProductDescription(result.path("ProductDescription").asText());
	                    product.setStandardPrice(result.path("StandardPrice").asDouble(0.0));
	                    product.setCurrency(result.path("Currency").asText());
	                    product.setProductSalesOrg(result.path("ProductSalesOrg").asText());
	                    product.setProductDistributionChnl(result.path("ProductDistributionChnl").asText());
	                    product.setDivision(result.path("Division").asText());
	                    
	                    JsonNode metadata = result.path("__metadata");
	                    product.setMetadataId(metadata.path("id").asText());
	                    product.setMetadataUri(metadata.path("uri").asText());
	                    product.setMetadataType(metadata.path("type").asText());
	                    
	                    productEntities.add(product);
	                });

	                sapDAO.saveAll(productEntities);
	                return "Product data saved successfully!";
	            } else {
	                return "Failed to fetch data: HTTP status " + response.getStatusCode();
	            }
	        } catch (Exception e) {
	            return "Error occurred: " + e.getMessage();
	        }
	    }


	 public List<SaveAllProductEntity> getAllS4hanaProductMaster() {
			List<SaveAllProductEntity> productList = sapDAO.findAll();
			return productList;
		}
	}
	



