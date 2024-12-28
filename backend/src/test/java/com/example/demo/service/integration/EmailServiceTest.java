package com.example.demo.service.integration;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.model.Image;
import com.example.demo.service.Email;
import com.example.demo.service.EmailService;
import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailServiceTest {

	@Autowired
	private EmailService emailService;

	private static GreenMail testSmtp;
	private static final int EMAIL_WAIT = 10000;

	@BeforeClass
	public static void testSmtpInit() {
		ServerSetup setup = new ServerSetup(587, "localhost", "smtp");
		testSmtp = new GreenMail(setup);
		testSmtp.start();
	}

	@Test
	public void testValidMail() throws MessagingException, IOException, FolderException {
		Email email = new Email();
		email.setTo("recipient@example.org");
		email.setSubject("Spring Mail Integration Testing with JUnit and GreenMail Example");
		email.setText("We show how to write Integration Tests using Spring and GreenMail.");
		this.emailService.sendEmail(email);

		assertTrue(testSmtp.waitForIncomingEmail(EMAIL_WAIT, 1));
		MimeMessage[] receivedMessages = testSmtp.getReceivedMessages();
		MimeMessage current = receivedMessages[0];
		String body = GreenMailUtil.getBody(current).replaceAll("=\r?\n", "");

		assertEquals(email.getSubject(), current.getSubject());
		assertEquals(email.getTo(), current.getAllRecipients()[0].toString());
		assertEquals(email.getText(), body);
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
		this.emailService.sendEmailWithAttachments(email);

		assertTrue(testSmtp.waitForIncomingEmail(EMAIL_WAIT, 1));
		MimeMessage[] receivedMessages = testSmtp.getReceivedMessages();
		MimeMessage current = receivedMessages[0];
		String body = GreenMailUtil.getBody(current).replaceAll("=\r?\n", "");
		
		assertEquals(email.getSubject(), current.getSubject());
		assertEquals(email.getTo(), current.getAllRecipients()[0].toString());
		assertEquals(email.getText(), body);
	}

	@Test
	public void testMailNoTo() {
		Email email = new Email();
		email.setTo("");
		email.setSubject("Spring Mail Integration Testing with JUnit and GreenMail Example");
		email.setText("We show how to write Integration Tests using Spring and GreenMail.");

		this.emailService.sendEmail(email);
		assertTrue(testSmtp.waitForIncomingEmail(EMAIL_WAIT, 0));
	}

	@Test
	public void testMailNoSubject() {
		Email email = new Email();
		email.setTo("recipient@example.org");
		email.setSubject("");
		email.setText("We show how to write Integration Tests using Spring and GreenMail.");

		this.emailService.sendEmail(email);
		assertTrue(testSmtp.waitForIncomingEmail(EMAIL_WAIT, 0));
	}

	@Test
	public void testMailNoText() {
		Email email = new Email();
		email.setTo("recipient@example.org");
		email.setSubject("Spring Mail Integration Testing with JUnit and GreenMail Example");
		email.setText("");

		this.emailService.sendEmail(email);
		assertTrue(testSmtp.waitForIncomingEmail(EMAIL_WAIT, 0));
	}

	@Test
	public void testImageMailNoTo() {
		Email email = new Email();
		email.setTo("");
		email.setSubject("Spring Mail Integration Testing with JUnit and GreenMail Example");
		email.setText("We show how to write Integration Tests using Spring and GreenMail.");
		Set<Image> images = new HashSet<Image>();
		images.add(new Image("/image_fair.png"));
		email.setImages(images);

		this.emailService.sendEmailWithAttachments(email);
		assertTrue(testSmtp.waitForIncomingEmail(EMAIL_WAIT, 0));
	}

	@Test
	public void testImageMailNoSubject() {
		Email email = new Email();
		email.setTo("recipient@example.org");
		email.setSubject("");
		email.setText("We show how to write Integration Tests using Spring and GreenMail.");
		Set<Image> images = new HashSet<Image>();
		images.add(new Image("/image_fair.png"));
		email.setImages(images);

		this.emailService.sendEmailWithAttachments(email);
		assertTrue(testSmtp.waitForIncomingEmail(EMAIL_WAIT, 0));
	}

	@Test
	public void testImageMailNoText() {
		Email email = new Email();
		email.setTo("recipient@example.org");
		email.setSubject("Spring Mail Integration Testing with JUnit and GreenMail Example");
		email.setText("");
		Set<Image> images = new HashSet<Image>();
		images.add(new Image("/image_fair.png"));
		email.setImages(images);

		this.emailService.sendEmailWithAttachments(email);
		assertTrue(testSmtp.waitForIncomingEmail(EMAIL_WAIT, 0));
	}

	@Test
	public void testImageMailInvalidImages() {
		Email email = new Email();
		email.setTo("recipient@example.org");
		email.setSubject("Spring Mail Integration Testing with JUnit and GreenMail Example");
		email.setText("We show how to write Integration Tests using Spring and GreenMail.");
		Set<Image> images = new HashSet<Image>();
		images.add(new Image("/awdwadawdawdaw.png"));
		email.setImages(images);

		this.emailService.sendEmailWithAttachments(email);
		assertTrue(testSmtp.waitForIncomingEmail(EMAIL_WAIT, 0));
	}

	@AfterClass
	public static void testSmtpCleanup() {
		testSmtp.stop();
	}

}
