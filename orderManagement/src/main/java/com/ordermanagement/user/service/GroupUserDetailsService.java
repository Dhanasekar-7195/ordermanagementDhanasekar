package com.ordermanagement.user.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ordermanagement.user.dao.UserMasterDAO;
import com.ordermanagement.user.entity.UserMasterEntity;

@Service
public class GroupUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserMasterDAO umDAO;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<UserMasterEntity> user = umDAO.findUserByName(username);
		return user.map(GroupUserDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException(username + "doesn't exist in the master"));
	}
	


	
	
	



}
