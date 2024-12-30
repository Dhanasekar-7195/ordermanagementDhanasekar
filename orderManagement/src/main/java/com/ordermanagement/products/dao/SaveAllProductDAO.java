package com.ordermanagement.products.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ordermanagement.products.entity.SaveAllProductEntity;

public interface SaveAllProductDAO extends JpaRepository<SaveAllProductEntity, String> {

}
