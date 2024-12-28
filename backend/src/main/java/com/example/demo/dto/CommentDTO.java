package com.example.demo.dto;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.model.Comment;
import com.example.demo.model.Image;

public class CommentDTO {

	private long id;
	private String user;
	private long culturalOfferId;
	private Date createdAt;
	private int rate;
	private String text;
	private List<String> images;

	public CommentDTO() {
		super();
	}

	public CommentDTO(Comment comment) {
		super();
		this.id = comment.getId();
		this.user = comment.getUser().getEmail();
		this.culturalOfferId = comment.getCulturalOffer().getId();
		this.createdAt = comment.getCreatedAt();
		this.rate = comment.getRate();
		this.text = comment.getText();
		this.images = comment.getImages().stream().map(Image::getPath).collect(Collectors.toList());
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public long getCulturalOfferId() {
		return culturalOfferId;
	}

	public void setCulturalOfferId(long culturalOfferId) {
		this.culturalOfferId = culturalOfferId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}
	
}
