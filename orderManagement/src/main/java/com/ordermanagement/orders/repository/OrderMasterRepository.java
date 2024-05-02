package com.ordermanagement.orders.repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
		omENT.setInvoiceNo(omDTO.getInvoiceNo());
		omENT.setSupplier(omDTO.getSupplier());
		omENT.setType(omDTO.getType());
		omENT.setGlGroup(omDTO.getGlGroup());
		omENT.setOrderDate(omDTO.getOrderDate());
		omENT.setName(omDTO.getName());
		omENT.setAddressLine1(omDTO.getAddressLine1());
		omENT.setAddressLine2(omDTO.getAddressLine2());
		omENT.setPhone(omDTO.getPhone());
		omENT.setFax(omDTO.getFax());
		omENT.setTel(omDTO.getTel());
		omENT.setComments(omDTO.getComments());
		omENT.setCreditLimit(omDTO.getCreditLimit());
		omENT.setOutstanding(omDTO.getOutstanding());
		omENT.setAvailable(omDTO.getAvailable());
		omENT.setAmount(omDTO.getAmount());
		omENT.setTax(omDTO.getTax());
		omENT.setTotalAmount(omDTO.getTotalAmount());
		omENT.setNotes(omDTO.getNotes());
		omDAO.save(omENT);
		
	       for (OrderMasterItemDTO itemDto :omDTO.getItems()) {
	    	   OrderMasterItemEntity item = new OrderMasterItemEntity();
	    	   item.setOrderMasterItemId(itemDto.getOrderMasterItemId());
	    	   item.setSNo(itemDto.getSNo());
	    	   item.setDescription(itemDto.getDescription());
	    	   item.setPart(itemDto.getPart());
	    	   item.setUom(itemDto.getUom());
	    	   item.setQty(itemDto.getQty());
	    	   item.setRate(itemDto.getRate());
	    	   item.setAmount(itemDto.getAmount());
	    	   item.setOrderId(omENT.getOrderId());
	    	   omiDAO.save(item);
	       }

			Map<String, Object> response = new LinkedHashMap<>();
	        response.put("status","success");
	        response.put("id", omENT.getOrderId());
	        return response;
        }

	public List<OrderMasterEntity> SearchByProductName(String orderId) {
		List<OrderMasterEntity> vList= omDAO.getAllOrderById(orderId);
		return vList;
	}

}
