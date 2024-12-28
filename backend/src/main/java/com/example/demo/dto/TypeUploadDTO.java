package com.example.demo.dto;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

public class TypeUploadDTO {
	
	private Long id;

	@NotBlank
	private String category;

	@NotBlank
	private String name;
		
	private MultipartFile placemarkIcon;
	
	public TypeUploadDTO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MultipartFile getPlacemarkIcon() {
		return placemarkIcon;
	}

	public void setPlacemarkIcon(MultipartFile placemarkIcon) {
		this.placemarkIcon = placemarkIcon;
	}

}
