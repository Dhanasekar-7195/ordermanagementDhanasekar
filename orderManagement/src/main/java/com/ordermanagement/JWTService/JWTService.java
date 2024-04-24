package com.ordermanagement.JWTService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ordermanagement.JWTEntity.JWTEntity;
import com.ordermanagement.JWTRepository.JWTRepository;

@Component
public class JWTService {
	
	@Autowired
	private JWTRepository jwtRepo;
	
	public String storeJWT(String userId, String token, String role) {
		JWTEntity jwtR = new JWTEntity();

		if (jwtRepo.findByUid(userId) == null) {
			jwtR.setUserid(userId);
			jwtR.setJwttoken(token);
			jwtR.setRole(role);
			jwtRepo.save(jwtR);
		} else {
			JWTEntity tempJwt = jwtRepo.findByUid(userId);
			jwtR.setJwtid(tempJwt.getJwtid());
			jwtR.setUserid(userId);
			jwtR.setJwttoken(token);
			jwtR.setRole(role);
			jwtRepo.save(jwtR);
		}

		return null;
	}

	public String deleteJWT(String token) {
		jwtRepo.deleteByToken(token);
		return null;
	}


}
