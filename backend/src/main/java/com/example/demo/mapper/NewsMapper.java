package com.example.demo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.NewsDTO;
import com.example.demo.dto.NewsUploadDTO;
import com.example.demo.model.News;
import com.example.demo.repository.CulturalOfferRepository;
import com.example.demo.repository.ImageRepository;

@Component
public class NewsMapper {

	@Autowired
	private CulturalOfferRepository culturalOfferRepository;

	@Autowired
	private ImageRepository imageRepository;

	public List<NewsDTO> map(List<News> news) {
		return news.stream().map(NewsDTO::new).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public News map(NewsUploadDTO newsDTO) {
		News news = new News();
		news.setId(newsDTO.getId());
		news.setCulturalOffer(this.culturalOfferRepository.findById(newsDTO.getCulturalOfferId()).orElse(null));
		news.setText(newsDTO.getText());
		if (newsDTO.getImagePaths() != null) {
			newsDTO.getImagePaths().stream().forEach(image -> news.addImage(this.imageRepository.findByPath(image)));
		}
		return news;
	}

}
