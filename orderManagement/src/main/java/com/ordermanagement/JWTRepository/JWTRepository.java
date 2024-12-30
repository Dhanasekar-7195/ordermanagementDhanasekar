package com.ordermanagement.JWTRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ordermanagement.JWTEntity.JWTEntity;

public interface JWTRepository extends JpaRepository<JWTEntity, String> {

	@Query(value = "DELETE FROM jwtresponse where jwttoken=?1 ", nativeQuery = true)
	public String deleteByToken(String token);

	@Query(value = "Select * FROM jwtresponse where userid=?1 ", nativeQuery = true)
	public JWTEntity findByUid(String userid);

}