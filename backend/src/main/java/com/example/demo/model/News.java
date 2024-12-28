package com.example.demo.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "news_table")
public class News {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
		
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
			
	@NotBlank
	@Column(name = "text")
	private String text;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "news_image", 
		joinColumns = @JoinColumn(referencedColumnName = "id", name = "news_id", 
			foreignKey = @ForeignKey(
		            foreignKeyDefinition = "FOREIGN KEY (news_id) REFERENCES news_table(id) ON DELETE CASCADE"
	        )
		), 
		inverseJoinColumns = @JoinColumn(referencedColumnName = "id", name = "image_id")
	)
	private Set<Image> images = new HashSet<>();
	
	public News() {
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
