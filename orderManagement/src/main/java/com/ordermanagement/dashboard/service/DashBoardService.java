package com.ordermanagement.dashboard.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ordermanagement.dashboard.entity.DashBoardEntity;
import com.ordermanagement.dashboard.repository.DashBoardRepository;

@Component
public class DashBoardService {

	@Autowired
	private DashBoardRepository dbRepo;

	public List<DashBoardEntity> getAllDashBoardDetails() {
		return this.dbRepo.getAllDashBoardDetails();
	}

	public Map<String, Double> getDashBoardCounts() {
		return this.dbRepo.getDashBoardCounts();
	}

	public List<DashBoardEntity> getOrderCompletedList() {
		return this.dbRepo.getOrderCompletedList();
	}

	public List<DashBoardEntity> getOpenOrdersList() {
		return this.dbRepo.getOpenOrdersList();
	}

}
