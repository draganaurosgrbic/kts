package com.example.demo.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.api.AuthAPI;
import com.example.demo.api.CulturalOfferAPI;
import com.example.demo.constants.Constants;
import com.example.demo.constants.CulturalOfferConstants;
import com.example.demo.constants.MainConstants;
import com.example.demo.constants.Filters;
import com.example.demo.constants.TypeConstants;
import com.example.demo.constants.UserConstants;
import com.example.demo.dto.CulturalOfferDTO;
import com.example.demo.dto.FilterParamsDTO;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.ProfileDTO;
import com.example.demo.exception.ExceptionConstants;
import com.example.demo.exception.ExceptionMessage;
import com.example.demo.repository.UserFollowingRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserFollowingControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;
	private String accessToken;

	@Autowired
	private UserFollowingRepository userFollowingRepository;

	@Autowired
	private Filters filters;
	private Pageable pageableTotal = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.TOTAL_SIZE);
	private Pageable pageablePart = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.PART_SIZE);
	private Pageable pageableNonExisting = PageRequest.of(MainConstants.ONE_SIZE, MainConstants.TOTAL_SIZE);

	@Before
	public void guestLogin() {
		LoginDTO login = new LoginDTO();
		login.setEmail(UserConstants.EMAIL_ONE);
		login.setPassword(UserConstants.LOGIN_PASSWORD);
		ResponseEntity<ProfileDTO> response = 
				this.restTemplate.postForEntity(
						AuthAPI.API_LOGIN, 
						login, 
						ProfileDTO.class);
		this.accessToken = response.getBody().getAccessToken();
	}
	
	@Test
	public void testFilterEmpty() {
		FilterParamsDTO filters = this.filters.filtersEmpty();
		ResponseEntity<List<CulturalOfferDTO>> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_FILTER_FOLLOWINGS(this.pageableTotal), 
						HttpMethod.POST, 
						this.httpEntity(filters), 
						new ParameterizedTypeReference<List<CulturalOfferDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<CulturalOfferDTO> offers = response.getBody();
		assertEquals(MainConstants.TOTAL_SIZE, offers.size());
		assertEquals(CulturalOfferConstants.ID_ONE, offers.get(0).getId());
		assertEquals(TypeConstants.NAME_ONE, offers.get(0).getType());
		assertEquals(CulturalOfferConstants.NAME_ONE, offers.get(0).getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offers.get(0).getLocation());
		assertEquals(CulturalOfferConstants.ID_TWO, offers.get(1).getId());
		assertEquals(TypeConstants.NAME_TWO, offers.get(1).getType());
		assertEquals(CulturalOfferConstants.NAME_TWO, offers.get(1).getName());
		assertEquals(CulturalOfferConstants.LOCATION_TWO, offers.get(1).getLocation());
		assertEquals(CulturalOfferConstants.ID_THREE, offers.get(2).getId());
		assertEquals(TypeConstants.NAME_THREE, offers.get(2).getType());
		assertEquals(CulturalOfferConstants.NAME_THREE, offers.get(2).getName());
		assertEquals(CulturalOfferConstants.LOCATION_THREE, offers.get(2).getLocation());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void testFilterEmptyPaginated() {
		FilterParamsDTO filters = this.filters.filtersEmpty();
		ResponseEntity<List<CulturalOfferDTO>> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_FILTER_FOLLOWINGS(this.pageablePart), 
						HttpMethod.POST, 
						this.httpEntity(filters), 
						new ParameterizedTypeReference<List<CulturalOfferDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<CulturalOfferDTO> offers = response.getBody();
		assertEquals(MainConstants.PART_SIZE, offers.size());
		assertEquals(CulturalOfferConstants.ID_ONE, offers.get(0).getId());
		assertEquals(TypeConstants.NAME_ONE, offers.get(0).getType());
		assertEquals(CulturalOfferConstants.NAME_ONE, offers.get(0).getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offers.get(0).getLocation());
		assertEquals(CulturalOfferConstants.ID_TWO, offers.get(1).getId());
		assertEquals(TypeConstants.NAME_TWO, offers.get(1).getType());
		assertEquals(CulturalOfferConstants.NAME_TWO, offers.get(1).getName());
		assertEquals(CulturalOfferConstants.LOCATION_TWO, offers.get(1).getLocation());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("false", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void testFilterEmptyNonExistingPage() {
		FilterParamsDTO filters = this.filters.filtersEmpty();
		ResponseEntity<List<CulturalOfferDTO>> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_FILTER_FOLLOWINGS(this.pageableNonExisting), 
						HttpMethod.POST, 
						this.httpEntity(filters), 
						new ParameterizedTypeReference<List<CulturalOfferDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().isEmpty());
		assertEquals("false", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void testFilterAll() {
		FilterParamsDTO filters = this.filters.filtersAll();
		ResponseEntity<List<CulturalOfferDTO>> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_FILTER_FOLLOWINGS(this.pageableTotal), 
						HttpMethod.POST, 
						this.httpEntity(filters), 
						new ParameterizedTypeReference<List<CulturalOfferDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<CulturalOfferDTO> offers = response.getBody();
		assertEquals(MainConstants.TOTAL_SIZE, offers.size());
		assertEquals(CulturalOfferConstants.ID_ONE, offers.get(0).getId());
		assertEquals(TypeConstants.NAME_ONE, offers.get(0).getType());
		assertEquals(CulturalOfferConstants.NAME_ONE, offers.get(0).getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offers.get(0).getLocation());
		assertEquals(CulturalOfferConstants.ID_TWO, offers.get(1).getId());
		assertEquals(TypeConstants.NAME_TWO, offers.get(1).getType());
		assertEquals(CulturalOfferConstants.NAME_TWO, offers.get(1).getName());
		assertEquals(CulturalOfferConstants.LOCATION_TWO, offers.get(1).getLocation());
		assertEquals(CulturalOfferConstants.ID_THREE, offers.get(2).getId());
		assertEquals(TypeConstants.NAME_THREE, offers.get(2).getType());
		assertEquals(CulturalOfferConstants.NAME_THREE, offers.get(2).getName());
		assertEquals(CulturalOfferConstants.LOCATION_THREE, offers.get(2).getLocation());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void testFilterAllPaginated() {
		FilterParamsDTO filters = this.filters.filtersAll();
		ResponseEntity<List<CulturalOfferDTO>> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_FILTER_FOLLOWINGS(this.pageablePart), 
						HttpMethod.POST, 
						this.httpEntity(filters), 
						new ParameterizedTypeReference<List<CulturalOfferDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<CulturalOfferDTO> offers = response.getBody();
		assertEquals(MainConstants.PART_SIZE, offers.size());
		assertEquals(CulturalOfferConstants.ID_ONE, offers.get(0).getId());
		assertEquals(TypeConstants.NAME_ONE, offers.get(0).getType());
		assertEquals(CulturalOfferConstants.NAME_ONE, offers.get(0).getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offers.get(0).getLocation());
		assertEquals(CulturalOfferConstants.ID_TWO, offers.get(1).getId());
		assertEquals(TypeConstants.NAME_TWO, offers.get(1).getType());
		assertEquals(CulturalOfferConstants.NAME_TWO, offers.get(1).getName());
		assertEquals(CulturalOfferConstants.LOCATION_TWO, offers.get(1).getLocation());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("false", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void testFilterAllNonExistingPage() {
		FilterParamsDTO filters = this.filters.filtersAll();
		ResponseEntity<List<CulturalOfferDTO>> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_FILTER_FOLLOWINGS(this.pageableNonExisting), 
						HttpMethod.POST, 
						this.httpEntity(filters), 
						new ParameterizedTypeReference<List<CulturalOfferDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().isEmpty());
		assertEquals("false", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void testFilterOneName() {
		FilterParamsDTO filters = this.filters.filtersOneName();
		ResponseEntity<List<CulturalOfferDTO>> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_FILTER_FOLLOWINGS(this.pageableTotal), 
						HttpMethod.POST, 
						this.httpEntity(filters), 
						new ParameterizedTypeReference<List<CulturalOfferDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<CulturalOfferDTO> offers = response.getBody();
		assertEquals(MainConstants.ONE_SIZE, offers.size());
		assertEquals(CulturalOfferConstants.ID_ONE, offers.get(0).getId());
		assertEquals(TypeConstants.NAME_ONE, offers.get(0).getType());
		assertEquals(CulturalOfferConstants.NAME_ONE, offers.get(0).getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offers.get(0).getLocation());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void testFilterOneLocation() {
		FilterParamsDTO filters = this.filters.filtersOneLocation();
		ResponseEntity<List<CulturalOfferDTO>> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_FILTER_FOLLOWINGS(this.pageableTotal), 
						HttpMethod.POST, 
						this.httpEntity(filters), 
						new ParameterizedTypeReference<List<CulturalOfferDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<CulturalOfferDTO> offers = response.getBody();
		assertEquals(MainConstants.ONE_SIZE, offers.size());
		assertEquals(CulturalOfferConstants.ID_ONE, offers.get(0).getId());
		assertEquals(TypeConstants.NAME_ONE, offers.get(0).getType());
		assertEquals(CulturalOfferConstants.NAME_ONE, offers.get(0).getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offers.get(0).getLocation());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void testFilterOneType() {
		FilterParamsDTO filters = this.filters.filtersOneType();
		ResponseEntity<List<CulturalOfferDTO>> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_FILTER_FOLLOWINGS(this.pageableTotal), 
						HttpMethod.POST, 
						this.httpEntity(filters), 
						new ParameterizedTypeReference<List<CulturalOfferDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<CulturalOfferDTO> offers = response.getBody();
		assertEquals(MainConstants.ONE_SIZE, offers.size());
		assertEquals(CulturalOfferConstants.ID_ONE, offers.get(0).getId());
		assertEquals(TypeConstants.NAME_ONE, offers.get(0).getType());
		assertEquals(CulturalOfferConstants.NAME_ONE, offers.get(0).getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offers.get(0).getLocation());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void testFilterNone() {
		FilterParamsDTO filters = this.filters.filtersNone();
		ResponseEntity<List<CulturalOfferDTO>> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_FILTER_FOLLOWINGS(this.pageableTotal), 
						HttpMethod.POST, 
						this.httpEntity(filters), 
						new ParameterizedTypeReference<List<CulturalOfferDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().isEmpty());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void toggleSubscription() {
		ResponseEntity<Void> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_TOGGLE_SUBSCRIPTION(CulturalOfferConstants.ID_ONE), 
						HttpMethod.GET, 
						this.httpEntity(null), 
						Void.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNull(this.userFollowingRepository.findByUserIdAndCulturalOfferId(UserConstants.ID_ONE, CulturalOfferConstants.ID_ONE));
		response = this.restTemplate.exchange(CulturalOfferAPI.API_TOGGLE_SUBSCRIPTION(CulturalOfferConstants.ID_ONE), HttpMethod.GET, this.httpEntity(null), Void.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(this.userFollowingRepository.findByUserIdAndCulturalOfferId(UserConstants.ID_ONE, CulturalOfferConstants.ID_ONE));
	}
	
	@Test
	public void testToggleSubscriptionNonExisting() {
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_TOGGLE_SUBSCRIPTION(MainConstants.NON_EXISTING_ID), 
						HttpMethod.GET, 
						this.httpEntity(null), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}

	private HttpEntity<Object> httpEntity(Object obj){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", this.accessToken);			
		return new HttpEntity<>(obj, headers);
	}
	
}
