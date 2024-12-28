package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constants.Constants;
import com.example.demo.dto.FilterParamsNewsDTO;
import com.example.demo.dto.NewsDTO;
import com.example.demo.dto.NewsUploadDTO;
import com.example.demo.mapper.NewsMapper;
import com.example.demo.model.News;
import com.example.demo.service.NewsService;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("permitAll()")
public class NewsController {

	@Autowired
	private NewsService newsService;

	@Autowired
	private NewsMapper newsMapper;
	
	@PostMapping(value = "/api/cultural_offers/{culturalOfferId}/filter_news")
	public ResponseEntity<List<NewsDTO>> list(@PathVariable long culturalOfferId, @RequestParam int page, @RequestParam int size, @RequestBody FilterParamsNewsDTO filters, HttpServletResponse response) {
		Pageable pageable = PageRequest.of(page, size);
		Page<News> news = this.newsService.filter(culturalOfferId, filters, pageable);
		response.setHeader(Constants.ENABLE_HEADER, Constants.FIRST_PAGE_HEADER + ", " + Constants.LAST_PAGE_HEADER);
		response.setHeader(Constants.FIRST_PAGE_HEADER, news.isFirst() + "");
		response.setHeader(Constants.LAST_PAGE_HEADER, news.isLast() + "");
		return new ResponseEntity<>(this.newsMapper.map(news.toList()), HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('admin')")
	@DeleteMapping(value = "/api/news/{id}")
	public ResponseEntity<Void> delete(@PathVariable long id) {
		this.newsService.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('admin')")
	@PostMapping(value = "/api/news")
	public ResponseEntity<NewsDTO> save(@Valid @ModelAttribute NewsUploadDTO newsDTO) {
		return new ResponseEntity<>(new NewsDTO(this.newsService.save(this.newsMapper.map(newsDTO), newsDTO.getImages())), HttpStatus.CREATED);
	}

}
