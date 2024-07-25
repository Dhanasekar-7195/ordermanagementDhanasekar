package com.ordermanagement.returns.repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ordermanagement.returns.dao.ReturnMasterDAO;
import com.ordermanagement.returns.dao.ReturnMasterItemDAO;
import com.ordermanagement.returns.dto.ReturnMasterDTO;
import com.ordermanagement.returns.dto.ReturnMasterItemDTO;
import com.ordermanagement.returns.entity.ReturnMasterEntity;
import com.ordermanagement.returns.entity.ReturnMasterItemEntity;

	@Component
	public class ReturnMasterRepository {
		
		@Autowired
		private ReturnMasterDAO rmDAO;
		
		@Autowired
		private ReturnMasterItemDAO rmiDAO;
		
		public Map<String, Object> AddReturnMaster(ReturnMasterDTO rmDTO) {
			ReturnMasterEntity rmENT = new ReturnMasterEntity();
			rmENT.setReturnId(rmDTO.getReturnId());
			rmENT.setInvoiceNumber(rmDTO.getInvoiceNumber());
			rmENT.setReason(rmDTO.getReason());
			rmENT.setContactPerson(rmDTO.getContactPerson());
			rmENT.setEmail(rmDTO.getEmail());
			rmENT.setTotalCredit(rmDTO.getTotalCredit());
			rmENT.setNotes(rmDTO.getNotes());
			rmDAO.save(rmENT);
			
			for(ReturnMasterItemDTO itemDTO : rmDTO.getItems()) {
				ReturnMasterItemEntity item = new ReturnMasterItemEntity();
				item.setReturnMasterItemId(itemDTO.getReturnMasterItemId());
				item.setProductName(itemDTO.getProductName());
				item.setCategory(itemDTO.getCategory());
				item.setSubCategory(itemDTO.getSubCategory());
				item.setPrice(itemDTO.getPrice());
				item.setQty(itemDTO.getQty());
				item.setReturnQty(itemDTO.getReturnQty());
				item.setInvoiceAmount(itemDTO.getInvoiceAmount());
				item.setCreditRequest(itemDTO.getCreditRequest());
				item.setImageId(itemDTO.getImageId());
				item.setReturnId(rmENT.getReturnId());
				rmiDAO.save(item);
			}
			
			Map<String, Object> response = new LinkedHashMap<>();
			response.put("status", "success");
			response.put("id", rmENT.getReturnId());
			return response;
		}
		
		public List<ReturnMasterEntity> GetAllReturnMaster() {
			List<ReturnMasterEntity> returnList = rmDAO.findAll();
			return returnList;
		}
	
	}
