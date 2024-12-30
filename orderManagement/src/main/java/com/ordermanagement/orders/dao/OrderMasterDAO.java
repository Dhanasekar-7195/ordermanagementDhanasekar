package com.ordermanagement.orders.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ordermanagement.orders.entity.OrderMasterEntity;

public interface OrderMasterDAO extends JpaRepository<OrderMasterEntity, String> {

	@Query(value = "SELECT * FROM order_master WHERE orderId LIKE ?1%", nativeQuery = true)
	List<OrderMasterEntity> getAllOrderById(String orderId);

	@Query(value = "SELECT * FROM order_master WHERE orderId = ?1", nativeQuery = true)
	OrderMasterEntity getOrderById(String orderId);

	@Query(value = "SELECT * FROM order_master_sap WHERE customerId = ?1", nativeQuery = true)
	List<OrderMasterEntity> getOrdersByCustomerId(String customerId);
	
	@Query(value = "SELECT * FROM order_master WHERE draftId IS NOT NULL", nativeQuery = true)
	List<OrderMasterEntity> getAllDraftMaster();
	
	@Query(value = "SELECT * FROM order_master WHERE draftId IS NULL", nativeQuery = true)
	List<OrderMasterEntity> getAllOrderMaster();

	//List<OrderMasterEntity> findByCustomerId();

//	Optional<OrderMasterEntity> findByDraftId(String draftId);

//	@Query(value = "SELECT * FROM order_master WHERE draftId = ?1", nativeQuery = true)
//	OrderMasterEntity getDraftById(String draftId);

}
