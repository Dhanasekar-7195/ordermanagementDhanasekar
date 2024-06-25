
package com.ordermanagement.orders.repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ordermanagement.orders.dao.OrderMasterDAO;
import com.ordermanagement.orders.dao.OrderMasterItemDAO;
import com.ordermanagement.orders.dto.OrderMasterDTO;
import com.ordermanagement.orders.dto.OrderMasterItemDTO;
import com.ordermanagement.orders.entity.OrderMasterEntity;
import com.ordermanagement.orders.entity.OrderMasterItemEntity;
import com.ordermanagement.products.entity.ProductMasterEntity;


@Component
public class OrderMasterRepository {
	
	@Autowired
	private OrderMasterDAO omDAO;
	
	@Autowired
	private OrderMasterItemDAO omiDAO;
	
	public  Map<String, Object>  AddOrderMaster(OrderMasterDTO omDTO) {
		OrderMasterEntity omENT= new OrderMasterEntity();
		omENT.setOrderId(omDTO.getOrderId());
		omENT.setOrderDate(omDTO.getOrderDate());
		omENT.setDeliveryLocation(omDTO.getDeliveryLocation());
		omENT.setDeliveryAddress(omDTO.getDeliveryAddress());
		omENT.setContactPerson(omDTO.getContactPerson());
		omENT.setContactNumber(omDTO.getContactNumber());
		omENT.setComments(omDTO.getComments());
		omENT.setTotal(omDTO.getTotal());
		omDAO.save(omENT);
		
	       for (OrderMasterItemDTO itemDto :omDTO.getItems()) {
	    	   OrderMasterItemEntity item = new OrderMasterItemEntity();
	    	   item.setOrderMasterItemId(itemDto.getOrderMasterItemId());
	    	   item.setProductName(itemDto.getProductName());
	    	   item.setCategory(itemDto.getCategory());
	    	   item.setSubCategory(itemDto.getSubCategory());
	    	   item.setPrice(itemDto.getPrice());
	    	   item.setQty(itemDto.getQty());
	    	   item.setTotalAmount(itemDto.getTotalAmount());
	    	   item.setOrderId(omENT.getOrderId());
	    	   omiDAO.save(item);
	       }

			Map<String, Object> response = new LinkedHashMap<>();
	        response.put("status","success");
	        response.put("id", omENT.getOrderId());
	        return response;
        }
	
	public Map<String, Object> UpdateOrderMaster(OrderMasterDTO omDTO) {
		OrderMasterEntity omENT= new OrderMasterEntity();
		omENT.setOrderId(omDTO.getOrderId());
		omENT.setOrderDate(omDTO.getOrderDate());
		omENT.setDeliveryLocation(omDTO.getDeliveryLocation());
		omENT.setDeliveryAddress(omDTO.getDeliveryAddress());
		omENT.setContactPerson(omDTO.getContactPerson());
		omENT.setContactNumber(omDTO.getContactNumber());
		omENT.setComments(omDTO.getComments());
		omENT.setTotal(omDTO.getTotal());
		omDAO.save(omENT);
	       for (OrderMasterItemDTO itemDto :omDTO.getItems()) {
	    	   OrderMasterItemEntity item = new OrderMasterItemEntity();
	    	   item.setOrderMasterItemId(itemDto.getOrderMasterItemId());
	    	   item.setProductName(itemDto.getProductName());
	    	   item.setCategory(itemDto.getCategory());
	    	   item.setSubCategory(itemDto.getSubCategory());
	    	   item.setPrice(itemDto.getPrice());
	    	   item.setQty(itemDto.getQty());
	    	   item.setTotalAmount(itemDto.getTotalAmount());
	    	   item.setOrderId(omENT.getOrderId());
	            omiDAO.save(item);
	       }
	       omDAO.save(omENT);
		Map<String, Object> response = new LinkedHashMap<>();
        response.put("status","success");
        response.put("id", omENT.getOrderId());
        return response;
        }
	
	@Transactional
    public Map<String, Object> AddUpdateDeleteOrderMaster(OrderMasterDTO omDTO) {
        // Find the order master entity by orderId
        OrderMasterEntity omENT = omDAO.findById(omDTO.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        // Update order master fields
        omENT.setOrderDate(omDTO.getOrderDate());
        omENT.setDeliveryLocation(omDTO.getDeliveryLocation());
        omENT.setDeliveryAddress(omDTO.getDeliveryAddress());
        omENT.setContactPerson(omDTO.getContactPerson());
        omENT.setContactNumber(omDTO.getContactNumber());
        omENT.setComments(omDTO.getComments());
        omENT.setTotal(omDTO.getTotal());
        omDAO.save(omENT);

        // Fetch existing items
        List<OrderMasterItemEntity> existingItems = omiDAO.findByOrderId(omENT.getOrderId());
        Map<String, OrderMasterItemEntity> existingItemsMap = new HashMap<>();
        for (OrderMasterItemEntity item : existingItems) {
            existingItemsMap.put(item.getOrderMasterItemId(), item);
        }

        // Update or add new items
        for (OrderMasterItemDTO itemDto : omDTO.getItems()) {
            OrderMasterItemEntity item;
            if (existingItemsMap.containsKey(itemDto.getOrderMasterItemId())) {
                // Update existing item
                item = existingItemsMap.get(itemDto.getOrderMasterItemId());
            } else {
                // Add new item
                item = new OrderMasterItemEntity();
                item.setOrderMasterItemId(itemDto.getOrderMasterItemId());
                item.setOrderId(omENT.getOrderId());
            }
            item.setProductName(itemDto.getProductName());
            item.setCategory(itemDto.getCategory());
            item.setSubCategory(itemDto.getSubCategory());
            item.setPrice(itemDto.getPrice());
            item.setQty(itemDto.getQty());
            item.setTotalAmount(itemDto.getTotalAmount());
            omiDAO.save(item);
            // Remove the processed item from the map
            existingItemsMap.remove(itemDto.getOrderMasterItemId());
        }

        // Delete items that are no longer in the order
        for (OrderMasterItemEntity existingItem : existingItemsMap.values()) {
            omiDAO.delete(existingItem);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("id", omENT.getOrderId());
        return response;
    }
	
	public List<OrderMasterEntity> SearchByProductName(String orderId) {
		List<OrderMasterEntity> vList= omDAO.getAllOrderById(orderId);
		return vList;
	}
	
	public List<OrderMasterEntity> GetAllOrderMaster() {
		List<OrderMasterEntity> orderList= omDAO.findAll();
		return orderList;
	}

}
