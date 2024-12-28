package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	@Query("select coalesce(avg(c.rate), 0) from Comment c where c.culturalOffer.id=:culturalOfferId")
	public double totalRate(long culturalOfferId);

    public Page<Comment> findByCulturalOfferIdOrderByCreatedAtDesc(long culturalOfferId, Pageable pageable);
	
}
