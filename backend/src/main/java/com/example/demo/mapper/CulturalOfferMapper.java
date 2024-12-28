package com.example.demo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CulturalOfferDTO;
import com.example.demo.dto.CulturalOfferUploadDTO;
import com.example.demo.model.CulturalOffer;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.TypeRepository;
import com.example.demo.service.UserService;

@Component
public class CulturalOfferMapper {

	@Autowired
	private TypeRepository typeRepository;

	@Autowired
	private CommentRepository commentRepository;
			
	@Autowired
	private UserService userService;
	
	@Transactional(readOnly = true)
	public List<CulturalOfferDTO> map(List<CulturalOffer> culturalOffers){
		return culturalOffers.stream().map(culturalOffer -> {
			CulturalOfferDTO culturalOfferDTO = new CulturalOfferDTO(culturalOffer);
			culturalOfferDTO.setTotalRate(this.commentRepository.totalRate(culturalOffer.getId()));
			culturalOfferDTO.setFollowed(this.userService.userIsFollowing(culturalOffer.getId()));
			return culturalOfferDTO;
		}).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public CulturalOffer map(CulturalOfferUploadDTO culturalOfferDTO) {
		CulturalOffer culturalOffer = new CulturalOffer();
		culturalOffer.setId(culturalOfferDTO.getId());
		culturalOffer.setType(this.typeRepository.findByName(culturalOfferDTO.getType()));
		culturalOffer.setName(culturalOfferDTO.getName());
		culturalOffer.setLocation(culturalOfferDTO.getLocation());
		culturalOffer.setLat(culturalOfferDTO.getLat());
		culturalOffer.setLng(culturalOfferDTO.getLng());
		culturalOffer.setDescription(culturalOfferDTO.getDescription());
		culturalOffer.setImage(culturalOfferDTO.getImagePath());
		return culturalOffer;
	}
	
	@Transactional
	public CulturalOfferDTO map(CulturalOffer culturalOffer) {
		CulturalOfferDTO culturalOfferDTO = new CulturalOfferDTO(culturalOffer);
		culturalOfferDTO.setTotalRate(this.commentRepository.totalRate(culturalOffer.getId()));
		culturalOfferDTO.setFollowed(this.userService.userIsFollowing(culturalOffer.getId()));
		return culturalOfferDTO;
	}

}
