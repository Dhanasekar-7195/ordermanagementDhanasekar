package com.ordermanagement.user.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ordermanagement.customers.entity.CustomerMasterEntity;
import com.ordermanagement.user.entity.UserMasterEntity;

public interface UserMasterDAO extends JpaRepository<UserMasterEntity, String> {

	@Query(value = "Select * FROM user_master where userName=?1 ", nativeQuery = true)
	Optional<UserMasterEntity> findUserByName(String userName);

	@Query(value = "Select * FROM user_master where userName=?1", nativeQuery = true)
	UserMasterEntity findUserByNameBy(String userName);

	@Query(value = "Select * FROM user_master where email=?1", nativeQuery = true)
	UserMasterEntity findUserByEmailBy(String email);

	@Query(value = "Select * FROM user_master where mobileNumber=?1", nativeQuery = true)
	UserMasterEntity findUserByPhoneNoBy(String mobileNumber);

	UserMasterEntity findByEmail(String email);

	UserMasterEntity findByToken(String token);

	@Query(value = "Select * From user_master where userId=?1", nativeQuery = true)
	UserMasterEntity getUserById(String userId);
	
	@Query(value = "Select * FROM user_master where email=?2 AND userId != ?1", nativeQuery = true)
	UserMasterEntity findUserByEmailByExcludeUserId(String userId, String email);
	
	@Query(value = "Select * FROM user_master where mobileNumber=?2 AND userId != ?1", nativeQuery = true)
	UserMasterEntity findUserByPhoneNoByExcludeUserId(String userId, String mobileNumber);
	
	@Query(value = "SELECT * FROM user_master WHERE userName = ?1", nativeQuery = true)
	List<UserMasterEntity> findUserByNameList(String userName);

	List<UserMasterEntity> findByUserName(String username);
	
	@Query(value = "SELECT * FROM user_master WHERE userId = ?1", nativeQuery = true)
	UserMasterEntity getAllUserById(String userId);
	
	@Query(value ="SELECT u.returnCredit FROM user_master u WHERE u.userId = ?1", nativeQuery = true)
	double getreturnCredit(String userId);

	@Query(value = "Select * FROM user_master where userName=?1", nativeQuery = true)
	List<UserMasterEntity> findUserByNameByList(String userName);

	@Query(value = "SELECT * FROM user_master WHERE companyName = ?1", nativeQuery = true)
	List<UserMasterEntity> findByCompanyName(String companyName);
	
	@Query(value = "SELECT * FROM user_master WHERE companyName = ?1 LIMIT 1", nativeQuery = true)
	UserMasterEntity getByCompanyName(String companyName);


}
