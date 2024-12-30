package com.ordermanagement.orders.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ordermanagement.orders.entity.OrderMasterItemEntity;

public interface OrderMasterItemDAO extends JpaRepository<OrderMasterItemEntity, String> {
	@Query(value = "SELECT * FROM order_master_item WHERE orderId = :orderId", nativeQuery = true)
	List<OrderMasterItemEntity> findByOrderId(@Param("orderId") String orderId);

}
