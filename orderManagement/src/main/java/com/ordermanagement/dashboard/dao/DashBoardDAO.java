package com.ordermanagement.dashboard.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ordermanagement.dashboard.entity.DashBoardEntity;

public interface DashBoardDAO extends JpaRepository<DashBoardEntity, String> {

	@Query(value = "SELECT SUM(totalAmount) FROM dashboard", nativeQuery = true)
	Double getTotalPrice();

	@Query(value = "SELECT * FROM dashboard WHERE status = 'Delivered'", nativeQuery = true)
	List<DashBoardEntity> getOrderCompletedList();

	@Query(value = "SELECT * FROM dashboard WHERE status = 'Not Started'", nativeQuery = true)
	List<DashBoardEntity> getOpenOrdersList();

	@Query(value = "SELECT * FROM dashboard WHERE orderId = ?1", nativeQuery = true)
	DashBoardEntity getDashBoardDetailsByOrderId(String orderId);
	
	@Query(value = "SELECT * FROM dashboard WHERE customerId = ?1", nativeQuery = true)
	List<DashBoardEntity> findAllByCustomerId(String customerId);

	@Query(value = "SELECT * FROM dashboard WHERE draftId = ?1", nativeQuery = true)
	DashBoardEntity getDashBoardDetailsByDraftId(String draftId);
}
