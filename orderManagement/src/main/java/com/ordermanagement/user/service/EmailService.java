package com.ordermanagement.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ordermanagement.user.dao.UserMasterDAO;
import com.ordermanagement.user.entity.UserMasterEntity;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private UserMasterDAO umDAO;

	public void sendEmail(String email, String token) {

		String otp = token;
	    String subject = otp + " is your Ikyam reset password code";
	    UserMasterEntity umEnt = umDAO.findUserByEmailBy(email);
	    String username = umEnt.getUserName();

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject(subject);
	    message.setText("Hi " + username + ",\n\nYou can enter this code to reset your password:\n" + otp);
		mailSender.send(message);

	}
}
