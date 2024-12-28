package com.example.demo.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    @Query("select n from News n where n.culturalOffer.id = :culturalOfferId and (cast(:startDate as date) is null or n.createdAt >= :startDate) and (cast(:endDate as date) is null or n.createdAt <= :endDate) order by n.createdAt desc")
    public Page<News> filter(long culturalOfferId, Date startDate, Date endDate, Pageable pageable);
    
}
