package com.example.demo.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.persistence.ForeignKey;

@Entity
@Table(name = "comment_table")
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
		
	@NotNull
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cultural_offer_id",
        foreignKey = @ForeignKey(
            foreignKeyDefinition = "FOREIGN KEY (cultural_offer_id) REFERENCES cultural_offer_table(id) ON DELETE CASCADE"
        )
	)
	private CulturalOffer culturalOffer;
	
	@NotNull
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "rate")
	private int rate;
		
	@NotBlank
	@Column(name = "text")
	private String text;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "comment_image", 
		joinColumns = @JoinColumn(referencedColumnName = "id", name = "comment_id", 
		foreignKey = @ForeignKey(
	            foreignKeyDefinition = "FOREIGN KEY (comment_id) REFERENCES comment_table(id) ON DELETE CASCADE"
	        )
		), 
		inverseJoinColumns = @JoinColumn(referencedColumnName = "id", name = "image_id")
	)
	private Set<Image> images = new HashSet<>();
		
	public Comment() {
		super();
		this.createdAt = new Date();
	}
	
	public void addImage(Image image) {
		this.images.add(image);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public CulturalOffer getCulturalOffer() {
		return culturalOffer;
	}

	public void setCulturalOffer(CulturalOffer culturalOffer) {
		this.culturalOffer = culturalOffer;
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

	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}

}
