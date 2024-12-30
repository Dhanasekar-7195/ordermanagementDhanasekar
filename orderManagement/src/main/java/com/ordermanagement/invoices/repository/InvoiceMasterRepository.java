package com.ordermanagement.invoices.repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ordermanagement.invoices.dao.InvoiceMasterDAO;
import com.ordermanagement.invoices.dao.InvoiceMasterItemDAO;
import com.ordermanagement.invoices.dto.InvoiceMasterDTO;
import com.ordermanagement.invoices.dto.InvoiceMasterItemDTO;
import com.ordermanagement.invoices.entity.InvoiceMasterEntity;
import com.ordermanagement.invoices.entity.InvoiceMasterItemEntity;
import com.ordermanagement.orders.dao.OrderMasterDAO;
import com.ordermanagement.orders.entity.OrderMasterEntity;

@Component
public class InvoiceMasterRepository {
	
	@Autowired
	private InvoiceMasterDAO imDAO;
	
	@Autowired
	private OrderMasterDAO omDAO;
	
	@Autowired
	private InvoiceMasterItemDAO imiDAO;

	public List<InvoiceMasterEntity> getAllInvoiceMaster() {
		List<InvoiceMasterEntity> invoiceList = imDAO.findAll();
		return invoiceList;
	}
	
	public Map<String, Object> addInvoiceMaster(InvoiceMasterDTO imDTO) {
		InvoiceMasterEntity imENT = new InvoiceMasterEntity();
		imENT.setInvoiceId(imDTO.getInvoiceId());
		imENT.setOrderId(imDTO.getOrderId());
		imENT.setInvoiceNo(imDTO.getInvoiceNo());
		imENT.setDeliveryLocation(imDTO.getDeliveryLocation());
		imENT.setDeliveryAddress(imDTO.getDeliveryAddress());
		imENT.setContactPerson(imDTO.getContactPerson());
		imENT.setContactNumber(imDTO.getContactNumber());
		imENT.setComments(imDTO.getComments());
		imENT.setTotal(imDTO.getTotal());
		imENT.setStatus(imDTO.getStatus());
		imENT.setDeliveryId(imDTO.getDeliveryId());
		imENT.setCustomerId(imDTO.getCustomerId());
		imENT.setDeliveredDate(imDTO.getDeliveredDate());

				
		imENT.setPaymentId("-");
		imENT.setPayableAmount(0);
		imENT.setPaidAmount(0);
		imENT.setPaymentDate("-");
		imENT.setPaymentMode("-");
		imENT.setPaymentStatus("-");
		OrderMasterEntity orderMasterEntity = omDAO.getOrderById(imDTO.getOrderId());
		imENT.setOrderDate(orderMasterEntity.getOrderDate());

		if(imENT.getStatus().equals("-")) {
			String draftId = (imDTO.getOrderId()).replace("ORD_", "DRFT_");
			imENT.setDraftId(draftId);
		}
		
		imDAO.save(imENT);
		
		for (InvoiceMasterItemDTO itemDto : imDTO.getItems()) {
			InvoiceMasterItemEntity item = new InvoiceMasterItemEntity();
			item.setInvoiceMasterItemId(itemDto.getInvoiceMasterItemId());
			item.setInvoiceId(imENT.getInvoiceId());
			item.setProductName(itemDto.getProductName());
			item.setCategory(itemDto.getCategory());
			item.setSubCategory(itemDto.getSubCategory());
			item.setPrice(itemDto.getPrice());
			item.setQty(itemDto.getQty());
			item.setTotalAmount(itemDto.getTotalAmount());
			item.setOrderId(imENT.getOrderId());
			
			item.setTax(itemDto.getTax());
			item.setDiscount(itemDto.getDiscount());
			item.setActualAmount(itemDto.getActualAmount());
			item.setOrderMasterItemId(itemDto.getOrderMasterItemId());
			imiDAO.save(item);
		}
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("status", "success");
		response.put("id", imENT.getInvoiceId());
		return response;
			
		}

		
	

}
