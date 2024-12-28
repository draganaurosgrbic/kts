package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	public Category findByName(String name);
	
	@Query("select c.name from Category c where lower(c.name) like lower(concat('%',:filter,'%')) order by c.name")
	public List<String> filterNames(String filter);
	
	public Page<Category> findAllByOrderByName(Pageable pageable);

}
