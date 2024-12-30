package com.ordermanagement.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ordermanagement.user.dao.UserMasterDAO;
import com.ordermanagement.user.entity.UserMasterEntity;

@Service
public class GroupUserDetailsService implements UserDetailsService {

	@Autowired
	private UserMasterDAO umDAO;

//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//		Optional<UserMasterEntity> user = umDAO.findUserByName(username);
//		return user.map(GroupUserDetails::new)
//				.orElseThrow(() -> new UsernameNotFoundException(username + "doesn't exist in the master"));
//	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    List<UserMasterEntity> users = umDAO.findByUserName(username);
	    
	    if (users.isEmpty()) {
	        throw new UsernameNotFoundException("User not found");
	    }
 
	    // You can also choose to check the password here if needed.
	    UserMasterEntity user = users.get(0); // If you need to differentiate, you might need to adjust this logic.
	    
	    return new GroupUserDetails(user);
	}

}
