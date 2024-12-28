package com.example.demo.service.unit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
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
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.constants.CommentConstants;
import com.example.demo.constants.CulturalOfferConstants;
import com.example.demo.constants.MainConstants;
import com.example.demo.constants.UserConstants;
import com.example.demo.model.Comment;
import com.example.demo.model.CulturalOffer;
import com.example.demo.model.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.service.CommentService;
import com.example.demo.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentServiceTest {

	@Autowired
	private CommentService commentService;
	
	@MockBean
	private CommentRepository commentRepository;
	
	@MockBean
	private UserService userService;

	private Pageable pageableTotal = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.TOTAL_SIZE);
	private Pageable pageablePart = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.PART_SIZE);
	private Pageable pageableNonExisting = PageRequest.of(MainConstants.ONE_SIZE, MainConstants.TOTAL_SIZE);
	private SimpleDateFormat format = new SimpleDateFormat(MainConstants.DATE_FORMAT);
	
	@Before
	public void setUp() {
		User user = new User();
		user.setId(UserConstants.ID_ONE);
		Mockito.when(this.userService.currentUser())
		.thenReturn(user);
	}

	@Test
	public void testTotalRateMore() {
		Mockito.when(this.commentRepository.totalRate(CulturalOfferConstants.ID_ONE))
		.thenReturn((double) MainConstants.TOTAL_SIZE);
		double totalRate = 
				this.commentService
				.totalRate(CulturalOfferConstants.ID_ONE);
		assertEquals(MainConstants.TOTAL_SIZE, totalRate);
	}
	
	@Test
	public void testTotalRateOne() {
		Mockito.when(this.commentRepository.totalRate(CulturalOfferConstants.ID_TWO))
		.thenReturn((double) MainConstants.ONE_SIZE);
		double totalRate = 
				this.commentService
				.totalRate(CulturalOfferConstants.ID_TWO);
		assertEquals(MainConstants.ONE_SIZE, totalRate);
	}
		
	@Test
	public void testTotalRateNone() {
		Mockito.when(this.commentRepository.totalRate(MainConstants.NON_EXISTING_ID))
		.thenReturn((double) MainConstants.NONE_SIZE);
		double totalRate = 
				this.commentService
				.totalRate(MainConstants.NON_EXISTING_ID);
		assertEquals(MainConstants.NONE_SIZE, totalRate);
	}

	@Test
	public void testListMore() {
		Mockito.when(this.commentRepository
				.findByCulturalOfferIdOrderByCreatedAtDesc(CulturalOfferConstants.ID_ONE, this.pageableTotal))
				.thenReturn(new PageImpl<>(List.of(
						this.filterComment(2), 
						this.filterComment(1), 
						this.filterComment(3))));
		List<Comment> comments = 
				this.commentService
				.list(CulturalOfferConstants.ID_ONE, this.pageableTotal).getContent();
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
	}
	
	@Test
	public void testListMorePaginated() {
		Mockito.when(this.commentRepository
				.findByCulturalOfferIdOrderByCreatedAtDesc(CulturalOfferConstants.ID_ONE, this.pageablePart))
				.thenReturn(new PageImpl<>(List.of(
						this.filterComment(2), 
						this.filterComment(1))));
		List<Comment> comments = 
				this.commentService
				.list(CulturalOfferConstants.ID_ONE, this.pageablePart).getContent();
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
	}
	
	@Test
	public void testListMoreNonExistingPage() {
		Mockito.when(this.commentRepository
				.findByCulturalOfferIdOrderByCreatedAtDesc(CulturalOfferConstants.ID_ONE, this.pageableNonExisting))
				.thenReturn(new PageImpl<>(List.of()));
		List<Comment> comments = 
				this.commentService
				.list(CulturalOfferConstants.ID_ONE, this.pageableNonExisting).getContent();
		assertTrue(comments.isEmpty());
	}
	
	@Test
	public void testListOne() {
		Mockito.when(this.commentRepository
				.findByCulturalOfferIdOrderByCreatedAtDesc(CulturalOfferConstants.ID_TWO, this.pageableTotal))
				.thenReturn(new PageImpl<>(List.of(this.filterComment(4))));
		List<Comment> comments = 
				this.commentService
				.list(CulturalOfferConstants.ID_TWO, this.pageableTotal).getContent();
		assertEquals(MainConstants.ONE_SIZE, comments.size());
		assertEquals(CommentConstants.ID_FOUR, comments.get(0).getId());
		assertEquals(CommentConstants.DATE_FOUR, this.format.format(comments.get(0).getCreatedAt()));
		assertEquals(CommentConstants.TEXT_FOUR, comments.get(0).getText());
		assertEquals(UserConstants.ID_ONE, comments.get(0).getUser().getId());
		assertEquals(CulturalOfferConstants.ID_TWO, comments.get(0).getCulturalOffer().getId());
	}
	
	@Test
	public void testListNone() {
		Mockito.when(this.commentRepository
				.findByCulturalOfferIdOrderByCreatedAtDesc(MainConstants.NON_EXISTING_ID, this.pageableTotal))
				.thenReturn(new PageImpl<>(List.of()));
		List<Comment> comments = 
				this.commentService
				.list(MainConstants.NON_EXISTING_ID, this.pageableTotal).getContent();
		assertTrue(comments.isEmpty());
	}
	
	@Test
	public void testDeleteExisting() {
		Comment comment = new Comment();
		User user = new User();
		user.setId(UserConstants.ID_ONE);
		CulturalOffer offer = new CulturalOffer();
		offer.setId(MainConstants.NON_EXISTING_ID);
		comment.setUser(user);
		comment.setCulturalOffer(offer);
		Mockito.when(this.commentRepository.findById(CommentConstants.ID_ONE))
		.thenReturn(Optional.of(comment));
		Mockito.when(this.commentRepository.count())
		.thenReturn((long) (MainConstants.TOTAL_SIZE + 1));
		long size = this.commentRepository.count();
		this.commentService.delete(CommentConstants.ID_ONE);
		Mockito.when(this.commentRepository.count())
		.thenReturn(size - 1);
		Mockito.when(this.commentRepository.findById(CulturalOfferConstants.ID_ONE))
		.thenReturn(Optional.empty());
		assertEquals(size - 1, this.commentRepository.count());
		assertNull(this.commentRepository.findById(CommentConstants.ID_ONE).orElse(null));
	}
	
	@Test(expected = NullPointerException.class)
	public void testDeleteNonExisting() {
		Comment comment = new Comment();
		CulturalOffer offer = new CulturalOffer();
		offer.setId(MainConstants.NON_EXISTING_ID);
		comment.setCulturalOffer(offer);
		Mockito.when(this.commentRepository.findById(MainConstants.NON_EXISTING_ID))
		.thenReturn(Optional.of(comment));
		Mockito.doThrow(EmptyResultDataAccessException.class).when(this.commentRepository)
		.deleteById(MainConstants.NON_EXISTING_ID);
		this.commentService.delete(MainConstants.NON_EXISTING_ID);
	}
	
	@Test
	public void testAddValid() {
		Mockito.when(this.commentRepository.count())
		.thenReturn((long) (MainConstants.TOTAL_SIZE + 1));
		long size = this.commentRepository.count();
		Comment comment = this.testingComment();
		Mockito.when(this.commentRepository.save(comment)).thenReturn(comment);
		comment = this.commentService.save(comment, null);
		Mockito.when(this.commentRepository.count())
		.thenReturn(size + 1);
		assertEquals(size + 1, this.commentRepository.count());
		assertEquals(UserConstants.ID_ONE, comment.getUser().getId());
		assertEquals(CulturalOfferConstants.ID_ONE, comment.getCulturalOffer().getId());
		assertEquals(this.format.format(new Date()), this.format.format(comment.getCreatedAt()));
		assertEquals(CommentConstants.TEXT_ONE, comment.getText());
	}
		
	@Test(expected = ConstraintViolationException.class)
	public void testAddNullUser() {
		Comment comment = this.testingComment();
		comment.setUser(null);
		Mockito.when(this.commentRepository.save(comment))
		.thenThrow(ConstraintViolationException.class);
		this.commentService.save(comment, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddNullOffer() {
		Comment comment = this.testingComment();
		comment.setCulturalOffer(null);
		Mockito.when(this.commentRepository.save(comment))
		.thenThrow(ConstraintViolationException.class);
		this.commentService.save(comment, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddNullDate() {
		Comment comment = this.testingComment();
		comment.setCreatedAt(null);
		Mockito.when(this.commentRepository.save(comment))
		.thenThrow(ConstraintViolationException.class);
		this.commentService.save(comment, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddNullText() {
		Comment comment = this.testingComment();
		comment.setText(null);
		Mockito.when(this.commentRepository.save(comment))
		.thenThrow(ConstraintViolationException.class);
		this.commentService.save(comment, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddEmptyText() {
		Comment comment = this.testingComment();
		comment.setText("");
		Mockito.when(this.commentRepository.save(comment))
		.thenThrow(ConstraintViolationException.class);
		this.commentService.save(comment, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddBlankText() {
		Comment comment = this.testingComment();
		comment.setText("  ");
		Mockito.when(this.commentRepository.save(comment))
		.thenThrow(ConstraintViolationException.class);
		this.commentService.save(comment, null);
	}
	
	@Test
	public void testUpdateValid() {
		Mockito.when(this.commentRepository.count())
		.thenReturn((long) (MainConstants.TOTAL_SIZE + 1));
		long size = this.commentRepository.count();
		Comment comment = this.testingComment();
		comment.setId(CommentConstants.ID_ONE);
		Mockito.when(this.commentRepository.save(comment))
		.thenReturn(comment);
		comment = this.commentService.save(comment, null);
		assertEquals(size, this.commentRepository.count());
		assertEquals(CommentConstants.ID_ONE, comment.getId());
		assertEquals(UserConstants.ID_ONE, comment.getUser().getId());
		assertEquals(CulturalOfferConstants.ID_ONE, comment.getUser().getId());
		assertEquals(this.format.format(new Date()), this.format.format(comment.getCreatedAt()));
		assertEquals(CommentConstants.TEXT_ONE, comment.getText());
	}
	
	@Test(expected = NullPointerException.class)
	public void testUpdateNullUser() {
		Comment comment = this.testingComment();
		comment.setId(CommentConstants.ID_ONE);
		comment.setUser(null);
		Mockito.when(this.commentRepository.save(comment))
		.thenThrow(ConstraintViolationException.class);
		this.commentService.save(comment, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateNullOffer() {
		Comment comment = this.testingComment();
		comment.setId(CommentConstants.ID_ONE);
		comment.setCulturalOffer(null);
		Mockito.when(this.commentRepository.save(comment))
		.thenThrow(ConstraintViolationException.class);
		this.commentService.save(comment, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateNullDate() {
		Comment comment = this.testingComment();
		comment.setId(CommentConstants.ID_ONE);
		comment.setCreatedAt(null);
		Mockito.when(this.commentRepository.save(comment))
		.thenThrow(ConstraintViolationException.class);
		this.commentService.save(comment, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateNullText() {
		Comment comment = this.testingComment();
		comment.setId(CommentConstants.ID_ONE);
		comment.setText(null);
		Mockito.when(this.commentRepository.save(comment))
		.thenThrow(ConstraintViolationException.class);
		this.commentService.save(comment, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateEmptyText() {
		Comment comment = this.testingComment();
		comment.setId(CommentConstants.ID_ONE);
		comment.setText("");
		Mockito.when(this.commentRepository.save(comment))
		.thenThrow(ConstraintViolationException.class);
		this.commentService.save(comment, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateBlankText() {
		Comment comment = this.testingComment();
		comment.setId(CommentConstants.ID_ONE);
		comment.setText("  ");
		Mockito.when(this.commentRepository.save(comment))
		.thenThrow(ConstraintViolationException.class);
		this.commentService.save(comment, null);
	}
	
	private Comment testingComment() {
		Comment comment = new Comment();
		User user = new User();
		user.setId(UserConstants.ID_ONE);
		comment.setUser(user);
		CulturalOffer offer = new CulturalOffer();
		offer.setId(CulturalOfferConstants.ID_ONE);
		comment.setCulturalOffer(offer);
		comment.setCreatedAt(new Date());
		comment.setText(CommentConstants.TEXT_ONE);
		return comment;
	}
	
	private Comment filterComment(int index) {
		if (index == 1) {
			Comment comment = new Comment();
			comment.setId(CommentConstants.ID_ONE);
			User user = new User();
			user.setId(UserConstants.ID_ONE);
			comment.setUser(user);
			CulturalOffer offer = new CulturalOffer();
			offer.setId(CulturalOfferConstants.ID_ONE);
			comment.setCulturalOffer(offer);
			try {
				comment.setCreatedAt(this.format.parse(CommentConstants.DATE_ONE));				
			}catch(Exception e) {};
			comment.setText(CommentConstants.TEXT_ONE);
			return comment;
		}
		if (index == 2) {
			Comment comment = new Comment();
			comment.setId(CommentConstants.ID_TWO);
			User user = new User();
			user.setId(UserConstants.ID_TWO);
			comment.setUser(user);
			CulturalOffer offer = new CulturalOffer();
			offer.setId(CulturalOfferConstants.ID_ONE);
			comment.setCulturalOffer(offer);
			try {
				comment.setCreatedAt(this.format.parse(CommentConstants.DATE_TWO));				
			}catch(Exception e) {};
			comment.setText(CommentConstants.TEXT_TWO);
			return comment;
		}
		if (index == 3) {
			Comment comment = new Comment();
			comment.setId(CommentConstants.ID_THREE);
			User user = new User();
			user.setId(UserConstants.ID_TWO);
			comment.setUser(user);
			CulturalOffer offer = new CulturalOffer();
			offer.setId(CulturalOfferConstants.ID_ONE);
			comment.setCulturalOffer(offer);
			try {
				comment.setCreatedAt(this.format.parse(CommentConstants.DATE_THREE));				
			}catch(Exception e) {};
			comment.setText(CommentConstants.TEXT_THREE);
			return comment;
		}
		Comment comment = new Comment();
		comment.setId(CommentConstants.ID_FOUR);
		User user = new User();
		user.setId(UserConstants.ID_ONE);
		comment.setUser(user);
		CulturalOffer offer = new CulturalOffer();
		offer.setId(CulturalOfferConstants.ID_TWO);
		comment.setCulturalOffer(offer);
		try {
			comment.setCreatedAt(this.format.parse(CommentConstants.DATE_FOUR));				
		}catch(Exception e) {};
		comment.setText(CommentConstants.TEXT_FOUR);
		return comment;
	}

}
