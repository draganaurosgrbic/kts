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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.api.AuthAPI;
import com.example.demo.api.CategoryAPI;
import com.example.demo.constants.CategoryConstants;
import com.example.demo.constants.Constants;
import com.example.demo.constants.MainConstants;
import com.example.demo.constants.UserConstants;
import com.example.demo.dto.BooleanDTO;
import com.example.demo.dto.CategoryDTO;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.ProfileDTO;
import com.example.demo.dto.StringDTO;
import com.example.demo.dto.UniqueCheckDTO;
import com.example.demo.exception.ExceptionConstants;
import com.example.demo.exception.ExceptionMessage;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
	
	@Autowired
	private TestRestTemplate restTemplate;
	private String accessToken;
	
	@Autowired 
	private CategoryRepository categoryRepository;
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
	public void testHasNameNonExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setName(CategoryConstants.NON_EXISTING_NAME);
		ResponseEntity<BooleanDTO> response = 
				this.restTemplate.exchange(
						CategoryAPI.API_HAS_NAME, 
						HttpMethod.POST, 
						this.httpEntity(param), 
						BooleanDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(response.getBody().isValue());
	}
	
	@Test
	public void testHasNameExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(null);
		param.setName(CategoryConstants.NAME_ONE);
		ResponseEntity<BooleanDTO> response = 
				this.restTemplate.exchange(
						CategoryAPI.API_HAS_NAME, 
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
						CategoryAPI.API_FILTER_NAMES, 
						HttpMethod.POST, 
						this.httpEntity(new StringDTO(MainConstants.FILTER_ALL)), 
						new ParameterizedTypeReference<List<String>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<String> names = response.getBody();
		assertEquals(MainConstants.TOTAL_SIZE, names.size());
		assertEquals(CategoryConstants.NAME_ONE, names.get(0));
		assertEquals(CategoryConstants.NAME_TWO, names.get(1));
		assertEquals(CategoryConstants.NAME_THREE, names.get(2));
	}
	
	@Test
	public void testFilterNamesAll() {
		ResponseEntity<List<String>> response = 
				this.restTemplate.exchange(
						CategoryAPI.API_FILTER_NAMES, 
						HttpMethod.POST, 
						this.httpEntity(new StringDTO(CategoryConstants.FILTER_NAMES_ALL)), 
						new ParameterizedTypeReference<List<String>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<String> names = response.getBody();
		assertEquals(MainConstants.TOTAL_SIZE, names.size());
		assertEquals(CategoryConstants.NAME_ONE, names.get(0));
		assertEquals(CategoryConstants.NAME_TWO, names.get(1));
	}
	
	@Test
	public void testFilterNamesOne() {
		ResponseEntity<List<String>> response = 
				this.restTemplate.exchange(
						CategoryAPI.API_FILTER_NAMES, 
						HttpMethod.POST, 
						this.httpEntity(new StringDTO(MainConstants.FILTER_ONE)), 
						new ParameterizedTypeReference<List<String>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<String> names = response.getBody();
		assertEquals(MainConstants.ONE_SIZE, names.size());
		assertEquals(CategoryConstants.NAME_ONE, names.get(0));
	}
	
	@Test
	public void testFilterNamesNone() {
		ResponseEntity<List<String>> response = 
				this.restTemplate.exchange(
						CategoryAPI.API_FILTER_NAMES, 
						HttpMethod.POST, 
						this.httpEntity(new StringDTO(MainConstants.FILTER_NONE)), 
						new ParameterizedTypeReference<List<String>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<String> names = response.getBody();
		assertTrue(names.isEmpty());
	}
	
	@Test
	public void testListAll() {
		ResponseEntity<List<CategoryDTO>> response = 
				this.restTemplate.exchange(
						CategoryAPI.API_LIST(this.pageableTotal), 
						HttpMethod.GET, 
						this.httpEntity(null), 
						new ParameterizedTypeReference<List<CategoryDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<CategoryDTO> categories = response.getBody();
		assertEquals(MainConstants.TOTAL_SIZE, categories.size());
		assertEquals(CategoryConstants.ID_ONE, categories.get(0).getId());
		assertEquals(CategoryConstants.NAME_ONE, categories.get(0).getName());
		assertEquals(CategoryConstants.ID_TWO, categories.get(1).getId());
		assertEquals(CategoryConstants.NAME_TWO, categories.get(1).getName());
		assertEquals(CategoryConstants.ID_THREE, categories.get(2).getId());
		assertEquals(CategoryConstants.NAME_THREE, categories.get(2).getName());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void testListPaginated() {
		ResponseEntity<List<CategoryDTO>> response = 
				this.restTemplate.exchange(
						CategoryAPI.API_LIST(this.pageablePart), 
						HttpMethod.GET, 
						this.httpEntity(null), 
						new ParameterizedTypeReference<List<CategoryDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<CategoryDTO> categories = response.getBody();
		assertEquals(MainConstants.PART_SIZE, categories.size());
		assertEquals(CategoryConstants.ID_ONE, categories.get(0).getId());
		assertEquals(CategoryConstants.NAME_ONE, categories.get(0).getName());
		assertEquals(CategoryConstants.ID_TWO, categories.get(1).getId());
		assertEquals(CategoryConstants.NAME_TWO, categories.get(1).getName());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("false", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void testListAllNonExistingPage() {
		ResponseEntity<List<CategoryDTO>> response = 
				this.restTemplate.exchange(
						CategoryAPI.API_LIST(this.pageableNonExisting), 
						HttpMethod.GET, this.httpEntity(null), 
						new ParameterizedTypeReference<List<CategoryDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().isEmpty());
		assertEquals("false", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void testDeleteExisting() {
		long id = this.categoryRepository.save(this.testingCategory()).getId();
		long size = this.categoryRepository.count();
		ResponseEntity<Void> response = 
				this.restTemplate.exchange(
						CategoryAPI.API_DELETE(id), 
						HttpMethod.DELETE, 
						this.httpEntity(null), 
						Void.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(size - 1, this.categoryRepository.count());
		assertNull(this.categoryRepository.findById(id).orElse(null));
	}
	
	@Test
	public void testDeleteWithType() {
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CategoryAPI.API_DELETE(CategoryConstants.ID_ONE), 
						HttpMethod.DELETE, 
						this.httpEntity(null), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.UNIQUE_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testDeleteNonExisting() {
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CategoryAPI.API_DELETE(MainConstants.NON_EXISTING_ID), 
						HttpMethod.DELETE, 
						this.httpEntity(null), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(ExceptionConstants.INVALID_ID, response.getBody().getMessage());
	}
	
	@Test
	public void testAddValid() {
		long size = this.categoryRepository.count();
		CategoryDTO categoryDTO = this.testingCategoryDTO();
		ResponseEntity<Void> response = 
				this.restTemplate.exchange(
						CategoryAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(categoryDTO), 
						Void.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(size + 1, this.categoryRepository.count());
		Category category = this.categoryRepository.findByName(categoryDTO.getName());
		assertEquals(CategoryConstants.NON_EXISTING_NAME, category.getName());
		this.categoryRepository.deleteById(category.getId());
	}
	
	@Test
	public void testAddNullName() {
		CategoryDTO categoryDTO = this.testingCategoryDTO();
		categoryDTO.setName(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CategoryAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(categoryDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testAddEmptyName() {
		CategoryDTO categoryDTO = this.testingCategoryDTO();
		categoryDTO.setName("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CategoryAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(categoryDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testAddBlankName() {
		CategoryDTO categoryDTO = this.testingCategoryDTO();
		categoryDTO.setName("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CategoryAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(categoryDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testAddNonUniqueName() {
		CategoryDTO categoryDTO = this.testingCategoryDTO();
		categoryDTO.setName(CategoryConstants.NAME_ONE);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CategoryAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(categoryDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.UNIQUE_VIOLATION));
	}
	
	@Test
	public void testUpdateValid() {
		long id = this.categoryRepository.save(this.testingCategory()).getId();
		long size = this.categoryRepository.count();
		CategoryDTO categoryDTO = this.testingCategoryDTO();
		categoryDTO.setId(id);
		ResponseEntity<Void> response = 
				this.restTemplate.exchange(
						CategoryAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(categoryDTO), 
						Void.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		Category category = this.categoryRepository.findByName(categoryDTO.getName());
		assertEquals(size, this.categoryRepository.count());
		assertEquals(id, category.getId());
		assertEquals(CategoryConstants.NON_EXISTING_NAME, category.getName());
		this.categoryRepository.deleteById(id);
	}
	
	@Test
	public void testUpdateNullName() {
		CategoryDTO categoryDTO = this.testingCategoryDTO();
		categoryDTO.setId(CategoryConstants.ID_ONE);
		categoryDTO.setName(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CategoryAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(categoryDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testUpdateEmptyName() {
		CategoryDTO categoryDTO = this.testingCategoryDTO();
		categoryDTO.setId(CategoryConstants.ID_ONE);
		categoryDTO.setName("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CategoryAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(categoryDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testUpdateBlankName() {
		CategoryDTO categoryDTO = this.testingCategoryDTO();
		categoryDTO.setId(CategoryConstants.ID_ONE);
		categoryDTO.setName("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CategoryAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(categoryDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testUpdateNonUniqueName() {
		CategoryDTO categoryDTO = this.testingCategoryDTO();
		categoryDTO.setId(CategoryConstants.ID_ONE);
		categoryDTO.setName(CategoryConstants.NAME_TWO);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CategoryAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(categoryDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.UNIQUE_VIOLATION));
	}
	
	private Category testingCategory() {
		Category category = new Category();
		category.setName(CategoryConstants.NON_EXISTING_NAME);
		return category;
	}
	
	private CategoryDTO testingCategoryDTO() {
		CategoryDTO category = new CategoryDTO();
		category.setName(CategoryConstants.NON_EXISTING_NAME);
		return category;
	}
	
	private HttpEntity<Object> httpEntity(Object obj){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", this.accessToken);			
		return new HttpEntity<>(obj, headers);
	}

}
