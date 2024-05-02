package com.ordermanagement.orders.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ordermanagement.orders.entity.OrderMasterItemEntity;

public interface OrderMasterItemDAO extends JpaRepository<OrderMasterItemEntity, String> {

}
