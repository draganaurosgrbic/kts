package com.example.demo.controller;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

import com.example.demo.api.AuthAPI;
import com.example.demo.api.CommentAPI;
import com.example.demo.api.CulturalOfferAPI;
import com.example.demo.constants.CommentConstants;
import com.example.demo.constants.Constants;
import com.example.demo.constants.CulturalOfferConstants;
import com.example.demo.constants.MainConstants;
import com.example.demo.constants.UserConstants;
import com.example.demo.dto.CommentDTO;
import com.example.demo.dto.CommentUploadDTO;
import com.example.demo.dto.DoubleDTO;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.ProfileDTO;
import com.example.demo.exception.ExceptionConstants;
import com.example.demo.exception.ExceptionMessage;
import com.example.demo.model.Comment;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.CulturalOfferRepository;
import com.example.demo.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTest {
	
	@Autowired
	private TestRestTemplate restTemplate;
	private String accessToken;
	
	@Autowired 
	private CommentRepository commentRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CulturalOfferRepository culturalOfferRepository;
	
	private Pageable pageableTotal = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.TOTAL_SIZE);
	private Pageable pageablePart = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.PART_SIZE);
	private Pageable pageableNonExisting = PageRequest.of(MainConstants.ONE_SIZE, MainConstants.TOTAL_SIZE);
	private SimpleDateFormat format = new SimpleDateFormat(MainConstants.DATE_FORMAT);
	
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
	public void testListMore() {
		ResponseEntity<List<CommentDTO>> response = 
				this.restTemplate.exchange(
						CommentAPI.API_LIST(CulturalOfferConstants.ID_ONE, this.pageableTotal), 
						HttpMethod.GET, 
						this.httpEntity(null), 
						new ParameterizedTypeReference<List<CommentDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<CommentDTO> comments = response.getBody();
		assertEquals(MainConstants.TOTAL_SIZE, comments.size());
		assertEquals(CommentConstants.ID_TWO, comments.get(0).getId());
		assertEquals(CommentConstants.DATE_TWO, this.format.format(comments.get(0).getCreatedAt()));
		assertEquals(CommentConstants.TEXT_TWO, comments.get(0).getText());
		assertEquals(UserConstants.EMAIL_TWO, comments.get(0).getUser());
		assertEquals(CulturalOfferConstants.ID_ONE, comments.get(0).getCulturalOfferId());
		assertEquals(CommentConstants.ID_ONE, comments.get(1).getId());
		assertEquals(CommentConstants.DATE_ONE, this.format.format(comments.get(1).getCreatedAt()));
		assertEquals(CommentConstants.TEXT_ONE, comments.get(1).getText());
		assertEquals(UserConstants.EMAIL_ONE, comments.get(1).getUser());
		assertEquals(CulturalOfferConstants.ID_ONE, comments.get(1).getCulturalOfferId());
		assertEquals(CommentConstants.ID_THREE, comments.get(2).getId());
		assertEquals(CommentConstants.DATE_THREE, this.format.format(comments.get(2).getCreatedAt()));
		assertEquals(CommentConstants.TEXT_THREE, comments.get(2).getText());
		assertEquals(UserConstants.EMAIL_TWO, comments.get(2).getUser());
		assertEquals(CulturalOfferConstants.ID_ONE, comments.get(2).getCulturalOfferId());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	
	@Test
	public void testListMorePaginated() {
		ResponseEntity<List<CommentDTO>> response = 
				this.restTemplate.exchange(
						CommentAPI.API_LIST(CulturalOfferConstants.ID_ONE, this.pageablePart), 
						HttpMethod.GET, 
						this.httpEntity(null), 
						new ParameterizedTypeReference<List<CommentDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<CommentDTO> comments = response.getBody();
		assertEquals(MainConstants.PART_SIZE, comments.size());
		assertEquals(CommentConstants.ID_TWO, comments.get(0).getId());
		assertEquals(CommentConstants.DATE_TWO, this.format.format(comments.get(0).getCreatedAt()));
		assertEquals(CommentConstants.TEXT_TWO, comments.get(0).getText());
		assertEquals(UserConstants.EMAIL_TWO, comments.get(0).getUser());
		assertEquals(CulturalOfferConstants.ID_ONE, comments.get(0).getCulturalOfferId());
		assertEquals(CommentConstants.ID_ONE, comments.get(1).getId());
		assertEquals(CommentConstants.DATE_ONE, this.format.format(comments.get(1).getCreatedAt()));
		assertEquals(CommentConstants.TEXT_ONE, comments.get(1).getText());
		assertEquals(UserConstants.EMAIL_ONE, comments.get(1).getUser());
		assertEquals(CulturalOfferConstants.ID_ONE, comments.get(1).getCulturalOfferId());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("false", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void testListMoreNonExistingPage() {
		ResponseEntity<List<CommentDTO>> response = 
				this.restTemplate.exchange(
						CommentAPI.API_LIST(CulturalOfferConstants.ID_ONE, this.pageableNonExisting), 
						HttpMethod.GET, 
						this.httpEntity(null), 
						new ParameterizedTypeReference<List<CommentDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().isEmpty());
		assertEquals("false", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void testListOne() {
		ResponseEntity<List<CommentDTO>> response = 
				this.restTemplate.exchange(
						CommentAPI.API_LIST(CulturalOfferConstants.ID_TWO, this.pageableTotal), 
						HttpMethod.GET, 
						this.httpEntity(null), 
						new ParameterizedTypeReference<List<CommentDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<CommentDTO> comments = response.getBody();
		assertEquals(MainConstants.ONE_SIZE, comments.size());
		assertEquals(CommentConstants.ID_FOUR, comments.get(0).getId());
		assertEquals(CommentConstants.DATE_FOUR, this.format.format(comments.get(0).getCreatedAt()));
		assertEquals(CommentConstants.TEXT_FOUR, comments.get(0).getText());
		assertEquals(UserConstants.EMAIL_ONE, comments.get(0).getUser());
		assertEquals(CulturalOfferConstants.ID_TWO, comments.get(0).getCulturalOfferId());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void testListNone() {
		ResponseEntity<List<CommentDTO>> response = 
				this.restTemplate.exchange(
						CommentAPI.API_LIST(MainConstants.NON_EXISTING_ID, this.pageableTotal), 
						HttpMethod.GET, 
						this.httpEntity(null), 
						new ParameterizedTypeReference<List<CommentDTO>>() {});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().isEmpty());
		assertEquals("true", response.getHeaders().get(Constants.FIRST_PAGE_HEADER).get(0));
		assertEquals("true", response.getHeaders().get(Constants.LAST_PAGE_HEADER).get(0));
	}
	
	@Test
	public void testDeleteExisting() {
		long id = this.commentRepository.save(this.testingComment()).getId();
		long size = this.commentRepository.count();
		ResponseEntity<DoubleDTO> response = 
				this.restTemplate.exchange(
						CommentAPI.API_DELETE(id), 
						HttpMethod.DELETE, 
						this.httpEntity(null), 
						DoubleDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(size - 1, this.commentRepository.count());
		assertNull(this.commentRepository.findById(id).orElse(null));
		assertEquals(MainConstants.TOTAL_SIZE, response.getBody().getValue());
	}
	
	@Test
	public void testDeleteNonExisting() {
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CommentAPI.API_DELETE(MainConstants.NON_EXISTING_ID), 
						HttpMethod.DELETE, 
						this.httpEntity(null), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_FOUND, response.getBody().getMessage());
	}
	
	@Test
	public void testAddValid() {
		long size = this.commentRepository.count();
		CommentUploadDTO commentDTO = this.testingCommentDTO();
		ResponseEntity<DoubleDTO> response = 
				this.restTemplate.exchange(
						CommentAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(commentDTO), 
						DoubleDTO.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(MainConstants.TOTAL_SIZE, response.getBody().getValue());
		Comment comment = this.commentRepository.findAll().get((int) (this.commentRepository.count() - 1));
		assertEquals(size + 1, this.commentRepository.count());
		assertEquals(UserConstants.ID_ONE, comment.getUser().getId());
		assertEquals(CulturalOfferConstants.ID_ONE, comment.getCulturalOffer().getId());
		assertEquals(this.format.format(new Date()), this.format.format(comment.getCreatedAt()));
		assertEquals(CommentConstants.NON_EXISTING_TEXT, comment.getText());
		this.commentRepository.deleteById(comment.getId());
	}
	
	@Test
	public void testAddNonExistingOffer() {
		CommentUploadDTO commentDTO = this.testingCommentDTO();
		commentDTO.setCulturalOfferId(MainConstants.NON_EXISTING_ID);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(commentDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testAddNullText() {
		CommentUploadDTO commentDTO = this.testingCommentDTO();
		commentDTO.setText(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CommentAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(commentDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testAddEmptyText() {
		CommentUploadDTO commentDTO = this.testingCommentDTO();
		commentDTO.setText("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CommentAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(commentDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testAddBlankText() {
		CommentUploadDTO commentDTO = this.testingCommentDTO();
		commentDTO.setText("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CommentAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(commentDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}

	@Test
	public void testUpdateValid() {
		long id = this.commentRepository.save(this.testingComment()).getId();
		long size = this.commentRepository.count();
		CommentUploadDTO commentDTO = this.testingCommentDTO();
		commentDTO.setId(id);
		ResponseEntity<DoubleDTO> response = 
				this.restTemplate.exchange(
						CommentAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(commentDTO), 
						DoubleDTO.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(MainConstants.TOTAL_SIZE, response.getBody().getValue());
		Comment comment = this.commentRepository.findById(id).orElse(null);
		assertEquals(size, this.commentRepository.count());
		assertEquals(id, comment.getId());
		assertEquals(UserConstants.ID_ONE, comment.getUser().getId());
		assertEquals(CulturalOfferConstants.ID_ONE, comment.getCulturalOffer().getId());
		assertEquals(this.format.format(new Date()), this.format.format(comment.getCreatedAt()));
		assertEquals(CommentConstants.NON_EXISTING_TEXT, comment.getText());
		this.commentRepository.deleteById(id);	
	}
	
	@Test
	public void testUpdateNonExistingOffer() {
		CommentUploadDTO commentDTO = this.testingCommentDTO();
		commentDTO.setId(CulturalOfferConstants.ID_ONE);
		commentDTO.setCulturalOfferId(MainConstants.NON_EXISTING_ID);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CulturalOfferAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(commentDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getMessage().contains(ExceptionConstants.NOT_EMPTY_VIOLATION));
	}
	
	@Test
	public void testUpdateNullText() {
		CommentUploadDTO commentDTO = this.testingCommentDTO();
		commentDTO.setId(CulturalOfferConstants.ID_ONE);
		commentDTO.setText(null);
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CommentAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(commentDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testUpdateEmptyText() {
		CommentUploadDTO commentDTO = this.testingCommentDTO();
		commentDTO.setId(CulturalOfferConstants.ID_ONE);
		commentDTO.setText("");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CommentAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(commentDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	@Test
	public void testUpdateBlankText() {
		CommentUploadDTO commentDTO = this.testingCommentDTO();
		commentDTO.setId(CulturalOfferConstants.ID_ONE);
		commentDTO.setText("  ");
		ResponseEntity<ExceptionMessage> response = 
				this.restTemplate.exchange(
						CommentAPI.API_BASE, 
						HttpMethod.POST, 
						this.httpEntity(commentDTO), 
						ExceptionMessage.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ExceptionConstants.NOT_EMPTY_VIOLATION, response.getBody().getMessage());
	}
	
	private Comment testingComment() {
		Comment comment = new Comment();
		comment.setUser(this.userRepository.findById(UserConstants.ID_ONE).orElse(null));
		comment.setCulturalOffer(this.culturalOfferRepository.findById(CulturalOfferConstants.ID_ONE).orElse(null));
		comment.setText(CommentConstants.TEXT_ONE);
		return comment;
	}
	
	private CommentUploadDTO testingCommentDTO() {
		CommentUploadDTO comment = new CommentUploadDTO();
		comment.setCulturalOfferId(CulturalOfferConstants.ID_ONE);
		comment.setRate(MainConstants.TOTAL_SIZE);
		comment.setText(CommentConstants.NON_EXISTING_TEXT);
		return comment;
	}
	
	private HttpEntity<Object> httpEntity(Object obj){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", this.accessToken);			
		if (obj instanceof CommentUploadDTO) {
			CommentUploadDTO upload = (CommentUploadDTO) obj;
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("id", upload.getId());
			body.add("culturalOfferId", upload.getCulturalOfferId());
			body.add("rate", upload.getRate());
			body.add("text", upload.getText());
			return new HttpEntity<Object>(body, headers);
		}
		return new HttpEntity<>(obj, headers);
	}
	
}
