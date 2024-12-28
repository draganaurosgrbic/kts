package com.example.demo.dto;

import javax.validation.constraints.NotBlank;

import com.example.demo.model.Category;

public class CategoryDTO {
	
	private Long id;
	
	@NotBlank
	private String name;
	
	public CategoryDTO() {
		super();
	}
	
	public CategoryDTO(Category category) {
		super();
		this.id = category.getId();
		this.name = category.getName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
