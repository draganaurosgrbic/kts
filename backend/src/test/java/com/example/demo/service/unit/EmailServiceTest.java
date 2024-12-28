package com.example.demo.service.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.model.Image;
import com.example.demo.service.Email;
import com.example.demo.service.EmailService;
import com.icegreen.greenmail.store.FolderException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailServiceTest {

	@Autowired
	private EmailService emailService;

	@MockBean
	private JavaMailSenderImpl sender;
	
	@Test
	public void testValidMail() throws MessagingException, IOException, FolderException {
		Email email = new Email();
		email.setTo("recipient@example.org");
		email.setSubject("Spring Mail Integration Testing with JUnit and GreenMail Example");
		email.setText("We show how to write Integration Tests using Spring and GreenMail.");
		
		MimeMessage mimeMessage = new MimeMessage((Session)null);
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getTo()));
		mimeMessage.setSubject(email.getSubject());
        mimeMessage.setText(email.getText());
        
		Mockito.when(this.sender.createMimeMessage()).thenReturn(mimeMessage);
		this.emailService.sendEmail(email);
		assertEquals(email.getSubject(), mimeMessage.getSubject());
	}

	@Test
	public void testValidImageMail() throws MessagingException, IOException, FolderException {
		Email email = new Email();
		email.setTo("recipient@example.org");
		email.setSubject("Spring Mail Integration Testing with JUnit and GreenMail Example");
		email.setText("We show how to write Integration Tests using Spring and GreenMail.");
		Set<Image> images = new HashSet<Image>();
		images.add(new Image("/image_fair.png"));
		email.setImages(images);

		MimeMessage mimeMessage = new MimeMessage((Session)null);
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getTo()));
		mimeMessage.setSubject(email.getSubject());
        mimeMessage.setText(email.getText());
        
        Mockito.when(this.sender.createMimeMessage())
        .thenReturn(mimeMessage);
		this.emailService.sendEmail(email);
		assertEquals(email.getSubject(), mimeMessage.getSubject());
	}

	@Test(expected = InstantiationError.class)
	public void testMailNoTo() {
		Email email = new Email();
		email.setTo("");
		email.setSubject("Spring Mail Integration Testing with JUnit and GreenMail Example");
		email.setText("We show how to write Integration Tests using Spring and GreenMail.");

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email.getTo());
		message.setSubject(email.getSubject());
		message.setText(email.getText());
		
		Mockito.doThrow(MailException.class).when(this.sender).send(message);
		this.emailService.sendEmail(email);
	}

	@Test(expected = InstantiationError.class)
	public void testMailNoSubject() {
		Email email = new Email();
		email.setTo("recipient@example.org");
		email.setSubject("");
		email.setText("We show how to write Integration Tests using Spring and GreenMail.");

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email.getTo());
		message.setSubject(email.getSubject());
		message.setText(email.getText());
		
		Mockito.doThrow(MailException.class).when(this.sender).send(message);
		this.emailService.sendEmail(email);
	}

	@Test(expected = InstantiationError.class)
	public void testMailNoText() {
		Email email = new Email();
		email.setTo("recipient@example.org");
		email.setSubject("Spring Mail Integration Testing with JUnit and GreenMail Example");
		email.setText("");

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email.getTo());
		message.setSubject(email.getSubject());
		message.setText(email.getText());
		
		Mockito.doThrow(MailException.class).when(this.sender).send(message);
		this.emailService.sendEmail(email);
	}

	@Test(expected = InstantiationError.class)
	public void testImageMailNoTo() throws MessagingException {
		Email email = new Email();
		email.setTo("");
		email.setSubject("Spring Mail Integration Testing with JUnit and GreenMail Example");
		email.setText("We show how to write Integration Tests using Spring and GreenMail.");
		Set<Image> images = new HashSet<Image>();
		images.add(new Image("/image_fair.png"));
		email.setImages(images);

		MimeMessage mimeMessage = new MimeMessage((Session)null);
		mimeMessage.setSubject(email.getSubject());
        mimeMessage.setText(email.getText());
        
		Mockito.doThrow(MailException.class).when(this.sender).send(mimeMessage);
		this.emailService.sendEmailWithAttachments(email);
	}

	@Test(expected = InstantiationError.class)
	public void testImageMailNoSubject() throws MessagingException {
		Email email = new Email();
		email.setTo("recipient@example.org");
		email.setSubject("");
		email.setText("We show how to write Integration Tests using Spring and GreenMail.");
		Set<Image> images = new HashSet<Image>();
		images.add(new Image("/image_fair.png"));
		email.setImages(images);

		MimeMessage mimeMessage = new MimeMessage((Session)null);
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getTo()));
		mimeMessage.setSubject(email.getSubject());
        mimeMessage.setText(email.getText());
        
		Mockito.doThrow(MailException.class).when(this.sender).send(mimeMessage);
		this.emailService.sendEmailWithAttachments(email);
	}

	@Test(expected = InstantiationError.class)
	public void testImageMailNoText() throws MessagingException {
		Email email = new Email();
		email.setTo("recipient@example.org");
		email.setSubject("Spring Mail Integration Testing with JUnit and GreenMail Example");
		email.setText("");
		Set<Image> images = new HashSet<Image>();
		images.add(new Image("/image_fair.png"));
		email.setImages(images);

		MimeMessage mimeMessage = new MimeMessage((Session)null);
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getTo()));
		mimeMessage.setSubject(email.getSubject());
        mimeMessage.setText(email.getText());
        
		Mockito.doThrow(MailException.class).when(this.sender).send(mimeMessage);
		this.emailService.sendEmailWithAttachments(email);
	}

	@Test(expected = InstantiationError.class)
	public void testImageMailInvalidImages() throws MessagingException {
		Email email = new Email();
		email.setTo("recipient@example.org");
		email.setSubject("Spring Mail Integration Testing with JUnit and GreenMail Example");
		email.setText("We show how to write Integration Tests using Spring and GreenMail.");
		Set<Image> images = new HashSet<Image>();
		images.add(new Image("/awdwadawdawdaw.png"));
		email.setImages(images);

		MimeMessage mimeMessage = new MimeMessage((Session)null);
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getTo()));
		mimeMessage.setSubject(email.getSubject());
        mimeMessage.setText(email.getText());
        
		Mockito.doThrow(MailException.class).when(this.sender).send(mimeMessage);
		this.emailService.sendEmailWithAttachments(email);
	}

}
