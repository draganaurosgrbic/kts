package com.example.demo.dto;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

public class CulturalOfferUploadDTO {

	private Long id;
	
	@NotBlank
	private String type;
	
	@NotBlank
	private String name;
		
	@NotBlank
	private String location;

	private double lat;
	private double lng;
	private String description;
	private MultipartFile image;
	private String imagePath;
	
	public CulturalOfferUploadDTO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
}
