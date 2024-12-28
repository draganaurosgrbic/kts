package com.example.demo.dto;

import com.example.demo.model.Type;

public class TypeDTO {
	
	private long id;
	private String category;
	private String name;
	private String placemarkIcon;

	public TypeDTO() {
		super();
	}
	
	public TypeDTO(Type type) {
		super();
		this.id = type.getId();
		this.category = type.getCategory().getName();
		this.name = type.getName();
		this.placemarkIcon = type.getPlacemarkIcon();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public String getPlacemarkIcon() {
		return placemarkIcon;
	}

	public void setPlacemarkIcon(String placemarkIcon) {
		this.placemarkIcon = placemarkIcon;
	}
	
}
