package com.example.demo.service.unit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constants.NewsConstants;
import com.example.demo.constants.CulturalOfferConstants;
import com.example.demo.constants.MainConstants;
import com.example.demo.dto.FilterParamsNewsDTO;
import com.example.demo.model.CulturalOffer;
import com.example.demo.model.News;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.UserFollowingRepository;
import com.example.demo.service.NewsService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NewsServiceTest {

	@Autowired
	private NewsService newsService;

	@MockBean
	private NewsRepository newsRepository;

	@MockBean
	private UserFollowingRepository userFollowingRepository;
	
	private Pageable pageableTotal = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.TOTAL_SIZE);
	private Pageable pageablePart = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.PART_SIZE);
	private Pageable pageableNonExisting = PageRequest.of(MainConstants.ONE_SIZE, MainConstants.TOTAL_SIZE);
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	@Test
	public void testListMore() {
		FilterParamsNewsDTO filters = new FilterParamsNewsDTO();
		Mockito.when(this.newsRepository.filter(
				CulturalOfferConstants.ID_THREE, 
				filters.getStartDate(), 
				filters.getEndDate(), 
				this.pageableTotal))
		.thenReturn(new PageImpl<>(List.of(this.filterNews(4), this.filterNews(2), this.filterNews(3))));
		List<News> news = this.newsService
				.filter(CulturalOfferConstants.ID_THREE, filters, this.pageableTotal).getContent();
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
	}

	@Test
	public void testListMorePaginated() {
		FilterParamsNewsDTO filters = new FilterParamsNewsDTO();
		Mockito.when(this.newsRepository.filter(
				CulturalOfferConstants.ID_THREE, 
				filters.getStartDate(), 
				filters.getEndDate(), 
				this.pageablePart))
		.thenReturn(new PageImpl<>(List.of(this.filterNews(4), this.filterNews(2))));
		List<News> news = this.newsService
				.filter(CulturalOfferConstants.ID_THREE, filters, this.pageablePart).getContent();
		assertEquals(MainConstants.PART_SIZE, news.size());
		assertEquals(NewsConstants.ID_FOUR, news.get(0).getId());
		assertEquals(NewsConstants.DATE_FOUR, this.format.format(news.get(0).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_FOUR, news.get(0).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(0).getCulturalOffer().getId());
		assertEquals(NewsConstants.ID_TWO, news.get(1).getId());
		assertEquals(NewsConstants.DATE_TWO, this.format.format(news.get(1).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_TWO, news.get(1).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(1).getCulturalOffer().getId());
	}

	@Test
	public void testListMoreNonExistingPage() {
		FilterParamsNewsDTO filters = new FilterParamsNewsDTO();
		Mockito.when(this.newsRepository.filter(
				CulturalOfferConstants.ID_THREE, 
				filters.getStartDate(), 
				filters.getEndDate(), 
				this.pageableNonExisting))
		.thenReturn(new PageImpl<>(List.of()));
		List<News> news = 
				this.newsService
				.filter(CulturalOfferConstants.ID_THREE, filters, this.pageableNonExisting).getContent();
		assertTrue(news.isEmpty());
	}

	@Test
	public void testListOne() {
		FilterParamsNewsDTO filters = new FilterParamsNewsDTO();
		Mockito.when(this.newsRepository.filter(
				CulturalOfferConstants.ID_TWO, 
				filters.getStartDate(), 
				filters.getEndDate(), 
				this.pageableTotal))
		.thenReturn(new PageImpl<>(List.of(this.filterNews(1))));
		List<News> news = this.newsService
				.filter(CulturalOfferConstants.ID_TWO, filters, this.pageableTotal).getContent();
		assertEquals(MainConstants.ONE_SIZE, news.size());
		assertEquals(NewsConstants.ID_ONE, news.get(0).getId());
		assertEquals(NewsConstants.DATE_ONE, this.format.format(news.get(0).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_ONE, news.get(0).getText());
		assertEquals(CulturalOfferConstants.ID_TWO, news.get(0).getCulturalOffer().getId());
	}

	@Test
	public void testListNone() {
		FilterParamsNewsDTO filters = new FilterParamsNewsDTO();
		Mockito.when(this.newsRepository.filter(
				MainConstants.NON_EXISTING_ID, 
				filters.getStartDate(), 
				filters.getEndDate(), 
				this.pageableTotal))
		.thenReturn(new PageImpl<>(List.of()));
		List<News> news = this.newsService
				.filter(MainConstants.NON_EXISTING_ID, filters, this.pageableTotal).getContent();
		assertTrue(news.isEmpty());
	}

	@Test
	public void testListMoreDates() throws ParseException {
		FilterParamsNewsDTO filters = new FilterParamsNewsDTO(
				this.format.parse(NewsConstants.START_DATE_MORE),
				this.format.parse(NewsConstants.END_DATE_MORE));
		Mockito.when(this.newsRepository.filter(
				CulturalOfferConstants.ID_THREE, 
				filters.getStartDate(), 
				filters.getEndDate(), 
				this.pageableTotal))
		.thenReturn(new PageImpl<>(List.of(this.filterNews(4), this.filterNews(2), this.filterNews(3))));
		List<News> news = 
				this.newsService
				.filter(CulturalOfferConstants.ID_THREE, filters, this.pageableTotal)
				.getContent();
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
	}

	@Test
	public void testListMoreDatesPaginated() throws ParseException {
		FilterParamsNewsDTO filters = new FilterParamsNewsDTO(
				this.format.parse(NewsConstants.START_DATE_MORE),
				this.format.parse(NewsConstants.END_DATE_MORE));
		Mockito.when(this.newsRepository.filter(
				CulturalOfferConstants.ID_THREE, 
				filters.getStartDate(), 
				filters.getEndDate(), 
				this.pageablePart))
		.thenReturn(new PageImpl<>(List.of(this.filterNews(4), this.filterNews(2))));
		List<News> news = 
				this.newsService
				.filter(CulturalOfferConstants.ID_THREE, filters, this.pageablePart)
				.getContent();
		assertEquals(MainConstants.PART_SIZE, news.size());
		assertEquals(NewsConstants.ID_FOUR, news.get(0).getId());
		assertEquals(NewsConstants.DATE_FOUR, this.format.format(news.get(0).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_FOUR, news.get(0).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(0).getCulturalOffer().getId());
		assertEquals(NewsConstants.ID_TWO, news.get(1).getId());
		assertEquals(NewsConstants.DATE_TWO, this.format.format(news.get(1).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_TWO, news.get(1).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(1).getCulturalOffer().getId());
	}
	
	@Test
	public void testListMoreDatesNonExistingPage() throws ParseException {
		FilterParamsNewsDTO filters = new FilterParamsNewsDTO(
				this.format.parse(NewsConstants.START_DATE_MORE),
				this.format.parse(NewsConstants.END_DATE_MORE));
		Mockito.when(this.newsRepository.filter(
				CulturalOfferConstants.ID_THREE, 
				filters.getStartDate(), 
				filters.getEndDate(), 
				this.pageableNonExisting))
		.thenReturn(new PageImpl<>(List.of()));
		List<News> news = 
				this.newsService
				.filter(CulturalOfferConstants.ID_THREE, filters, this.pageableNonExisting)
				.getContent();
		assertTrue(news.isEmpty());
	}
	
	@Test
	public void testListOneDates() throws ParseException {
		FilterParamsNewsDTO filters = new FilterParamsNewsDTO(
				this.format.parse(NewsConstants.START_DATE_ONE),
				this.format.parse(NewsConstants.END_DATE_ONE));
		Mockito.when(this.newsRepository.filter(
				CulturalOfferConstants.ID_THREE, 
				filters.getStartDate(), 
				filters.getEndDate(), 
				this.pageableTotal))
		.thenReturn(new PageImpl<>(List.of(this.filterNews(4))));
		List<News> news = 
				this.newsService
				.filter(CulturalOfferConstants.ID_THREE, filters, this.pageableTotal)
				.getContent();
		assertEquals(MainConstants.ONE_SIZE, news.size());
		assertEquals(NewsConstants.ID_FOUR, news.get(0).getId());
		assertEquals(NewsConstants.DATE_FOUR, this.format.format(news.get(0).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_FOUR, news.get(0).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(0).getCulturalOffer().getId());
	}

	@Test
	public void testListNoneDates() throws ParseException {
		FilterParamsNewsDTO filters = new FilterParamsNewsDTO(
				this.format.parse(NewsConstants.WRONG_START),
				this.format.parse(NewsConstants.WRONG_END));
		Mockito.when(this.newsRepository.filter(
				MainConstants.NON_EXISTING_ID, 
				filters.getStartDate(), 
				filters.getEndDate(), 
				this.pageableTotal))
		.thenReturn(new PageImpl<>(List.of()));
		List<News> news = 
				this.newsService
				.filter(MainConstants.NON_EXISTING_ID, filters, this.pageableTotal)
				.getContent();
		assertTrue(news.isEmpty());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testDeleteExisting() {
		Mockito.when(this.newsRepository.count())
		.thenReturn((long) (MainConstants.TOTAL_SIZE + 1));
		long size = this.newsRepository.count();
		this.newsService.delete(NewsConstants.ID_ONE);
		Mockito.when(this.newsRepository.count())
		.thenReturn(size - 1);
		assertEquals(size - 1, this.newsRepository.count());
		Mockito.when(this.newsRepository.findById(NewsConstants.ID_ONE))
		.thenReturn(Optional.empty());
		assertNull(this.newsRepository.findById(NewsConstants.ID_ONE).orElse(null));
	}

	@Test(expected = EmptyResultDataAccessException.class)
	@Transactional
	@Rollback(true)
	public void testDeleteNonExisting() {
		Mockito.doThrow(EmptyResultDataAccessException.class)
		.when(this.newsRepository).deleteById(MainConstants.NON_EXISTING_ID);
		this.newsService.delete(MainConstants.NON_EXISTING_ID);
	}
	
	@Test
	public void testAddValid() {
		Mockito.when(this.newsRepository.count())
		.thenReturn((long) (MainConstants.TOTAL_SIZE + 1));
		long size = this.newsRepository.count();
		News news = this.testingNews();
		Mockito.when(this.newsRepository.save(news)).thenReturn(news);
		List<String> emails = new ArrayList<String>();
		Mockito.when(this.userFollowingRepository.subscribedEmails(news.getCulturalOffer().getId()))
		.thenReturn(emails);
		news = this.newsService.save(news, null);
		Mockito.when(this.newsRepository.count())
		.thenReturn(size + 1);
		assertEquals(size + 1, this.newsRepository.count());
		assertEquals(CulturalOfferConstants.ID_THREE, news.getCulturalOffer().getId());
		assertEquals(this.format.format(new Date()), this.format.format(news.getCreatedAt()));
		assertEquals(NewsConstants.TEXT_ONE, news.getText());
	}

	@Test(expected = NullPointerException.class)
	public void testAddNullOffer() {
		News news = this.testingNews();
		news.setCulturalOffer(null);
		this.newsService.save(news, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddNullDate() {
		News news = this.testingNews();
		news.setCreatedAt(null);
		Mockito.when(this.newsRepository.save(news))
		.thenThrow(ConstraintViolationException.class);
		this.newsService.save(news, null);
	}

	@Test(expected = ConstraintViolationException.class)
	public void testAddNullText() {
		News news = this.testingNews();
		news.setText(null);
		Mockito.when(this.newsRepository.save(news))
		.thenThrow(ConstraintViolationException.class);
		this.newsService.save(news, null);
	}

	@Test(expected = ConstraintViolationException.class)
	public void testAddEmptyText() {
		News news = this.testingNews();
		news.setText("");
		Mockito.when(this.newsRepository.save(news))
		.thenThrow(ConstraintViolationException.class);
		this.newsService.save(news, null);
	}

	@Test(expected = ConstraintViolationException.class)
	public void testAddBlankText() {
		News news = this.testingNews();
		news.setText("  ");
		Mockito.when(this.newsRepository.save(news))
		.thenThrow(ConstraintViolationException.class);
		this.newsService.save(news, null);
	}

	@Test
	public void testUpdate() {
		Mockito.when(this.newsRepository.count())
		.thenReturn((long) (MainConstants.TOTAL_SIZE + 1));
		long size = this.newsRepository.count();
		News news = this.testingNews();
		news.setId(NewsConstants.ID_ONE);
		Mockito.when(this.newsRepository.save(news))
		.thenReturn(news);
		List<String> emails = new ArrayList<String>();
		Mockito.when(this.userFollowingRepository.subscribedEmails(news.getCulturalOffer().getId()))
		.thenReturn(emails);
		news = this.newsService.save(news, null);
		assertEquals(size, this.newsRepository.count());
		assertEquals(NewsConstants.ID_ONE, news.getId());
		assertEquals(CulturalOfferConstants.ID_THREE, news.getCulturalOffer().getId());
		assertEquals(this.format.format(new Date()), this.format.format(news.getCreatedAt()));
		assertEquals(NewsConstants.TEXT_ONE, news.getText());
	}

	@Test(expected = NullPointerException.class)
	public void testUpdateNullOffer() {
		News news = this.testingNews();
		news.setId(NewsConstants.ID_ONE);
		news.setCulturalOffer(null);
		Mockito.when(this.newsRepository.save(news))
		.thenThrow(NullPointerException.class);
		this.newsService.save(news, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateNullDate() {
		News news = this.testingNews();
		news.setId(NewsConstants.ID_ONE);
		news.setCreatedAt(null);
		Mockito.when(this.newsRepository.save(news))
		.thenThrow(ConstraintViolationException.class);
		this.newsService.save(news, null);
	}

	@Test(expected = ConstraintViolationException.class)
	public void testUpdateNullText() {
		News news = this.testingNews();
		news.setId(NewsConstants.ID_ONE);
		news.setText(null);
		Mockito.when(this.newsRepository.save(news))
		.thenThrow(ConstraintViolationException.class);
		this.newsService.save(news, null);
	}

	@Test(expected = ConstraintViolationException.class)
	public void testUpdateEmptyText() {
		News news = this.testingNews();
		news.setId(NewsConstants.ID_ONE);
		news.setText("");
		Mockito.when(this.newsRepository.save(news))
		.thenThrow(ConstraintViolationException.class);
		this.newsService.save(news, null);
	}

	@Test(expected = ConstraintViolationException.class)
	public void testUpdateBlankText() {
		News news = this.testingNews();
		news.setId(NewsConstants.ID_ONE);
		news.setText("  ");
		Mockito.when(this.newsRepository.save(news))
		.thenThrow(ConstraintViolationException.class);
		this.newsService.save(news, null);
	}
	
	public News testingNews() {
		News news = new News();
		CulturalOffer offer = new CulturalOffer();
		offer.setId(CulturalOfferConstants.ID_THREE);
		news.setCulturalOffer(offer);
		news.setText(NewsConstants.TEXT_ONE);
		return news;
	}
	
	private News filterNews(int index) {
		if (index == 1) {
			News news = new News();
			news.setId(NewsConstants.ID_ONE);
			CulturalOffer offer = new CulturalOffer();
			offer.setId(CulturalOfferConstants.ID_TWO);
			news.setCulturalOffer(offer);
			try {
				news.setCreatedAt(this.format.parse(NewsConstants.DATE_ONE));				
			}catch(Exception e) {};
			news.setText(NewsConstants.TEXT_ONE);
			return news;
		}
		if (index == 2) {
			News news = new News();
			news.setId(NewsConstants.ID_TWO);
			CulturalOffer offer = new CulturalOffer();
			offer.setId(CulturalOfferConstants.ID_THREE);
			news.setCulturalOffer(offer);
			try {
				news.setCreatedAt(this.format.parse(NewsConstants.DATE_TWO));				
			}catch(Exception e) {};
			news.setText(NewsConstants.TEXT_TWO);
			return news;
		}
		if (index == 3) {
			News news = new News();
			news.setId(NewsConstants.ID_THREE);
			CulturalOffer offer = new CulturalOffer();
			offer.setId(CulturalOfferConstants.ID_THREE);
			news.setCulturalOffer(offer);
			try {
				news.setCreatedAt(this.format.parse(NewsConstants.DATE_THREE));				
			}catch(Exception e) {};
			news.setText(NewsConstants.TEXT_THREE);
			return news;
		}
		News news = new News();
		news.setId(NewsConstants.ID_FOUR);
		CulturalOffer offer = new CulturalOffer();
		offer.setId(CulturalOfferConstants.ID_THREE);
		news.setCulturalOffer(offer);
		try {
			news.setCreatedAt(this.format.parse(NewsConstants.DATE_FOUR));				
		}catch(Exception e) {};
		news.setText(NewsConstants.TEXT_FOUR);
		return news;
	}

}
