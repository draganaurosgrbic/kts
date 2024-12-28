package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.FilterParamsNewsDTO;
import com.example.demo.model.Image;
import com.example.demo.model.News;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.UserFollowingRepository;

@Service
@Transactional(readOnly = true)
public class NewsService {

	@Autowired
	private NewsRepository newsRepository;

	@Autowired
	private UserFollowingRepository userFollowingRepository;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private EmailService emailService;

	@Transactional(readOnly = true)
	public Page<News> filter(long culturalOfferId, FilterParamsNewsDTO filters, Pageable pageable) {
		return this.newsRepository.filter(culturalOfferId, filters.getStartDate(), filters.getEndDate(), pageable);
	}
	
	@Transactional(readOnly = false)
	public void delete(long id) {
		this.newsRepository.deleteById(id);
	}

	@Transactional(readOnly = false)
	public News save(News news, List<MultipartFile> uploads) {
		if (uploads != null) {
			uploads.stream().forEach(upload -> {
				Image image = new Image(this.imageService.store(upload));
				news.addImage(image);
				this.imageService.save(image);
			});
		}
		this.userFollowingRepository.subscribedEmails(news.getCulturalOffer().getId()).stream().forEach(emailAddress -> {
			Email email = new Email(emailAddress, "News about '" + news.getCulturalOffer().getName() + "'", news.getText(), news.getImages());
			this.emailService.sendEmailWithAttachments(email);
		});
		return this.newsRepository.save(news);
	}
	
}
