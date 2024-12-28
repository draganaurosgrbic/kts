package com.example.demo.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

import com.example.demo.constants.CommentConstants;
import com.example.demo.constants.CulturalOfferConstants;
import com.example.demo.constants.MainConstants;
import com.example.demo.constants.UserConstants;
import com.example.demo.model.Comment;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentRepositoryTest {

	@Autowired
	private CommentRepository commentRepository;
	
	private Pageable pageableTotal = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.TOTAL_SIZE);
	private Pageable pageablePart = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.PART_SIZE);
	private Pageable pageableNonExisting = PageRequest.of(MainConstants.ONE_SIZE, MainConstants.TOTAL_SIZE);
	private SimpleDateFormat format = new SimpleDateFormat(MainConstants.DATE_FORMAT);
	
	@Test
	public void testTotalRateMore() {
		double totalRate = 
				this.commentRepository
				.totalRate(CulturalOfferConstants.ID_ONE);
		assertEquals(MainConstants.TOTAL_SIZE, totalRate);
	}
	
	@Test
	public void testTotalRateOne() {
		double totalRate = 
				this.commentRepository
				.totalRate(CulturalOfferConstants.ID_TWO);
		assertEquals(MainConstants.ONE_SIZE, totalRate);
	}
	
	@Test
	public void testTotalRateNone() {
		double totalRate = 
				this.commentRepository
				.totalRate(MainConstants.NON_EXISTING_ID);
		assertEquals(MainConstants.NONE_SIZE, totalRate);
	}
	
	@Test
	public void testListMore() {
		Page<Comment> page = this.commentRepository
				.findByCulturalOfferIdOrderByCreatedAtDesc
				(CulturalOfferConstants.ID_ONE, this.pageableTotal);
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
				this.commentRepository
				.findByCulturalOfferIdOrderByCreatedAtDesc
				(CulturalOfferConstants.ID_ONE, this.pageablePart);
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
				this.commentRepository
				.findByCulturalOfferIdOrderByCreatedAtDesc
				(CulturalOfferConstants.ID_ONE, this.pageableNonExisting);
		List<Comment> comments = page.getContent();
		assertTrue(comments.isEmpty());
		assertFalse(page.isFirst());
		assertTrue(page.isLast());
	}
	
	@Test
	public void testListOne() {
		Page<Comment> page = 
				this.commentRepository
				.findByCulturalOfferIdOrderByCreatedAtDesc
				(CulturalOfferConstants.ID_TWO, this.pageableTotal);
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
				this.commentRepository
				.findByCulturalOfferIdOrderByCreatedAtDesc
				(CulturalOfferConstants.ID_THREE, this.pageableTotal);
		List<Comment> comments = page.getContent();
		assertTrue(comments.isEmpty());
		assertTrue(page.isFirst());
		assertTrue(page.isLast());
	}
	
}
