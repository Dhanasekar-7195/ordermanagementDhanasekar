package com.ordermanagement.dashboard.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordermanagement.dashboard.entity.DashBoardEntity;
import com.ordermanagement.dashboard.service.DashBoardService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("api/dashboard")
@Api(description = "Dashboard Service", tags = { "DashboardAPI" })
public class DashBoardController {

	@Autowired
	private DashBoardService dbService;

	@GetMapping("/get_all_dashboard")
	public List<DashBoardEntity> getAllDashBoardDetails() {
		return this.dbService.getAllDashBoardDetails();
	}

	@GetMapping("/get_dashboard_counts")
	public Map<String, Double> getDashBoardCounts() {
		return this.dbService.getDashBoardCounts();
	}

	@GetMapping("/get_order_completed_list")
	public List<DashBoardEntity> getOrderCompletedList() {
		return this.dbService.getOrderCompletedList();
	}

	@GetMapping("/get_open_orders_list")
	public List<DashBoardEntity> getOpenOrdersList() {
		return this.dbService.getOpenOrdersList();
	}

}
