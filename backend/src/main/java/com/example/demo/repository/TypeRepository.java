package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Type;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {
	
	public Type findByName(String name);

	@Query("select t.name from Type t where lower(t.name) like lower(concat('%',:filter,'%')) order by t.name")
	public List<String> filterNames(String filter);
	
	public Page<Type> findAllByOrderByName(Pageable pageable);
	
}
