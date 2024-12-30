package com.ordermanagement.customers.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ordermanagement.customers.entity.SaveAllCustomerEntity;

public interface SaveAllCustomerDAO extends JpaRepository<SaveAllCustomerEntity, String> {

}
