package com.example.demo.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.constants.CulturalOfferConstants;
import com.example.demo.constants.MainConstants;
import com.example.demo.constants.NewsConstants;
import com.example.demo.model.News;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NewsRepositoryTest {

	@Autowired
	private NewsRepository newsRepository;

	private Pageable pageableTotal = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.TOTAL_SIZE);
	private Pageable pageablePart = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.PART_SIZE);
	private Pageable pageableNonExisting = PageRequest.of(MainConstants.ONE_SIZE, MainConstants.TOTAL_SIZE);
	private SimpleDateFormat format = new SimpleDateFormat(MainConstants.DATE_FORMAT);

	@Test
	public void testFilterMore() {
		Page<News> page = 
				this.newsRepository
				.filter(CulturalOfferConstants.ID_THREE, null, null, this.pageableTotal);
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
	public void testFilterMorePaginated() {
		Page<News> page = 
				this.newsRepository
				.filter(CulturalOfferConstants.ID_THREE, null, null, this.pageablePart);
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
	public void testFilterMoreNonExistingPage() {
		Page<News> page = 
				this.newsRepository
				.filter(CulturalOfferConstants.ID_ONE, null, null, this.pageableNonExisting);
		List<News> news = page.getContent();
		assertTrue(news.isEmpty());
		assertFalse(page.isFirst());
		assertTrue(page.isLast());
	}

	@Test
	public void testFilterMoreDates() throws ParseException {
		Page<News> page = 
				this.newsRepository
				.filter(CulturalOfferConstants.ID_THREE, 
						this.format.parse(NewsConstants.START_DATE_MORE),
						this.format.parse(NewsConstants.END_DATE_MORE), 
						this.pageableTotal);
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
	public void testFilterMoreDatesPaginated() throws ParseException {
		Page<News> page = 
				this.newsRepository
				.filter(CulturalOfferConstants.ID_THREE, 
						this.format.parse(NewsConstants.START_DATE_MORE),
						this.format.parse(NewsConstants.END_DATE_MORE), 
						this.pageablePart);
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
	public void testFilterMoreDatesNonExistingPage() throws ParseException {
		Page<News> page = 
				this.newsRepository
				.filter(CulturalOfferConstants.ID_THREE, 
						this.format.parse(NewsConstants.START_DATE_MORE),
						this.format.parse(NewsConstants.END_DATE_MORE), 
						this.pageableNonExisting);
		List<News> news = page.getContent();
		assertTrue(news.isEmpty());
		assertFalse(page.isFirst());
		assertTrue(page.isLast());
	}
	
	@Test
	public void testFilterOne() {
		Page<News> page = 
				this.newsRepository.filter(CulturalOfferConstants.ID_TWO, null, null, this.pageableTotal);
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
	public void testFilterOneDates() throws ParseException {
		Page<News> page = 
				this.newsRepository
				.filter(CulturalOfferConstants.ID_THREE, 
						this.format.parse(NewsConstants.START_DATE_ONE),
						this.format.parse(NewsConstants.END_DATE_ONE), 
						this.pageableTotal);
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
	public void testFilterNone() {
		Page<News> page = 
				this.newsRepository.filter(MainConstants.NON_EXISTING_ID, null, null, this.pageableTotal);
		List<News> news = page.getContent();
		assertTrue(news.isEmpty());
		assertTrue(page.isFirst());
		assertTrue(page.isLast());
	}

	@Test
	public void testFilterNoneDates() throws ParseException {
		Page<News> page = 
				this.newsRepository
				.filter(CulturalOfferConstants.ID_TWO, 
						this.format.parse(NewsConstants.WRONG_START),
						this.format.parse(NewsConstants.WRONG_END), 
						this.pageableTotal);
		List<News> news = page.getContent();
		assertTrue(news.isEmpty());
		assertTrue(page.isFirst());
		assertTrue(page.isLast());
	}
	
}
