package com.example.demo.service.integration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constants.CommentConstants;
import com.example.demo.constants.CulturalOfferConstants;
import com.example.demo.constants.MainConstants;
import com.example.demo.constants.NewsConstants;
import com.example.demo.dto.FilterParamsNewsDTO;
import com.example.demo.model.News;
import com.example.demo.repository.CulturalOfferRepository;
import com.example.demo.repository.NewsRepository;
import com.example.demo.service.NewsService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NewsServiceTest {

	@Autowired
	private NewsService newsService;

	@Autowired
	private NewsRepository newsRepository;

	@Autowired
	private CulturalOfferRepository culturalOfferRepository;

	private Pageable pageableTotal = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.TOTAL_SIZE);
	private Pageable pageablePart = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.PART_SIZE);
	private Pageable pageableNonExisting = PageRequest.of(MainConstants.ONE_SIZE, MainConstants.TOTAL_SIZE);
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	@Test
	public void testListMore() {
		Page<News> page = this.newsService
				.filter(CulturalOfferConstants.ID_THREE, new FilterParamsNewsDTO(), this.pageableTotal);
		List<News> news = page.getContent();
		assertEquals(MainConstants.TOTAL_SIZE, news.size());
		assertEquals(NewsConstants.ID_FOUR, news.get(0).getId());
		assertEquals(NewsConstants.DATE_FOUR, this.format.format(news.get(0).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_FOUR, news.get(0).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(0).getCulturalOffer().getId());
		assertEquals(NewsConstants.ID_TWO, news.get(1).getId());
		assertEquals(NewsConstants.DATE_TWO, this.format.format(news.get(1).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_TWO, news.get(1).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(1).getCulturalOffer().getId());
		assertEquals(NewsConstants.ID_THREE, news.get(2).getId());
		assertEquals(NewsConstants.DATE_THREE, this.format.format(news.get(2).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_THREE, news.get(2).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(2).getCulturalOffer().getId());
		assertTrue(page.isFirst());
		assertTrue(page.isLast());
	}

	@Test
	public void testListMorePaginated() {
		Page<News> page = this.newsService
				.filter(CulturalOfferConstants.ID_THREE, new FilterParamsNewsDTO(), this.pageablePart);
		List<News> news = page.getContent();
		assertEquals(MainConstants.PART_SIZE, news.size());
		assertEquals(NewsConstants.ID_FOUR, news.get(0).getId());
		assertEquals(NewsConstants.DATE_FOUR, this.format.format(news.get(0).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_FOUR, news.get(0).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(0).getCulturalOffer().getId());
		assertEquals(NewsConstants.ID_TWO, news.get(1).getId());
		assertEquals(NewsConstants.DATE_TWO, this.format.format(news.get(1).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_TWO, news.get(1).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(1).getCulturalOffer().getId());
		assertTrue(page.isFirst());
		assertFalse(page.isLast());
	}

	@Test
	public void testListMoreNonExistingPage() {
		Page<News> page = this.newsService
				.filter(CulturalOfferConstants.ID_THREE, new FilterParamsNewsDTO(), this.pageableNonExisting);
		List<News> news = page.getContent();
		assertTrue(news.isEmpty());
		assertFalse(page.isFirst());
		assertTrue(page.isLast());
	}

	@Test
	public void testListOne() {
		Page<News> page = this.newsService
				.filter(CulturalOfferConstants.ID_TWO, new FilterParamsNewsDTO(), this.pageableTotal);
		List<News> news = page.getContent();
		assertEquals(MainConstants.ONE_SIZE, news.size());
		assertEquals(NewsConstants.ID_ONE, news.get(0).getId());
		assertEquals(NewsConstants.DATE_ONE, this.format.format(news.get(0).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_ONE, news.get(0).getText());
		assertEquals(CulturalOfferConstants.ID_TWO, news.get(0).getCulturalOffer().getId());
		assertTrue(page.isFirst());
		assertTrue(page.isLast());
	}

	@Test
	public void testListNone() {
		Page<News> page = this.newsService
				.filter(MainConstants.NON_EXISTING_ID, new FilterParamsNewsDTO(), this.pageableTotal);
		List<News> news = page.getContent();
		assertTrue(news.isEmpty());
		assertTrue(page.isFirst());
		assertTrue(page.isLast());
	}

	@Test
	public void testListMoreDates() throws ParseException {
		FilterParamsNewsDTO filters = new FilterParamsNewsDTO(
				this.format.parse(NewsConstants.START_DATE_MORE),
				this.format.parse(NewsConstants.END_DATE_MORE));
		Page<News> page = this.newsService
				.filter(CulturalOfferConstants.ID_THREE, filters, this.pageableTotal);
		List<News> news = page.getContent();
		assertEquals(MainConstants.TOTAL_SIZE, news.size());
		assertEquals(NewsConstants.ID_FOUR, news.get(0).getId());
		assertEquals(NewsConstants.DATE_FOUR, this.format.format(news.get(0).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_FOUR, news.get(0).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(0).getCulturalOffer().getId());
		assertEquals(NewsConstants.ID_TWO, news.get(1).getId());
		assertEquals(NewsConstants.DATE_TWO, this.format.format(news.get(1).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_TWO, news.get(1).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(1).getCulturalOffer().getId());
		assertEquals(NewsConstants.ID_THREE, news.get(2).getId());
		assertEquals(NewsConstants.DATE_THREE, this.format.format(news.get(2).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_THREE, news.get(2).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(2).getCulturalOffer().getId());
		assertTrue(page.isFirst());
		assertTrue(page.isLast());
	}

	@Test
	public void testListMoreDatesPaginated() throws ParseException {
		FilterParamsNewsDTO filters = new FilterParamsNewsDTO(
				this.format.parse(NewsConstants.START_DATE_MORE),
				this.format.parse(NewsConstants.END_DATE_MORE));
		Page<News> page = this.newsService
				.filter(CulturalOfferConstants.ID_THREE, filters, this.pageablePart);
		List<News> news = page.getContent();
		assertEquals(MainConstants.PART_SIZE, news.size());
		assertEquals(NewsConstants.ID_FOUR, news.get(0).getId());
		assertEquals(NewsConstants.DATE_FOUR, this.format.format(news.get(0).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_FOUR, news.get(0).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(0).getCulturalOffer().getId());
		assertEquals(NewsConstants.ID_TWO, news.get(1).getId());
		assertEquals(NewsConstants.DATE_TWO, this.format.format(news.get(1).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_TWO, news.get(1).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(1).getCulturalOffer().getId());
		assertTrue(page.isFirst());
		assertFalse(page.isLast());
	}
	
	@Test
	public void testListMoreDatesNonExistingPage() throws ParseException {
		FilterParamsNewsDTO filters = new FilterParamsNewsDTO(
				this.format.parse(NewsConstants.START_DATE_MORE),
				this.format.parse(NewsConstants.END_DATE_MORE));
		Page<News> page = this.newsService
				.filter(CulturalOfferConstants.ID_THREE, filters, this.pageableNonExisting);
		List<News> news = page.getContent();
		assertTrue(news.isEmpty());
		assertFalse(page.isFirst());
		assertTrue(page.isLast());
	}
	
	@Test
	public void testListOneDates() throws ParseException {
		FilterParamsNewsDTO filters = new FilterParamsNewsDTO(
				this.format.parse(NewsConstants.START_DATE_ONE),
				this.format.parse(NewsConstants.END_DATE_ONE));
		Page<News> page = this.newsService
				.filter(CulturalOfferConstants.ID_THREE, filters, this.pageableTotal);
		List<News> news = page.getContent();
		assertEquals(MainConstants.ONE_SIZE, news.size());
		assertEquals(NewsConstants.ID_FOUR, news.get(0).getId());
		assertEquals(NewsConstants.DATE_FOUR, this.format.format(news.get(0).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_FOUR, news.get(0).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(0).getCulturalOffer().getId());
		assertTrue(page.isFirst());
		assertTrue(page.isLast());
	}

	@Test
	public void testListNoneDates() throws ParseException {
		FilterParamsNewsDTO filters = new FilterParamsNewsDTO(
				this.format.parse(NewsConstants.WRONG_START),
				this.format.parse(NewsConstants.WRONG_END));
		Page<News> page = this.newsService
				.filter(MainConstants.NON_EXISTING_ID, filters, this.pageableTotal);
		List<News> news = page.getContent();
		assertTrue(news.isEmpty());
		assertTrue(page.isFirst());
		assertTrue(page.isLast());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testDeleteExisting() {
		long size = this.newsRepository.count();
		this.newsService.delete(NewsConstants.ID_ONE);
		assertEquals(size - 1, this.newsRepository.count());
		assertNull(this.newsRepository.findById(CommentConstants.ID_ONE).orElse(null));
	}

	@Test(expected = EmptyResultDataAccessException.class)
	@Transactional
	@Rollback(true)
	public void testDeleteNonExisting() {
		this.newsService.delete(MainConstants.NON_EXISTING_ID);
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testAddValid() {
		long size = this.newsRepository.count();
		News news = this.testingNews();
		news = this.newsService.save(news, null);
		assertEquals(size + 1, this.newsRepository.count());
		assertEquals(CulturalOfferConstants.ID_THREE, news.getCulturalOffer().getId());
		assertEquals(this.format.format(new Date()), this.format.format(news.getCreatedAt()));
		assertEquals(NewsConstants.TEXT_ONE, news.getText());
	}

	@Test(expected = NullPointerException.class)
	@Transactional
	@Rollback(true)
	public void testAddNullOffer() {
		News news = this.testingNews();
		news.setCulturalOffer(null);
		this.newsService.save(news, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddNullDate() {
		News news = this.testingNews();
		news.setCreatedAt(null);
		this.newsService.save(news, null);
	}

	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddNullText() {
		News news = this.testingNews();
		news.setText(null);
		this.newsService.save(news, null);
	}

	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddEmptyText() {
		News news = this.testingNews();
		news.setText("");
		this.newsService.save(news, null);
	}

	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddBlankText() {
		News news = this.testingNews();
		news.setText("  ");
		this.newsService.save(news, null);
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testUpdateValid() {
		long size = this.newsRepository.count();
		News news = this.testingNews();
		news.setId(NewsConstants.ID_ONE);
		news = this.newsService.save(news, null);
		assertEquals(size, this.newsRepository.count());
		assertEquals(NewsConstants.ID_ONE, news.getId());
		assertEquals(CulturalOfferConstants.ID_THREE, news.getCulturalOffer().getId());
		assertEquals(this.format.format(new Date()), this.format.format(news.getCreatedAt()));
		assertEquals(NewsConstants.TEXT_ONE, news.getText());
	}

	@Test(expected = NullPointerException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateNullOffer() {
		News news = this.testingNews();
		news.setId(NewsConstants.ID_ONE);
		news.setCulturalOffer(null);
		this.newsService.save(news, null);
		this.newsRepository.count();
	}

	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateNullDate() {
		News news = this.testingNews();
		news.setId(NewsConstants.ID_ONE);
		news.setCreatedAt(null);
		this.newsService.save(news, null);
		this.newsRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateNullText() {
		News news = this.testingNews();
		news.setId(NewsConstants.ID_ONE);
		news.setText(null);
		this.newsService.save(news, null);
		this.newsRepository.count();
	}

	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateEmptyText() {
		News news = this.testingNews();
		news.setId(NewsConstants.ID_ONE);
		news.setText("");
		this.newsService.save(news, null);
		this.newsRepository.count();
	}

	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateBlankText() {
		News news = this.testingNews();
		news.setId(NewsConstants.ID_ONE);
		news.setText("  ");
		this.newsService.save(news, null);
		this.newsRepository.count();
	}
	
	public News testingNews() {
		News news = new News();
		news.setCulturalOffer(this.culturalOfferRepository.findById(CulturalOfferConstants.ID_THREE).orElse(null));
		news.setText(NewsConstants.TEXT_ONE);
		return news;
	}

}
