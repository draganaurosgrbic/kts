package com.example.demo.service;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.model.Image;

public class Email {
	
	private String to;
	private String subject;
	private String text;
	private Set<Image> images = new HashSet<>();
	
	public Email() {
		super();
	}

	public Email(String to, String subject, String text) {
		super();
		this.to = to;
		this.subject = subject;
		this.text = text;
	}
	
	public Email(String to, String subject, String text, Set<Image> images) {
		super();
		this.to = to;
		this.subject = subject;
		this.text = text;
		this.images = images;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}
	
}
