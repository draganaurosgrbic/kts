package com.example.demo.service;

import java.io.File;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.demo.constants.Constants;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSenderImpl sender;
	
	@Async
	public void sendEmail(Email email) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email.getTo());
		message.setSubject(email.getSubject());
		message.setText(email.getText());
		try {
			this.sender.send(message);
		}
		catch(Exception e) {
			;
		}
	}
	
	@Async
	public void sendEmailWithAttachments(Email email) {
	    MimeMessagePreparator preparator = new MimeMessagePreparator() 
	    {
			@Override
	        public void prepare(MimeMessage mimeMessage) throws Exception 
	        {
	            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getTo()));
	            mimeMessage.setSubject(email.getSubject());
	            mimeMessage.setText(email.getText());
	            
	            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	            helper.setText(email.getText(), true);
	            email.getImages().stream().forEach(image -> {
	            	String path = Constants.STATIC_FOLDER + File.separatorChar + image.getPath().replace(Constants.BACKEND_URL, "").substring(1);
	            	FileSystemResource file = new FileSystemResource(path);
	            	try {
		            	helper.addAttachment(path, file);
	            	}
	            	catch(Exception e) {
	            		return;
	            	}
	            });
	        }
	    };
	    try {
	    	this.sender.send(preparator);
	    }
	    catch(Exception e) {
	    	;
	    }
	}

}
