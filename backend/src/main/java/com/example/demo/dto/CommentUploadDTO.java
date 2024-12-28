package com.example.demo.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

public class CommentUploadDTO {
	
	private Long id;
	private long culturalOfferId;
	private int rate;

	@NotBlank
	private String text;
	private List<MultipartFile> images;
	private List<String> imagePaths;
	
	public CommentUploadDTO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getCulturalOfferId() {
		return culturalOfferId;
	}

	public void setCulturalOfferId(long culturalOfferId) {
		this.culturalOfferId = culturalOfferId;
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

	public List<MultipartFile> getImages() {
		return images;
	}

	public void setImages(List<MultipartFile> images) {
		this.images = images;
	}

	public List<String> getImagePaths() {
		return imagePaths;
	}

	public void setImagePaths(List<String> imagePaths) {
		this.imagePaths = imagePaths;
	}

}
