package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.CulturalOffer;
import com.example.demo.model.UserFollowing;

@Repository
public interface UserFollowingRepository extends JpaRepository<UserFollowing, Long> {

	public UserFollowing findByUserIdAndCulturalOfferId(long userId, long culturalOfferId);

	@Query("select uf.culturalOffer from UserFollowing uf where uf.user.id = :userId and (lower(uf.culturalOffer.name) like lower(concat('%', :name, '%')) and lower(uf.culturalOffer.location) like lower(concat('%', :location, '%')) and lower(uf.culturalOffer.type.name) like lower(concat('%', :type, '%'))) order by uf.culturalOffer.name")
    public Page<CulturalOffer> filter(long userId, String name, String location, String type, Pageable pageable);

	@Query("select uf.user.email from UserFollowing uf where uf.culturalOffer.id=:culturalOfferId order by uf.user.email")
	public List<String> subscribedEmails(long culturalOfferId);

}

