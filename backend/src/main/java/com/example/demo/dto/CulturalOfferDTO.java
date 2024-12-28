package com.example.demo.dto;

import com.example.demo.model.CulturalOffer;

public class CulturalOfferDTO {
	
	private long id;
	private String category;
	private String type;
	private String placemarkIcon;
	private String name;
	private String location;
	private double lat;
	private double lng;
	private String description;
	private String image;
	private double totalRate;
	private boolean followed;
	
	public CulturalOfferDTO() {
		super();
	}
	
	public CulturalOfferDTO(CulturalOffer culturalOffer) {
		this.id = culturalOffer.getId();
		this.category = culturalOffer.getType().getCategory().getName();
		this.type = culturalOffer.getType().getName();
		this.placemarkIcon = culturalOffer.getType().getPlacemarkIcon();
		this.name = culturalOffer.getName();
		this.location = culturalOffer.getLocation();
		this.lat = culturalOffer.getLat();
		this.lng = culturalOffer.getLng();
		this.description = culturalOffer.getDescription();
		this.image = culturalOffer.getImage();
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPlacemarkIcon() {
		return placemarkIcon;
	}

	public void setPlacemarkIcon(String placemarkIcon) {
		this.placemarkIcon = placemarkIcon;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public double getTotalRate() {
		return totalRate;
	}

	public void setTotalRate(double totalRate) {
		this.totalRate = totalRate;
	}

	public boolean isFollowed() {
		return followed;
	}

	public void setFollowed(boolean followed) {
		this.followed = followed;
	}

}
