//package com.ordermanagement.deliveries.dao;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import com.ordermanagement.deliveries.entity.DeliveryMasterEntity;
//import com.ordermanagement.invoices.entity.InvoiceMasterEntity;
//
//public interface DeliveryMasterDAO extends JpaRepository<DeliveryMasterEntity, String> {
//
//	@Query(value = "SELECT * FROM delivery_master WHERE deliveryId = ?1", nativeQuery = true)
//	DeliveryMasterEntity findByDeliveryId(String deliveryId);
//
//}
