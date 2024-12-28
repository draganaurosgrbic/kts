package com.example.demo.service.integration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constants.CommentConstants;
import com.example.demo.constants.CulturalOfferConstants;
import com.example.demo.constants.MainConstants;
import com.example.demo.constants.UserConstants;
import com.example.demo.model.Comment;
import com.example.demo.model.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.CulturalOfferRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.AuthToken;
import com.example.demo.security.TokenUtils;
import com.example.demo.service.CommentService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentServiceTest {

	@Autowired
	private CommentService commentService;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CulturalOfferRepository culturalOfferRepository;
	
	@Autowired
	private TokenUtils tokenUtils;

	private Pageable pageableTotal = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.TOTAL_SIZE);
	private Pageable pageablePart = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.PART_SIZE);
	private Pageable pageableNonExisting = PageRequest.of(MainConstants.ONE_SIZE, MainConstants.TOTAL_SIZE);
	private SimpleDateFormat format = new SimpleDateFormat(MainConstants.DATE_FORMAT);
	
	@Before
	public void setUp() {
		User user = this.userRepository.findById(UserConstants.ID_ONE).orElse(null);
		String token = this.tokenUtils.generateToken(user.getUsername());
		AuthToken authToken = new AuthToken(user, token);
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}

	@Test
	public void testTotalRateMore() {
		double totalRate = 
				this.commentService
				.totalRate(CulturalOfferConstants.ID_ONE);
		assertEquals(MainConstants.TOTAL_SIZE, totalRate);
	}
	
	@Test
	public void testTotalRateOne() {
		double totalRate = 
				this.commentService
				.totalRate(CulturalOfferConstants.ID_TWO);
		assertEquals(MainConstants.ONE_SIZE, totalRate);
	}
		
	@Test
	public void testTotalRateNone() {
		double totalRate = 
				this.commentService
				.totalRate(MainConstants.NON_EXISTING_ID);
		assertEquals(MainConstants.NONE_SIZE, totalRate);
	}

	@Test
	public void testListMore() {
		Page<Comment> page = 
				this.commentService
				.list(CulturalOfferConstants.ID_ONE, this.pageableTotal);
		List<Comment> comments = page.getContent();
		assertEquals(MainConstants.TOTAL_SIZE, comments.size());
		assertEquals(CommentConstants.ID_TWO, comments.get(0).getId());
		assertEquals(CommentConstants.DATE_TWO, this.format.format(comments.get(0).getCreatedAt()));
		assertEquals(CommentConstants.TEXT_TWO, comments.get(0).getText());
		assertEquals(UserConstants.ID_TWO, comments.get(0).getUser().getId());
		assertEquals(CulturalOfferConstants.ID_ONE, comments.get(0).getCulturalOffer().getId());
		assertEquals(CommentConstants.ID_ONE, comments.get(1).getId());
		assertEquals(CommentConstants.DATE_ONE, this.format.format(comments.get(1).getCreatedAt()));
		assertEquals(CommentConstants.TEXT_ONE, comments.get(1).getText());
		assertEquals(UserConstants.ID_ONE, comments.get(1).getUser().getId());
		assertEquals(CulturalOfferConstants.ID_ONE, comments.get(1).getCulturalOffer().getId());
		assertEquals(CommentConstants.ID_THREE, comments.get(2).getId());
		assertEquals(CommentConstants.DATE_THREE, this.format.format(comments.get(2).getCreatedAt()));
		assertEquals(CommentConstants.TEXT_THREE, comments.get(2).getText());
		assertEquals(UserConstants.ID_TWO, comments.get(2).getUser().getId());
		assertEquals(CulturalOfferConstants.ID_ONE, comments.get(2).getCulturalOffer().getId());
		assertTrue(page.isFirst());
		assertTrue(page.isLast());
	}
	
	@Test
	public void testListMorePaginated() {
		Page<Comment> page = 
				this.commentService
				.list(CulturalOfferConstants.ID_ONE, this.pageablePart);
		List<Comment> comments = page.getContent();
		assertEquals(MainConstants.PART_SIZE, comments.size());
		assertEquals(CommentConstants.ID_TWO, comments.get(0).getId());
		assertEquals(CommentConstants.DATE_TWO, this.format.format(comments.get(0).getCreatedAt()));
		assertEquals(CommentConstants.TEXT_TWO, comments.get(0).getText());
		assertEquals(UserConstants.ID_TWO, comments.get(0).getUser().getId());
		assertEquals(CulturalOfferConstants.ID_ONE, comments.get(0).getCulturalOffer().getId());
		assertEquals(CommentConstants.ID_ONE, comments.get(1).getId());
		assertEquals(CommentConstants.DATE_ONE, this.format.format(comments.get(1).getCreatedAt()));
		assertEquals(CommentConstants.TEXT_ONE, comments.get(1).getText());
		assertEquals(UserConstants.ID_ONE, comments.get(1).getUser().getId());
		assertEquals(CulturalOfferConstants.ID_ONE, comments.get(1).getCulturalOffer().getId());
		assertTrue(page.isFirst());
		assertFalse(page.isLast());
	}
	
	@Test
	public void testListMoreNonExistingPage() {
		Page<Comment> page = 
				this.commentService
				.list(CulturalOfferConstants.ID_ONE, this.pageableNonExisting);
		List<Comment> comments = page.getContent();
		assertTrue(comments.isEmpty());
		assertFalse(page.isFirst());
		assertTrue(page.isLast());
	}
	
	@Test
	public void testListOne() {
		Page<Comment> page = 
				this.commentService
				.list(CulturalOfferConstants.ID_TWO, this.pageableTotal);
		List<Comment> comments = page.getContent();
		assertEquals(MainConstants.ONE_SIZE, comments.size());
		assertEquals(CommentConstants.ID_FOUR, comments.get(0).getId());
		assertEquals(CommentConstants.DATE_FOUR, this.format.format(comments.get(0).getCreatedAt()));
		assertEquals(CommentConstants.TEXT_FOUR, comments.get(0).getText());
		assertEquals(UserConstants.ID_ONE, comments.get(0).getUser().getId());
		assertEquals(CulturalOfferConstants.ID_TWO, comments.get(0).getCulturalOffer().getId());
		assertTrue(page.isFirst());
		assertTrue(page.isLast());
	}
	
	@Test
	public void testListNone() {
		Page<Comment> page = 
				this.commentService
				.list(MainConstants.NON_EXISTING_ID, this.pageableTotal);
		List<Comment> comments = page.getContent();
		assertTrue(comments.isEmpty());
		assertTrue(page.isFirst());
		assertTrue(page.isLast());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testDeleteExisting() {
		long size = this.commentRepository.count();
		this.commentService.delete(CommentConstants.ID_ONE);
		assertEquals(size - 1, this.commentRepository.count());
		assertNull(this.commentRepository.findById(CommentConstants.ID_ONE).orElse(null));
	}
	
	@Test(expected = NullPointerException.class)
	@Transactional
	@Rollback(true)
	public void testDeleteNonExisting() {
		this.commentService.delete(MainConstants.NON_EXISTING_ID);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testAddValid() {
		long size = this.commentRepository.count();
		Comment comment = this.testingComment();
		comment = this.commentService.save(comment, null);
		assertEquals(size + 1, this.commentRepository.count());
		assertEquals(UserConstants.ID_ONE, comment.getUser().getId());
		assertEquals(CulturalOfferConstants.ID_ONE, comment.getCulturalOffer().getId());
		assertEquals(this.format.format(new Date()), this.format.format(comment.getCreatedAt()));
		assertEquals(CommentConstants.TEXT_ONE, comment.getText());
	}
		
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddNullUser() {
		Comment comment = this.testingComment();
		comment.setUser(null);
		this.commentService.save(comment, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddNullOffer() {
		Comment comment = this.testingComment();
		comment.setCulturalOffer(null);
		this.commentService.save(comment, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddNullDate() {
		Comment comment = this.testingComment();
		comment.setCreatedAt(null);
		this.commentService.save(comment, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddNullText() {
		Comment comment = this.testingComment();
		comment.setText(null);
		this.commentService.save(comment, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddEmptyText() {
		Comment comment = this.testingComment();
		comment.setText("");
		this.commentService.save(comment, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddBlankText() {
		Comment comment = this.testingComment();
		comment.setText("  ");
		this.commentService.save(comment, null);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testUpdateValid() {
		long size = this.commentRepository.count();
		Comment comment = this.testingComment();
		comment.setId(CommentConstants.ID_ONE);
		comment = this.commentService.save(comment, null);
		assertEquals(size, this.commentRepository.count());
		assertEquals(CommentConstants.ID_ONE, comment.getId());
		assertEquals(UserConstants.ID_ONE, comment.getUser().getId());
		assertEquals(CulturalOfferConstants.ID_ONE, comment.getUser().getId());
		assertEquals(this.format.format(new Date()), this.format.format(comment.getCreatedAt()));
		assertEquals(CommentConstants.TEXT_ONE, comment.getText());
	}
	
	@Test(expected = NullPointerException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateNullUser() {
		Comment comment = this.testingComment();
		comment.setId(CommentConstants.ID_ONE);
		comment.setUser(null);
		this.commentService.save(comment, null);
		this.commentRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateNullOffer() {
		Comment comment = this.testingComment();
		comment.setId(CommentConstants.ID_ONE);
		comment.setCulturalOffer(null);
		this.commentService.save(comment, null);
		this.commentRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateNullDate() {
		Comment comment = this.testingComment();
		comment.setId(CommentConstants.ID_ONE);
		comment.setCreatedAt(null);
		this.commentService.save(comment, null);
		this.commentRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateNullText() {
		Comment comment = this.testingComment();
		comment.setId(CommentConstants.ID_ONE);
		comment.setText(null);
		this.commentService.save(comment, null);
		this.commentRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateEmptyText() {
		Comment comment = this.testingComment();
		comment.setId(CommentConstants.ID_ONE);
		comment.setText("");
		this.commentService.save(comment, null);
		this.commentRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateBlankText() {
		Comment comment = this.testingComment();
		comment.setId(CommentConstants.ID_ONE);
		comment.setText("  ");
		this.commentService.save(comment, null);
		this.commentRepository.count();
	}
	
	private Comment testingComment() {
		Comment comment = new Comment();
		comment.setUser(this.userRepository.findById(UserConstants.ID_ONE).orElse(null));
		comment.setCulturalOffer(this.culturalOfferRepository.findById(CulturalOfferConstants.ID_ONE).orElse(null));
		comment.setCreatedAt(new Date());
		comment.setText(CommentConstants.TEXT_ONE);
		return comment;
	}

}
