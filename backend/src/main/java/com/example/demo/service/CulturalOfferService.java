package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.FilterParamsDTO;
import com.example.demo.dto.UniqueCheckDTO;
import com.example.demo.model.CulturalOffer;
import com.example.demo.model.Image;
import com.example.demo.repository.CulturalOfferRepository;

@Service
@Transactional(readOnly = true)
public class CulturalOfferService {
	
	@Autowired
	private CulturalOfferRepository culturalOfferRepository;
	
	@Autowired
	private ImageService imageService;
	
	@Transactional(readOnly = true) 
	public boolean hasName(UniqueCheckDTO param) {
		return this.culturalOfferRepository.hasName(param.getId(), param.getName()) != null;
	}	
	
	@Transactional(readOnly = true)
	public List<String> filterNames(String filter){
		return this.culturalOfferRepository.filterNames(filter);
	}
	
	@Transactional(readOnly = true)
	public List<String> filterLocations(String filter){
		return this.culturalOfferRepository.filterLocations(filter);
	}	
	
	@Transactional(readOnly = true)
	public List<String> filterTypes(String filter){
		return this.culturalOfferRepository.filterTypes(filter);
	}	

	@Transactional(readOnly = true)
	public Page<CulturalOffer> filter(FilterParamsDTO filters, Pageable pageable){
		return this.culturalOfferRepository.filter(filters.getName(), filters.getLocation(), filters.getType(), pageable);
	}

	@Transactional(readOnly = false)
	public void delete(long id) {
		this.culturalOfferRepository.deleteById(id);
	}

	@Transactional(readOnly = false)
	public CulturalOffer save(CulturalOffer culturalOffer, MultipartFile upload) {
		if (upload != null) {
			Image image = new Image(this.imageService.store(upload));
			culturalOffer.setImage(image.getPath());
			this.imageService.save(image);
		}
		return this.culturalOfferRepository.save(culturalOffer);
	}
		
}
