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
import com.example.demo.api.TypeAPI;
import com.example.demo.constants.CategoryConstants;
import com.example.demo.constants.Constants;
import com.example.demo.constants.MainConstants;
import com.example.demo.constants.TypeConstants;
import com.example.demo.constants.UserConstants;
import com.example.demo.dto.BooleanDTO;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.ProfileDTO;
import com.example.demo.dto.StringDTO;
import com.example.demo.dto.TypeDTO;
import com.example.demo.dto.TypeUploadDTO;
import com.example.demo.dto.UniqueCheckDTO;
import com.example.demo.exception.ExceptionConstants;
import com.example.demo.exception.ExceptionMessage;
import com.example.demo.model.Type;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.TypeRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TypeControllerTest {
	
	@Autowired
	private TestRestTemplate restTemplate;
	private String accessToken;
	
	@Autowired 
	private TypeRepository typeRepository;
	
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
		param.setName(TypeConstants.NON_EXISTING_NAME);
		ResponseEntity<BooleanDTO> response = 
				this.restTemplate.exchange(
						TypeAPI.API_HAS_NAME, 
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
		param.setName(TypeConstants.NAME_ONE);
		ResponseEntity<BooleanDTO> response = 
				this.restTemplate.exchange(
						TypeAPI.API_HAS_NAME, 
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
						TypeAPI.API_FILTER_NAMES, 
						HttpMethod.POST, 
						this.httpEntity(new StringDTO(MainConstants.FILTER_ALL)), 
						new ParameterizedTypeReference<List<String>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<String> names = response.getBody();
		assertEquals(MainConstants.TOTAL_SIZE, names.size());
		assertEquals(TypeConstants.NAME_ONE, names.get(0));
		assertEquals(TypeConstants.NAME_TWO, names.get(1));
		assertEquals(TypeConstants.NAME_THREE, names.get(2));
	}
	
	@Test
	public void testFilterNamesAll() {
		ResponseEntity<List<String>> response = 
				this.restTemplate.exchange(
						TypeAPI.API_FILTER_NAMES, 
						HttpMethod.POST, 
						this.httpEntity(new StringDTO(TypeConstants.FILTER_NAMES_ALL)), 
						new ParameterizedTypeReference<List<String>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<String> names = response.getBody();
		assertEquals(MainConstants.TOTAL_SIZE, names.size());
		assertEquals(TypeConstants.NAME_ONE, names.get(0));
		assertEquals(TypeConstants.NAME_TWO, names.get(1));
	}
	
	@Test
	public void testFilterNamesOne() {
		ResponseEntity<List<String>> response = 
				this.restTemplate.exchange(
						TypeAPI.API_FILTER_NAMES, 
						HttpMethod.POST, 
						this.httpEntity(new StringDTO(MainConstants.FILTER_ONE)), 
						new ParameterizedTypeReference<List<String>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<String> names = response.getBody();
		assertEquals(MainConstants.ONE_SIZE, names.size());
		assertEquals(TypeConstants.NAME_ONE, names.get(0));
	}
	
	@Test
	public void testFilterNamesNone() {
		ResponseEntity<List<String>> response = 
				this.restTemplate.exchange(
						TypeAPI.API_FILTER_NAMES, 
						HttpMethod.POST, 
						this.httpEntity(new StringDTO(MainConstants.FILTER_NONE)), 
						new ParameterizedTypeReference<List<String>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<String> names = response.getBody();
		assertTrue(names.isEmpty());
	}
	
	@Test
	public void testListAll() {
		ResponseEntity<List<TypeDTO>> response = 
				this.restTemplate.exchange(
						TypeAPI.API_LIST(this.pageableTotal), 
						HttpMethod.GET, 
						this.httpEntity(null), 
						new ParameterizedTypeReference<List<TypeDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<TypeDTO> types = response.getBody();
		assertEquals(MainConstants.TOTAL_SIZE, types.size());
		assertEquals(TypeConstants.ID_ONE, types.get(0).getId());
		assertEquals(CategoryConstants.NAME_ONE, types.get(0).getCategory());
		assertEquals(TypeConstants.NAME_ONE, types.get(0).getName());
		assertEquals(TypeConstants.ID_TWO, types.get(1).getId());
		assertEquals(CategoryConstants.NAME_TWO, types.get(1).getCategory());
		assertEquals(TypeConstants.NAME_TWO, types.get(1).getName());
		assertEquals(TypeConstants.ID_THREE, types.get(2).getId());
		assertEquals(CategoryConstants.NAME_THREE, types.get(2).getCategory());
		assertEquals(TypeConstants.NAME_THREE, types.get(2).getName());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void testListPaginated() {
		ResponseEntity<List<TypeDTO>> response = 
				this.restTemplate.exchange(
						TypeAPI.API_LIST(this.pageablePart), 
						HttpMethod.GET, 
						this.httpEntity(null), 
						new ParameterizedTypeReference<List<TypeDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<TypeDTO> types = response.getBody();
		assertEquals(MainConstants.PART_SIZE, types.size());
		assertEquals(TypeConstants.ID_ONE, types.get(0).getId());
		assertEquals(CategoryConstants.NAME_ONE, types.get(0).getCategory());
		assertEquals(TypeConstants.NAME_ONE, types.get(0).getName());
		assertEquals(TypeConstants.ID_TWO, types.get(1).getId());
		assertEquals(CategoryConstants.NAME_TWO, types.get(1).getCategory());
		assertEquals(TypeConstants.NAME_TWO, types.get(1).getName());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("false", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void testListAllNonExistingPage() {
		ResponseEntity<List<TypeDTO>> response = 
				this.restTemplate.exchange(
						TypeAPI.API_LIST(this.pageableNonExisting), 
						HttpMethod.GET, 
						this.httpEntity(null), 
						new ParameterizedTypeReference<List<TypeDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().isEmpty());
		assertEquals("false", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void testDeleteExisting() {
		long id = this.typeRepository.save(this.testingType()).getId();
		long size = this.typeRepository.count();
		ResponseEntity<Void> response = 
				this.restTemplate.exchange(
						TypeAPI.API_DELETE(id), 
						HttpMethod.DELETE, 
						this.httpEntity(null), 
						Void.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(size - 1, this.typeRepository.count());
		assertNull(this.typeRepository.findById(id).orElse(null));
	}
	
	@Test
	public void testDeleteWithType() {
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						TypeAPI.API_DELETE(TypeConstants.ID_ONE), 
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
						TypeAPI.API_DELETE(MainConstants.NON_EXISTING_ID), 
						HttpMethod.DELETE, 
						this.httpEntity(null), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(ExceptionConstants.INVALID_ID, response.getBody().getMessage());
	}
	
	@Test
	public void testAddValid() {
		long size = this.typeRepository.count();
		TypeUploadDTO typeDTO = this.testingTypeUploadDTO();
		ResponseEntity<Void> response = 
				this.restTemplate.exchange(
						TypeAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(typeDTO), 
						Void.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(size + 1, this.typeRepository.count());
		Type type = this.typeRepository.findByName(typeDTO.getName());
		assertEquals(CategoryConstants.ID_ONE, type.getCategory().getId());
		assertEquals(TypeConstants.NON_EXISTING_NAME, type.getName());
		this.typeRepository.deleteById(type.getId());
	}
	
	@Test
	public void testAddNullCategory() {
		TypeUploadDTO typeDTO = this.testingTypeUploadDTO();
		typeDTO.setCategory(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						TypeAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(typeDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testAddEmptyCategory() {
		TypeUploadDTO typeDTO = this.testingTypeUploadDTO();
		typeDTO.setCategory("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						TypeAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(typeDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}

	@Test
	public void testAddBlankCategory() {
		TypeUploadDTO typeDTO = this.testingTypeUploadDTO();
		typeDTO.setCategory("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						TypeAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(typeDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testAddNonExistingCategory() {
		TypeUploadDTO typeDTO = this.testingTypeUploadDTO();
		typeDTO.setCategory(CategoryConstants.NON_EXISTING_NAME);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						TypeAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(typeDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testAddNullName() {
		TypeUploadDTO typeDTO = this.testingTypeUploadDTO();
		typeDTO.setName(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						TypeAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(typeDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testAddEmptyName() {
		TypeUploadDTO typeDTO = this.testingTypeUploadDTO();
		typeDTO.setName("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						TypeAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(typeDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testAddBlankName() {
		TypeUploadDTO typeDTO = this.testingTypeUploadDTO();
		typeDTO.setName("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						TypeAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(typeDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testAddNonUniqueName() {
		TypeUploadDTO typeDTO = this.testingTypeUploadDTO();
		typeDTO.setName(TypeConstants.NAME_ONE);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						TypeAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(typeDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.UNIQUE_VIOLATION));
	}
	
	@Test
	public void testUpdateValid() {
		long id = this.typeRepository.save(this.testingType()).getId();
		long size = this.typeRepository.count();
		TypeUploadDTO typeDTO = this.testingTypeUploadDTO();
		typeDTO.setId(id);
		ResponseEntity<Void> response = 
				this.restTemplate.exchange(
						TypeAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(typeDTO), 
						Void.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		Type type = this.typeRepository.findByName(typeDTO.getName());
		assertEquals(size, this.typeRepository.count());
		assertEquals(id, type.getId());
		assertEquals(TypeConstants.NON_EXISTING_NAME, type.getName());
		this.typeRepository.deleteById(id);
	}
	
	@Test
	public void testUpdateNullCategory() {
		TypeUploadDTO typeDTO = this.testingTypeUploadDTO();
		typeDTO.setId(TypeConstants.ID_ONE);
		typeDTO.setCategory(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						TypeAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(typeDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testUpdateEmptyCategory() {
		TypeUploadDTO typeDTO = this.testingTypeUploadDTO();
		typeDTO.setId(TypeConstants.ID_ONE);
		typeDTO.setCategory("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						TypeAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(typeDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}

	@Test
	public void testUpdateBlankCategory() {
		TypeUploadDTO typeDTO = this.testingTypeUploadDTO();
		typeDTO.setId(TypeConstants.ID_ONE);
		typeDTO.setCategory("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						TypeAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(typeDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testUpdateNonExistingCategory() {
		TypeUploadDTO typeDTO = this.testingTypeUploadDTO();
		typeDTO.setId(TypeConstants.ID_ONE);
		typeDTO.setCategory(CategoryConstants.NON_EXISTING_NAME);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						TypeAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(typeDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}
	
	@Test
	public void testUpdateNullName() {
		TypeUploadDTO typeDTO = this.testingTypeUploadDTO();
		typeDTO.setId(TypeConstants.ID_ONE);
		typeDTO.setName(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						TypeAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(typeDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testUpdateEmptyName() {
		TypeUploadDTO typeDTO = this.testingTypeUploadDTO();
		typeDTO.setId(TypeConstants.ID_ONE);
		typeDTO.setName("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						TypeAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(typeDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testUpdateBlankName() {
		TypeUploadDTO typeDTO = this.testingTypeUploadDTO();
		typeDTO.setId(TypeConstants.ID_ONE);
		typeDTO.setName("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						TypeAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(typeDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testUpdateNonUniqueName() {
		TypeUploadDTO typeDTO = this.testingTypeUploadDTO();
		typeDTO.setId(TypeConstants.ID_ONE);
		typeDTO.setName(TypeConstants.NAME_TWO);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						TypeAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(typeDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.UNIQUE_VIOLATION));
	}
	
	private Type testingType() {
		Type type = new Type();
		type.setCategory(categoryRepository.findByName(CategoryConstants.NAME_ONE));
		type.setName(TypeConstants.NON_EXISTING_NAME);
		return type;
	}
	
	private TypeUploadDTO testingTypeUploadDTO() {
		TypeUploadDTO type = new TypeUploadDTO();
		type.setCategory(CategoryConstants.NAME_ONE);
		type.setName(TypeConstants.NON_EXISTING_NAME);
		return type;
	}
	
	private HttpEntity<Object> httpEntity(Object obj){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", this.accessToken);		
		if (obj instanceof TypeUploadDTO) {
			TypeUploadDTO upload = (TypeUploadDTO) obj;
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("id", upload.getId());
			body.add("category", upload.getCategory());
			body.add("name", upload.getName());
			return new HttpEntity<Object>(body, headers);
		}
		return new HttpEntity<>(obj, headers);
	}

}
