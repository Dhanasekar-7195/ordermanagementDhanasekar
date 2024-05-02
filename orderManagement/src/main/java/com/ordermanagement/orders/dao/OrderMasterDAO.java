package com.ordermanagement.orders.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ordermanagement.orders.entity.OrderMasterEntity;
import com.ordermanagement.products.entity.ProductMasterEntity;

public interface OrderMasterDAO extends JpaRepository<OrderMasterEntity, String> {

    @Query(value = "SELECT * FROM order_master WHERE orderId LIKE ?1%", nativeQuery = true)
	List<OrderMasterEntity> getAllOrderById(String orderId);

}
