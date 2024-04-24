package com.ordermanagement.user.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ordermanagement.user.entity.UserMasterEntity;

public interface UserMasterDAO extends JpaRepository<UserMasterEntity, String>{
	
	@Query(value = "Select * FROM user_master where userName=?1 ", nativeQuery = true)
	Optional<UserMasterEntity> findUserByName(String userName);
	
	@Query(value = "Select * FROM user_master where userName=?1", nativeQuery = true)
	UserMasterEntity findUserByNameBy(String userName);
	
	@Query(value = "Select * FROM user_master where email=?1", nativeQuery = true)
	UserMasterEntity findUserByEmailBy(String email);
	
	@Query(value = "Select * FROM user_master where phoneNumber=?1", nativeQuery = true)
	UserMasterEntity findUserByPhoneNoBy(String phoneNumber);

}
