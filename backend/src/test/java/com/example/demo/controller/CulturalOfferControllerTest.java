package com.example.demo.controller;

import static org.junit.Assert.assertFalse;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.demo.api.AuthAPI;
import com.example.demo.api.CulturalOfferAPI;
import com.example.demo.constants.Constants;
import com.example.demo.constants.CulturalOfferConstants;
import com.example.demo.constants.MainConstants;
import com.example.demo.constants.Filters;
import com.example.demo.constants.TypeConstants;
import com.example.demo.constants.UserConstants;
import com.example.demo.dto.BooleanDTO;
import com.example.demo.dto.CulturalOfferDTO;
import com.example.demo.dto.CulturalOfferUploadDTO;
import com.example.demo.dto.StringDTO;
import com.example.demo.dto.FilterParamsDTO;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.ProfileDTO;
import com.example.demo.dto.UniqueCheckDTO;
import com.example.demo.exception.ExceptionConstants;
import com.example.demo.exception.ExceptionMessage;
import com.example.demo.model.CulturalOffer;
import com.example.demo.repository.CulturalOfferRepository;
import com.example.demo.repository.TypeRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CulturalOfferControllerTest {
	
	@Autowired
	private TestRestTemplate restTemplate;
	private String accessToken;

	@Autowired 
	private CulturalOfferRepository culturalOfferRepository;
	
	@Autowired
	private TypeRepository typeRepository;

	@Autowired
	private Filters filters;
	private Pageable pageableTotal = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.TOTAL_SIZE);
	private Pageable pageablePart = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.PART_SIZE);
	private Pageable pageableNonExisting = PageRequest.of(MainConstants.ONE_SIZE, MainConstants.TOTAL_SIZE);

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
	public void testHasNameNewOfferNonExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(null);
		param.setName(CulturalOfferConstants.NON_EXISTING_NAME);
		ResponseEntity<BooleanDTO> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_HAS_NAME, 
						HttpMethod.POST, 
						this.httpEntity(param), 
						BooleanDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(response.getBody().isValue());
	}
	
	@Test
	public void testHasNameNewOfferExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(null);
		param.setName(CulturalOfferConstants.NAME_ONE);
		ResponseEntity<BooleanDTO> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_HAS_NAME, 
						HttpMethod.POST, 
						this.httpEntity(param), 
						BooleanDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().isValue());
	}
	
	@Test
	public void testHasNameOldOfferOwnName() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(CulturalOfferConstants.ID_ONE);
		param.setName(CulturalOfferConstants.NAME_ONE);
		ResponseEntity<BooleanDTO> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_HAS_NAME, 
						HttpMethod.POST, 
						this.httpEntity(param), 
						BooleanDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(response.getBody().isValue());
	}
	
	@Test
	public void testHasNameOldOfferNonExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(CulturalOfferConstants.ID_ONE);
		param.setName(CulturalOfferConstants.NON_EXISTING_NAME);
		ResponseEntity<BooleanDTO> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_HAS_NAME, 
						HttpMethod.POST, 
						this.httpEntity(param), 
						BooleanDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(response.getBody().isValue());
	}
	
	@Test
	public void testHasNameOldOfferExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(CulturalOfferConstants.ID_ONE);
		param.setName(CulturalOfferConstants.NAME_TWO);
		ResponseEntity<BooleanDTO> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_HAS_NAME, 
						HttpMethod.POST, 
						this.httpEntity(param), 
						BooleanDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().isValue());
	}
	
	@Test
	public void testFilterNamesEmpty() {
		ResponseEntity<List<String>> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_FILTER_NAMES, 
						HttpMethod.POST, 
						this.httpEntity(new StringDTO(MainConstants.FILTER_ALL)), 
						new ParameterizedTypeReference<List<String>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<String> names = response.getBody();
		assertEquals(MainConstants.TOTAL_SIZE, names.size());
		assertEquals(CulturalOfferConstants.NAME_ONE, names.get(0));
		assertEquals(CulturalOfferConstants.NAME_TWO, names.get(1));
		assertEquals(CulturalOfferConstants.NAME_THREE, names.get(2));
	}
	
	@Test
	public void testFilterNamesAll() {
		ResponseEntity<List<String>> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_FILTER_NAMES, 
						HttpMethod.POST, 
						this.httpEntity(new StringDTO(CulturalOfferConstants.FILTER_NAMES_ALL)), 
						new ParameterizedTypeReference<List<String>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<String> names = response.getBody();
		assertEquals(MainConstants.TOTAL_SIZE, names.size());
		assertEquals(CulturalOfferConstants.NAME_ONE, names.get(0));
		assertEquals(CulturalOfferConstants.NAME_TWO, names.get(1));
	}
	
	@Test
	public void testFilterNamesOne() {
		ResponseEntity<List<String>> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_FILTER_NAMES, 
						HttpMethod.POST, 
						this.httpEntity(new StringDTO(MainConstants.FILTER_ONE)), 
						new ParameterizedTypeReference<List<String>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<String> names = response.getBody();
		assertEquals(MainConstants.ONE_SIZE, names.size());
		assertEquals(CulturalOfferConstants.NAME_ONE, names.get(0));
	}
	
	@Test
	public void testFilterNamesNone() {
		ResponseEntity<List<String>> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_FILTER_NAMES, 
						HttpMethod.POST, 
						this.httpEntity(new StringDTO(MainConstants.FILTER_NONE)), 
						new ParameterizedTypeReference<List<String>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<String> names = response.getBody();
		assertTrue(names.isEmpty());
	}
	
	@Test
	public void testFilterLocationsEmpty() {
		ResponseEntity<List<String>> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_FILTER_LOCATIONS, 
						HttpMethod.POST, 
						this.httpEntity(new StringDTO(MainConstants.FILTER_ALL)), 
						new ParameterizedTypeReference<List<String>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<String> locations = response.getBody();
		assertEquals(MainConstants.TOTAL_SIZE, locations.size());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, locations.get(0));
		assertEquals(CulturalOfferConstants.LOCATION_TWO, locations.get(1));
		assertEquals(CulturalOfferConstants.LOCATION_THREE, locations.get(2));
	}
	
	@Test
	public void testFilterLocationsAll() {
		ResponseEntity<List<String>> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_FILTER_LOCATIONS, 
						HttpMethod.POST, 
						this.httpEntity(new StringDTO(CulturalOfferConstants.FILTER_LOCATIONS_ALL)), 
						new ParameterizedTypeReference<List<String>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<String> locations = response.getBody();
		assertEquals(MainConstants.TOTAL_SIZE, locations.size());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, locations.get(0));
		assertEquals(CulturalOfferConstants.LOCATION_TWO, locations.get(1));
	}
	
	@Test
	public void testFilterLocationsOne() {
		ResponseEntity<List<String>> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_FILTER_LOCATIONS, 
						HttpMethod.POST, 
						this.httpEntity(new StringDTO(MainConstants.FILTER_ONE)), 
						new ParameterizedTypeReference<List<String>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<String> locations = response.getBody();
		assertEquals(MainConstants.ONE_SIZE, locations.size());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, locations.get(0));
	}
	
	@Test
	public void testFilterLocationsNone() {
		ResponseEntity<List<String>> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_FILTER_LOCATIONS, 
						HttpMethod.POST, 
						this.httpEntity(new StringDTO(MainConstants.FILTER_NONE)), 
						new ParameterizedTypeReference<List<String>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<String> locations = response.getBody();
		assertTrue(locations.isEmpty());
	}
	
	@Test
	public void testFilterTypesEmpty() {
		ResponseEntity<List<String>> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_FILTER_TYPES, 
						HttpMethod.POST, 
						this.httpEntity(new StringDTO(MainConstants.FILTER_ALL)), 
						new ParameterizedTypeReference<List<String>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<String> types = response.getBody();
		assertEquals(MainConstants.TOTAL_SIZE, types.size());
		assertEquals(TypeConstants.NAME_ONE, types.get(0));
		assertEquals(TypeConstants.NAME_TWO, types.get(1));
		assertEquals(TypeConstants.NAME_THREE, types.get(2));
	}
	
	@Test
	public void testFilterTypesAll() {
		ResponseEntity<List<String>> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_FILTER_TYPES, 
						HttpMethod.POST, 
						this.httpEntity(new StringDTO(CulturalOfferConstants.FILTER_TYPES_ALL)), 
						new ParameterizedTypeReference<List<String>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<String> types = response.getBody();
		assertEquals(MainConstants.TOTAL_SIZE, types.size());
		assertEquals(TypeConstants.NAME_ONE, types.get(0));
		assertEquals(TypeConstants.NAME_TWO, types.get(1));
		assertEquals(TypeConstants.NAME_THREE, types.get(2));
	}
	
	@Test
	public void testFilterTypesOne() {
		ResponseEntity<List<String>> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_FILTER_TYPES, 
						HttpMethod.POST, 
						this.httpEntity(new StringDTO(MainConstants.FILTER_ONE)), 
						new ParameterizedTypeReference<List<String>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<String> types = response.getBody();
		assertEquals(MainConstants.ONE_SIZE, types.size());
		assertEquals(TypeConstants.NAME_ONE, types.get(0));
	}
	
	@Test
	public void testFilterTypesNone() {
		ResponseEntity<List<String>> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_FILTER_TYPES, 
						HttpMethod.POST, 
						this.httpEntity(new StringDTO(MainConstants.FILTER_NONE)), 
						new ParameterizedTypeReference<List<String>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<String> types = response.getBody();
		assertTrue(types.isEmpty());
	}
		
	@Test
	public void testFilterEmpty() {
		FilterParamsDTO filters = this.filters.filtersEmpty();
		ResponseEntity<List<CulturalOfferDTO>> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_FILTER(this.pageableTotal), 
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
						CulturalOfferAPI.API_FILTER(this.pageablePart), 
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
						CulturalOfferAPI.API_FILTER(this.pageableNonExisting), 
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
						CulturalOfferAPI.API_FILTER(this.pageableTotal), 
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
						CulturalOfferAPI.API_FILTER(this.pageablePart), 
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
		ResponseEntity<List<CulturalOfferDTO>> response = this.restTemplate.exchange(CulturalOfferAPI.API_FILTER(this.pageableNonExisting), HttpMethod.POST, this.httpEntity(filters), new ParameterizedTypeReference<List<CulturalOfferDTO>>() {});
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
						CulturalOfferAPI.API_FILTER(this.pageableTotal), 
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
						CulturalOfferAPI.API_FILTER(this.pageableTotal), 
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
						CulturalOfferAPI.API_FILTER(this.pageableTotal), 
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
						CulturalOfferAPI.API_FILTER(this.pageableTotal), 
						HttpMethod.POST, 
						this.httpEntity(filters), 
						new ParameterizedTypeReference<List<CulturalOfferDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().isEmpty());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void testDeleteExisting() {
		long id = this.culturalOfferRepository.save(this.testingOffer()).getId();
		long size = this.culturalOfferRepository.count();
		ResponseEntity<Void> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_DELETE(id), 
						HttpMethod.DELETE, 
						this.httpEntity(null), 
						Void.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(size - 1, this.culturalOfferRepository.count());
		assertNull(this.culturalOfferRepository.findById(id).orElse(null));
	}
	
	@Test
	public void testDeleteNonExisting() {
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_DELETE(MainConstants.NON_EXISTING_ID), 
						HttpMethod.DELETE, 
						this.httpEntity(null), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(ExceptionConstants.INVALID_ID, response.getBody().getMessage());
	}
	
	@Test
	public void testAddValid() {
		long size = this.culturalOfferRepository.count();
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		ResponseEntity<CulturalOfferDTO> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						CulturalOfferDTO.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		CulturalOfferDTO offer = response.getBody();
		assertEquals(size + 1, this.culturalOfferRepository.count());
		assertEquals(TypeConstants.NAME_ONE, offer.getType());
		assertEquals(CulturalOfferConstants.NON_EXISTING_NAME, offer.getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offer.getLocation());
		this.culturalOfferRepository.deleteById(offer.getId());
	}
	
	@Test
	public void testAddNullType() {
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		offerDTO.setType(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testAddEmptyType() {
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		offerDTO.setType("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}

	@Test
	public void testAddBlankType() {
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		offerDTO.setType("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testAddNonExistingType() {
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		offerDTO.setType(TypeConstants.NON_EXISTING_NAME);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testAddNullName() {
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		offerDTO.setName(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	
	@Test
	public void testAddEmptyName() {
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		offerDTO.setName("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testAddBlankName() {
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		offerDTO.setName("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testAddNonUniqueName() {
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		offerDTO.setName(CulturalOfferConstants.NAME_ONE);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.UNIQUE_VIOLATION));
	}
	
	@Test
	public void testAddNullLocation() {
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		offerDTO.setLocation(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testAddEmptyLocation() {
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		offerDTO.setLocation("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testAddBlankLocation() {
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		offerDTO.setName("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testUpdateValid() {
		long id = this.culturalOfferRepository.save(this.testingOffer()).getId();
		long size = this.culturalOfferRepository.count();
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		offerDTO.setId(id);
		ResponseEntity<CulturalOfferDTO> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						CulturalOfferDTO.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		CulturalOfferDTO offer = response.getBody();
		assertEquals(size, this.culturalOfferRepository.count());
		assertEquals(id, offer.getId());
		assertEquals(TypeConstants.NAME_ONE, offer.getType());
		assertEquals(CulturalOfferConstants.NON_EXISTING_NAME, offer.getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offer.getLocation());
		this.culturalOfferRepository.deleteById(id);
	}
	
	@Test
	public void testUpdateNullType() {
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		offerDTO.setId(CulturalOfferConstants.ID_ONE);
		offerDTO.setType(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testUpdateEmptyType() {
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		offerDTO.setId(CulturalOfferConstants.ID_ONE);
		offerDTO.setType("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}

	@Test
	public void testUpdateBlankType() {
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		offerDTO.setId(CulturalOfferConstants.ID_ONE);
		offerDTO.setType("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testUpdateNonExistingType() {
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		offerDTO.setId(CulturalOfferConstants.ID_ONE);
		offerDTO.setType(TypeConstants.NON_EXISTING_NAME);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}
	
	@Test
	public void testUpdateNullName() {
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		offerDTO.setId(CulturalOfferConstants.ID_ONE);
		offerDTO.setName(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	
	@Test
	public void testUpdateEmptyName() {
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		offerDTO.setId(CulturalOfferConstants.ID_ONE);
		offerDTO.setName("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testUpdateBlankName() {
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		offerDTO.setId(CulturalOfferConstants.ID_ONE);
		offerDTO.setName("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testUpdateNonUniqueName() {
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		offerDTO.setId(CulturalOfferConstants.ID_ONE);
		offerDTO.setName(CulturalOfferConstants.NAME_TWO);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.UNIQUE_VIOLATION));
	}
	
	@Test
	public void testUpdateNullLocation() {
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		offerDTO.setId(CulturalOfferConstants.ID_ONE);
		offerDTO.setLocation(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testUpdateEmptyLocation() {
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		offerDTO.setId(CulturalOfferConstants.ID_ONE);
		offerDTO.setLocation("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testUpdateBlankLocation() {
		CulturalOfferUploadDTO offerDTO = this.testingOfferDTO();
		offerDTO.setId(CulturalOfferConstants.ID_ONE);
		offerDTO.setName("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(offerDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	private CulturalOfferUploadDTO testingOfferDTO() {
		CulturalOfferUploadDTO offer = new CulturalOfferUploadDTO();
		offer.setType(TypeConstants.NAME_ONE);
		offer.setName(CulturalOfferConstants.NON_EXISTING_NAME);
		offer.setLocation(CulturalOfferConstants.LOCATION_ONE);
		return offer;
	}
	
	private CulturalOffer testingOffer() {
		CulturalOffer offer = new CulturalOffer();
		offer.setType(this.typeRepository.findById(1l).orElse(null));
		offer.setName(CulturalOfferConstants.NON_EXISTING_NAME);
		offer.setLocation(CulturalOfferConstants.LOCATION_ONE);
		return offer;
	}
	
	private HttpEntity<Object> httpEntity(Object obj){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", this.accessToken);			
		if (obj instanceof CulturalOfferUploadDTO) {
			CulturalOfferUploadDTO upload = (CulturalOfferUploadDTO) obj;
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("id", upload.getId());
			body.add("type", upload.getType());
			body.add("name", upload.getName());
			body.add("location", upload.getLocation());
			return new HttpEntity<Object>(body, headers);
		}
		return new HttpEntity<>(obj, headers);
	}
	
}
