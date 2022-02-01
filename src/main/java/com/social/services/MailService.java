package com.social.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	@Autowired
	private JavaMailSender javaMailSender;
	
	private void sendMail(String email, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email); 
        message.setSubject(subject); 
        message.setText(body);
        message.setFrom("Post sharing <no-reply@gmail.com>");
        this.javaMailSender.send(message);
	}

	public void send(String email, String subject, String body) {
		this.sendMail(email, subject, body);
	}
}
