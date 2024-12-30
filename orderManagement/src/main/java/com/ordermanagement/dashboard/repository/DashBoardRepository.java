package com.ordermanagement.dashboard.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ordermanagement.dashboard.dao.DashBoardDAO;
import com.ordermanagement.dashboard.entity.DashBoardEntity;
import com.ordermanagement.payments.dao.PaymentMasterDAO;
import com.ordermanagement.payments.entity.PaymentMasterEntity;

@Component
public class DashBoardRepository {

	@Autowired
	private DashBoardDAO dbDAO;
	
	@Autowired
	private PaymentMasterDAO pmDAO;

	public List<DashBoardEntity> getAllDashBoardDetails() {
		List<DashBoardEntity> dbList = dbDAO.findAll();
		return dbList;
	}

	public Map<String, Double> getDashBoardCounts() {
	    List<DashBoardEntity> getDashBoard = dbDAO.findAll();
	    List<PaymentMasterEntity> getPayStatus = pmDAO.findAll();
	    
	    Map<String, Double> statusCounts = new HashMap<>();
	    int notStartedCount = 0;
	    int deliveredCount = 0;
	    int inProgressCount = 0;
	    int paymentStatusCount = 0;
	    int pickedCount = 0;

	    // Count statuses from DashBoardEntity
	    for (DashBoardEntity dbEnt : getDashBoard) {
	        String status = dbEnt.getStatus();

	        if ("Not Started".equals(status)) {
	            notStartedCount++;
	        } else if ("Delivered".equals(status)) {
	            deliveredCount++;
	        } else if ("In Progress".equals(status)) {
	            inProgressCount++;
	        } else if ("Picked".equals(status)) {
	            pickedCount++;
	        }
	    }

	    // Count payment statuses from PaymentMasterEntity
	    for (PaymentMasterEntity pmEnt : getPayStatus) {
	        String status = pmEnt.getPaymentStatus();

	        if ("cleared".equals(status)) {
	            paymentStatusCount++;
	        }
	    }

	    // Get total amount and handle potential null value
	    Double totalAmount = dbDAO.getTotalPrice();
	    if (totalAmount == null) {
	        totalAmount = 0.0; // Default to 0.0 if null
	    }

	    // Populate the statusCounts map
	    statusCounts.put("Not Started", (double) notStartedCount);
	    statusCounts.put("Delivered", (double) deliveredCount);
	    statusCounts.put("In Progress", (double) inProgressCount);
	    statusCounts.put("Picked", (double) pickedCount);
	    statusCounts.put("totalAmount", totalAmount);
	    statusCounts.put("Cleared", (double) paymentStatusCount);

	    return statusCounts;
	}

	public List<DashBoardEntity> getOrderCompletedList() {
		List<DashBoardEntity> ocList = dbDAO.getOrderCompletedList();
		return ocList;
	}

	public List<DashBoardEntity> getOpenOrdersList() {
		List<DashBoardEntity> ooList = dbDAO.getOpenOrdersList();
		return ooList;
	}

}
