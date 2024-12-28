package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user_following_table", 
uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "cultural_offer_id"})})
public class UserFollowing {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
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

	public UserFollowing() {
		super();
	}

	public UserFollowing(User user, CulturalOffer culturalOffer) {
		super();
		this.user = user;
		this.culturalOffer = culturalOffer;
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
	
}
