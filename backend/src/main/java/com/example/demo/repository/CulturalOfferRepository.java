package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.CulturalOffer;

@Repository
public interface CulturalOfferRepository extends JpaRepository<CulturalOffer, Long> {

	@Query("select co from CulturalOffer co where (co.id != :id or :id is null) and co.name=:name")
	public CulturalOffer hasName(Long id, String name);

	@Query("select co.name from CulturalOffer co where lower(co.name) like lower(concat('%', :filter, '%')) order by co.name")
	public List<String> filterNames(String filter);
	
	@Query("select co.location from CulturalOffer co where lower(co.location) like lower(concat('%', :filter, '%')) order by co.location")
	public List<String> filterLocations(String filter);
	
	@Query("select distinct co.type.name from CulturalOffer co where lower(co.type.name) like lower(concat('%', :filter, '%')) order by co.type.name")
	public List<String> filterTypes(String filter);
	
	@Query("select co from CulturalOffer co where lower(co.name) like lower(concat('%', :name, '%')) and lower(co.location) like lower(concat('%', :location, '%')) and lower(co.type.name) like lower(concat('%', :type, '%')) order by co.name")
    public Page<CulturalOffer> filter(String name, String location, String type, Pageable pageable);
		
}
