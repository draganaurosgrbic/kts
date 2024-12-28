package com.example.demo.controller;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import com.example.demo.api.AuthAPI;
import com.example.demo.api.CulturalOfferAPI;
import com.example.demo.api.NewsAPI;
import com.example.demo.constants.Constants;
import com.example.demo.constants.CulturalOfferConstants;
import com.example.demo.constants.MainConstants;
import com.example.demo.constants.NewsConstants;
import com.example.demo.constants.UserConstants;
import com.example.demo.dto.FilterParamsNewsDTO;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.NewsDTO;
import com.example.demo.dto.NewsUploadDTO;
import com.example.demo.dto.ProfileDTO;
import com.example.demo.exception.ExceptionConstants;
import com.example.demo.exception.ExceptionMessage;
import com.example.demo.model.News;
import com.example.demo.repository.CulturalOfferRepository;
import com.example.demo.repository.NewsRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NewsControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;
	private String accessToken;

	@Autowired
	private NewsRepository newsRepository;

	@Autowired
	private CulturalOfferRepository culturalOfferRepository;

	private Pageable pageableTotal = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.TOTAL_SIZE);
	private Pageable pageablePart = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.PART_SIZE);
	private Pageable pageableNonExisting = PageRequest.of(MainConstants.ONE_SIZE, MainConstants.TOTAL_SIZE);
	private SimpleDateFormat format = new SimpleDateFormat(MainConstants.DATE_FORMAT);

	@Before
	public void adminLogin() {
		LoginDTO login = new LoginDTO();
		login.setEmail(UserConstants.EMAIL_TWO);
		login.setPassword(UserConstants.LOGIN_PASSWORD);
		ResponseEntity<ProfileDTO> response = 
				this.restTemplate.postForEntity(
						AuthAPI.API_LOGIN, 
						login, 
						ProfileDTO.class);
		this.accessToken = response.getBody().getAccessToken();
	}

	@Test
	public void testListMore() {
		ResponseEntity<List<NewsDTO>> response = 
				this.restTemplate.exchange(
						NewsAPI.API_FILTER(CulturalOfferConstants.ID_THREE, 
						this.pageableTotal), 
						HttpMethod.POST,
						this.httpEntity(new FilterParamsNewsDTO()), 
						new ParameterizedTypeReference<List<NewsDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<NewsDTO> news = response.getBody();
		assertEquals(MainConstants.TOTAL_SIZE, news.size());
		assertEquals(NewsConstants.ID_FOUR, news.get(0).getId());
		assertEquals(NewsConstants.DATE_FOUR, this.format.format(news.get(0).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_FOUR, news.get(0).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(0).getCulturalOfferId());
		assertEquals(NewsConstants.ID_TWO, news.get(1).getId());
		assertEquals(NewsConstants.DATE_TWO, this.format.format(news.get(1).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_TWO, news.get(1).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(1).getCulturalOfferId());
		assertEquals(NewsConstants.ID_THREE, news.get(2).getId());
		assertEquals(NewsConstants.DATE_THREE, this.format.format(news.get(2).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_THREE, news.get(2).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(2).getCulturalOfferId());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}

	@Test
	public void testListMorePaginated() throws RestClientException, ParseException {
		ResponseEntity<List<NewsDTO>> response = 
				this.restTemplate.exchange(
				NewsAPI.API_FILTER(CulturalOfferConstants.ID_THREE, this.pageablePart), HttpMethod.POST,
				this.httpEntity(new FilterParamsNewsDTO()), 
				new ParameterizedTypeReference<List<NewsDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<NewsDTO> news = response.getBody();
		assertEquals(MainConstants.PART_SIZE, news.size());
		assertEquals(NewsConstants.ID_FOUR, news.get(0).getId());
		assertEquals(NewsConstants.DATE_FOUR, this.format.format(news.get(0).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_FOUR, news.get(0).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(0).getCulturalOfferId());
		assertEquals(NewsConstants.ID_TWO, news.get(1).getId());
		assertEquals(NewsConstants.DATE_TWO, this.format.format(news.get(1).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_TWO, news.get(1).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(1).getCulturalOfferId());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("false", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}

	@Test
	public void testListMoreNonExistingPage() throws RestClientException, ParseException {
		ResponseEntity<List<NewsDTO>> response = 
				this.restTemplate.exchange(
						NewsAPI.API_FILTER(CulturalOfferConstants.ID_THREE, this.pageableNonExisting),
						HttpMethod.POST,
						this.httpEntity(new FilterParamsNewsDTO(format.parse(NewsConstants.START_DATE_MORE),format.parse(NewsConstants.END_DATE_MORE))),
						new ParameterizedTypeReference<List<NewsDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().isEmpty());
		assertEquals("false", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void testListOne() {
		ResponseEntity<List<NewsDTO>> response = 
				this.restTemplate.exchange(
				NewsAPI.API_FILTER(CulturalOfferConstants.ID_TWO, this.pageableTotal), HttpMethod.POST,
				this.httpEntity(new FilterParamsNewsDTO()), 
				new ParameterizedTypeReference<List<NewsDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<NewsDTO> news = response.getBody();
		assertEquals(MainConstants.ONE_SIZE, news.size());
		assertEquals(NewsConstants.ID_ONE, news.get(0).getId());
		assertEquals(NewsConstants.DATE_ONE, this.format.format(news.get(0).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_ONE, news.get(0).getText());
		assertEquals(CulturalOfferConstants.ID_TWO, news.get(0).getCulturalOfferId());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void testListNone() {
		ResponseEntity<List<NewsDTO>> response = 
				this.restTemplate.exchange(
				NewsAPI.API_FILTER(MainConstants.NON_EXISTING_ID, this.pageableTotal), HttpMethod.POST,
				this.httpEntity(new FilterParamsNewsDTO()), 
				new ParameterizedTypeReference<List<NewsDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().isEmpty());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void testListMoreDates() throws RestClientException, ParseException {
		ResponseEntity<List<NewsDTO>> response = 
				this.restTemplate.exchange(
						NewsAPI.API_FILTER(CulturalOfferConstants.ID_THREE, this.pageableTotal), 
						HttpMethod.POST,
						this.httpEntity(new FilterParamsNewsDTO(format.parse(NewsConstants.START_DATE_MORE),format.parse(NewsConstants.END_DATE_MORE))),
						new ParameterizedTypeReference<List<NewsDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<NewsDTO> news = response.getBody();
		assertEquals(MainConstants.TOTAL_SIZE, news.size());
		assertEquals(NewsConstants.ID_FOUR, news.get(0).getId());
		assertEquals(NewsConstants.DATE_FOUR, this.format.format(news.get(0).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_FOUR, news.get(0).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(0).getCulturalOfferId());
		assertEquals(NewsConstants.ID_TWO, news.get(1).getId());
		assertEquals(NewsConstants.DATE_TWO, this.format.format(news.get(1).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_TWO, news.get(1).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(1).getCulturalOfferId());
		assertEquals(NewsConstants.ID_THREE, news.get(2).getId());
		assertEquals(NewsConstants.DATE_THREE, this.format.format(news.get(2).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_THREE, news.get(2).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(2).getCulturalOfferId());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}

	@Test
	public void testListMoreDatesPaginated() throws RestClientException, ParseException {
		ResponseEntity<List<NewsDTO>> response = 
				this.restTemplate.exchange(
						NewsAPI.API_FILTER(CulturalOfferConstants.ID_THREE, this.pageablePart), 
						HttpMethod.POST,
						this.httpEntity(new FilterParamsNewsDTO(format.parse(NewsConstants.START_DATE_MORE),format.parse(NewsConstants.END_DATE_MORE))),
						new ParameterizedTypeReference<List<NewsDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<NewsDTO> news = response.getBody();
		assertEquals(MainConstants.PART_SIZE, news.size());
		assertEquals(NewsConstants.ID_FOUR, news.get(0).getId());
		assertEquals(NewsConstants.DATE_FOUR, this.format.format(news.get(0).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_FOUR, news.get(0).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(0).getCulturalOfferId());
		assertEquals(NewsConstants.ID_TWO, news.get(1).getId());
		assertEquals(NewsConstants.DATE_TWO, this.format.format(news.get(1).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_TWO, news.get(1).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(1).getCulturalOfferId());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("false", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}

	@Test
	public void testListMoreDatesNonExistingPage() throws RestClientException, ParseException {
		ResponseEntity<List<NewsDTO>> response = 
				this.restTemplate.exchange(
						NewsAPI.API_FILTER(CulturalOfferConstants.ID_THREE, this.pageableNonExisting),
						HttpMethod.POST,
						this.httpEntity(new FilterParamsNewsDTO(format.parse(NewsConstants.START_DATE_MORE), format.parse(NewsConstants.END_DATE_MORE))),
						new ParameterizedTypeReference<List<NewsDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().isEmpty());
		assertEquals("false", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}

	@Test
	public void testListOneDates() throws RestClientException, ParseException {
		ResponseEntity<List<NewsDTO>> response = 
				this.restTemplate.exchange(
				NewsAPI.API_FILTER(CulturalOfferConstants.ID_THREE, this.pageableTotal), HttpMethod.POST,
				this.httpEntity(new FilterParamsNewsDTO(this.format.parse(NewsConstants.START_DATE_ONE), this.format.parse(NewsConstants.END_DATE_ONE))),
				new ParameterizedTypeReference<List<NewsDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<NewsDTO> news = response.getBody();
		assertEquals(MainConstants.ONE_SIZE, news.size());
		assertEquals(NewsConstants.ID_FOUR, news.get(0).getId());
		assertEquals(NewsConstants.DATE_FOUR, this.format.format(news.get(0).getCreatedAt()));
		assertEquals(NewsConstants.TEXT_FOUR, news.get(0).getText());
		assertEquals(CulturalOfferConstants.ID_THREE, news.get(0).getCulturalOfferId());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}

	@Test
	public void testListNoneDates() throws RestClientException, ParseException {
		ResponseEntity<List<NewsDTO>> response = 
				this.restTemplate.exchange(NewsAPI.API_FILTER(
						MainConstants.NON_EXISTING_ID, this.pageableTotal), 
						HttpMethod.POST,
						this.httpEntity(new FilterParamsNewsDTO(this.format.parse(NewsConstants.WRONG_START), this.format.parse(NewsConstants.WRONG_END))),
						new ParameterizedTypeReference<List<NewsDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().isEmpty());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}

	@Test
	public void testDeleteExisting() {
		long id = this.newsRepository.save(this.testingNews()).getId();
		long size = this.newsRepository.count();
		ResponseEntity<Void> response = 
				this.restTemplate.exchange(
						NewsAPI.API_DELETE(id), 
						HttpMethod.DELETE,
						this.httpEntity(null), 
						Void.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(size - 1, this.newsRepository.count());
		assertNull(this.newsRepository.findById(id).orElse(null));
	}

	@Test
	public void testDeleteNonExisting() {
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
				NewsAPI.API_DELETE(MainConstants.NON_EXISTING_ID), 
				HttpMethod.DELETE, 
				this.httpEntity(null),
				ExceptionMessage.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(ExceptionConstants.INVALID_ID, response.getBody().getMessage());
	}

	@Test
	public void testAddValid() {
		long size = this.newsRepository.count();
		NewsUploadDTO newsDTO = this.testingNewsDTO();
		ResponseEntity<Void> response = 
				this.restTemplate.exchange(
						NewsAPI.API_BASE, 
						HttpMethod.POST,
						this.httpEntity(newsDTO), 
						Void.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		News news = this.newsRepository.findAll().get((int) (this.newsRepository.count() - 1));
		assertEquals(size + 1, this.newsRepository.count());
		assertEquals(this.format.format(new Date()), this.format.format(news.getCreatedAt()));
		assertEquals(CulturalOfferConstants.ID_THREE, news.getCulturalOffer().getId());
		assertEquals(NewsConstants.NON_EXISTING_TEXT, news.getText());
		this.newsRepository.deleteById(news.getId());
	}
	
	@Test
	public void testAddNonExistingOffer() {
		NewsUploadDTO newsDTO = this.testingNewsDTO();
		newsDTO.setCulturalOfferId(MainConstants.NON_EXISTING_ID);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(newsDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}

	@Test
	public void testAddNullText() {
		NewsUploadDTO newsDTO = this.testingNewsDTO();
		newsDTO.setText(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						NewsAPI.API_BASE, 
						HttpMethod.POST,
						this.httpEntity(newsDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}

	@Test
	public void testAddEmptyText() {
		NewsUploadDTO newsDTO = this.testingNewsDTO();
		newsDTO.setText("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						NewsAPI.API_BASE, 
						HttpMethod.POST,
						this.httpEntity(newsDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}

	@Test
	public void testAddBlankText() {
		NewsUploadDTO newsDTO = this.testingNewsDTO();
		newsDTO.setText("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						NewsAPI.API_BASE, 
						HttpMethod.POST,
						this.httpEntity(newsDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}

	@Test
	public void testUpdateValid() {
		long id = this.newsRepository.save(this.testingNews()).getId();
		long size = this.newsRepository.count();
		NewsUploadDTO newsDTO = this.testingNewsDTO();
		newsDTO.setId(id);
		ResponseEntity<Void> response = 
				this.restTemplate.exchange(
						NewsAPI.API_BASE, 
						HttpMethod.POST,
						this.httpEntity(newsDTO), 
						Void.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		News news = this.newsRepository.findById(id).orElse(null);
		assertEquals(size, this.newsRepository.count());
		assertEquals(id, news.getId());
		assertEquals(CulturalOfferConstants.ID_THREE, news.getCulturalOffer().getId());
		assertEquals(this.format.format(new Date()), this.format.format(news.getCreatedAt()));
		assertEquals(NewsConstants.NON_EXISTING_TEXT, news.getText());
		this.newsRepository.deleteById(id);
	}
	
	@Test
	public void testUpdateNonExistingOffer() {
		NewsUploadDTO newsDTO = this.testingNewsDTO();
		newsDTO.setId(CulturalOfferConstants.ID_ONE);
		newsDTO.setCulturalOfferId(MainConstants.NON_EXISTING_ID);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(newsDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}

	@Test
	public void testUpdateNullText() {
		NewsUploadDTO newsDTO = this.testingNewsDTO();
		newsDTO.setId(CulturalOfferConstants.ID_TWO);
		newsDTO.setText(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						NewsAPI.API_BASE, 
						HttpMethod.POST,
						this.httpEntity(newsDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}

	@Test
	public void testUpdateEmptyText() {
		NewsUploadDTO newsDTO = this.testingNewsDTO();
		newsDTO.setId(CulturalOfferConstants.ID_TWO);
		newsDTO.setText("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						NewsAPI.API_BASE, 
						HttpMethod.POST,
						this.httpEntity(newsDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}

	@Test
	public void testUpdateBlankText() {
		NewsUploadDTO newsDTO = this.testingNewsDTO();
		newsDTO.setId(CulturalOfferConstants.ID_TWO);
		newsDTO.setText("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						NewsAPI.API_BASE, 
						HttpMethod.POST,
						this.httpEntity(newsDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}

	public News testingNews() {
		News news = new News();
		news.setCulturalOffer(this.culturalOfferRepository.findById(CulturalOfferConstants.ID_THREE).orElse(null));
		news.setText(NewsConstants.TEXT_ONE);
		return news;
	}

	private NewsUploadDTO testingNewsDTO() {
		NewsUploadDTO news = new NewsUploadDTO();
		news.setCulturalOfferId(CulturalOfferConstants.ID_THREE);
		news.setText(NewsConstants.NON_EXISTING_TEXT);
		return news;
	}

	private HttpEntity<Object> httpEntity(Object obj) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", this.accessToken);
		if (obj instanceof NewsUploadDTO) {
			NewsUploadDTO upload = (NewsUploadDTO) obj;
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("id", upload.getId());
			body.add("culturalOfferId", upload.getCulturalOfferId());
			body.add("text", upload.getText());
			return new HttpEntity<Object>(body, headers);
		}
		return new HttpEntity<>(obj, headers);
	}

}
