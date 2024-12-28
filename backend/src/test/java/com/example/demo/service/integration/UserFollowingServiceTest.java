package com.example.demo.service.integration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

import com.example.demo.constants.CulturalOfferConstants;
import com.example.demo.constants.MainConstants;
import com.example.demo.constants.Filters;
import com.example.demo.constants.TypeConstants;
import com.example.demo.constants.UserConstants;
import com.example.demo.dto.FilterParamsDTO;
import com.example.demo.model.CulturalOffer;
import com.example.demo.model.User;
import com.example.demo.repository.UserFollowingRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.AuthToken;
import com.example.demo.security.TokenUtils;
import com.example.demo.service.UserFollowingService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserFollowingServiceTest {

	@Autowired
	private UserFollowingService userFollowingService;
	
	@Autowired
	private UserFollowingRepository userFollowingRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TokenUtils tokenUtils;
	
	@Autowired
	private Filters filters;
	private Pageable pageableTotal = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.TOTAL_SIZE);
	private Pageable pageablePart = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.PART_SIZE);
	private Pageable pageableNonExisting = PageRequest.of(MainConstants.ONE_SIZE, MainConstants.TOTAL_SIZE);
	
	@Before
	public void setUp() {
		User user = this.userRepository.findById(UserConstants.ID_ONE).orElse(null);
		String token = this.tokenUtils.generateToken(user.getUsername());
		AuthToken authToken = new AuthToken(user, token);
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}
	
	@Test
	public void testFilterEmpty() {
		FilterParamsDTO filters = this.filters.filtersEmpty();
		Page<CulturalOffer> page = 
				this.userFollowingService
				.filter(filters, this.pageableTotal);
		List<CulturalOffer> offers = page.getContent();
		assertEquals(MainConstants.TOTAL_SIZE, offers.size());
		assertEquals(CulturalOfferConstants.ID_ONE, offers.get(0).getId());
		assertEquals(TypeConstants.ID_ONE, offers.get(0).getType().getId());
		assertEquals(CulturalOfferConstants.NAME_ONE, offers.get(0).getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offers.get(0).getLocation());
		assertEquals(CulturalOfferConstants.ID_TWO, offers.get(1).getId());
		assertEquals(TypeConstants.ID_TWO, offers.get(1).getType().getId());
		assertEquals(CulturalOfferConstants.NAME_TWO, offers.get(1).getName());
		assertEquals(CulturalOfferConstants.LOCATION_TWO, offers.get(1).getLocation());
		assertEquals(CulturalOfferConstants.ID_THREE, offers.get(2).getId());
		assertEquals(TypeConstants.ID_THREE, offers.get(2).getType().getId());
		assertEquals(CulturalOfferConstants.NAME_THREE, offers.get(2).getName());
		assertEquals(CulturalOfferConstants.LOCATION_THREE, offers.get(2).getLocation());
		assertTrue(page.isFirst());
		assertTrue(page.isLast());
	}
	
	@Test
	public void testFilterEmptyPaginated() {
		FilterParamsDTO filters = this.filters.filtersEmpty();
		Page<CulturalOffer> page = 
				this.userFollowingService
				.filter(filters, this.pageablePart);
		List<CulturalOffer> offers = page.getContent();
		assertEquals(MainConstants.PART_SIZE, offers.size());
		assertEquals(CulturalOfferConstants.ID_ONE, offers.get(0).getId());
		assertEquals(TypeConstants.ID_ONE, offers.get(0).getType().getId());
		assertEquals(CulturalOfferConstants.NAME_ONE, offers.get(0).getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offers.get(0).getLocation());
		assertEquals(CulturalOfferConstants.ID_TWO, offers.get(1).getId());
		assertEquals(TypeConstants.ID_TWO, offers.get(1).getType().getId());
		assertEquals(CulturalOfferConstants.NAME_TWO, offers.get(1).getName());
		assertEquals(CulturalOfferConstants.LOCATION_TWO, offers.get(1).getLocation());
		assertTrue(page.isFirst());
		assertFalse(page.isLast());
	}
	
	@Test
	public void testFilterEmptyNonExistingPage() {
		FilterParamsDTO filters = this.filters.filtersEmpty();
		Page<CulturalOffer> page = 
				this.userFollowingService
				.filter(filters, this.pageableNonExisting);
		List<CulturalOffer> offers = page.getContent();
		assertTrue(offers.isEmpty());
		assertFalse(page.isFirst());
		assertTrue(page.isLast());
	}
	
	@Test
	public void testFilterAll() {
		FilterParamsDTO filters = this.filters.filtersAll();
		Page<CulturalOffer> page = 
				this.userFollowingService
				.filter(filters, this.pageableTotal);
		List<CulturalOffer> offers = page.getContent();
		assertEquals(MainConstants.TOTAL_SIZE, offers.size());
		assertEquals(CulturalOfferConstants.ID_ONE, offers.get(0).getId());
		assertEquals(TypeConstants.ID_ONE, offers.get(0).getType().getId());
		assertEquals(CulturalOfferConstants.NAME_ONE, offers.get(0).getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offers.get(0).getLocation());
		assertEquals(CulturalOfferConstants.ID_TWO, offers.get(1).getId());
		assertEquals(TypeConstants.ID_TWO, offers.get(1).getType().getId());
		assertEquals(CulturalOfferConstants.NAME_TWO, offers.get(1).getName());
		assertEquals(CulturalOfferConstants.LOCATION_TWO, offers.get(1).getLocation());
		assertEquals(CulturalOfferConstants.ID_THREE, offers.get(2).getId());
		assertEquals(TypeConstants.ID_THREE, offers.get(2).getType().getId());
		assertEquals(CulturalOfferConstants.NAME_THREE, offers.get(2).getName());
		assertEquals(CulturalOfferConstants.LOCATION_THREE, offers.get(2).getLocation());
		assertTrue(page.isFirst());
		assertTrue(page.isLast());
	}
	
	@Test
	public void testFilterAllPaginated() {
		FilterParamsDTO filters = this.filters.filtersAll();
		Page<CulturalOffer> page = 
				this.userFollowingService
				.filter(filters, this.pageablePart);
		List<CulturalOffer> offers = page.getContent();
		assertEquals(MainConstants.PART_SIZE, offers.size());
		assertEquals(CulturalOfferConstants.ID_ONE, offers.get(0).getId());
		assertEquals(TypeConstants.ID_ONE, offers.get(0).getType().getId());
		assertEquals(CulturalOfferConstants.NAME_ONE, offers.get(0).getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offers.get(0).getLocation());
		assertEquals(CulturalOfferConstants.ID_TWO, offers.get(1).getId());
		assertEquals(TypeConstants.ID_TWO, offers.get(1).getType().getId());
		assertEquals(CulturalOfferConstants.NAME_TWO, offers.get(1).getName());
		assertEquals(CulturalOfferConstants.LOCATION_TWO, offers.get(1).getLocation());
		assertTrue(page.isFirst());
		assertFalse(page.isLast());
	}
	
	@Test
	public void testFilterAllNonExistingPage() {
		FilterParamsDTO filters = this.filters.filtersAll();
		Page<CulturalOffer> page = 
				this.userFollowingService
				.filter(filters, this.pageableNonExisting);
		List<CulturalOffer> offers = page.getContent();
		assertTrue(offers.isEmpty());
		assertFalse(page.isFirst());
		assertTrue(page.isLast());
	}
	
	@Test
	public void testFilterOneName() {
		FilterParamsDTO filters = this.filters.filtersOneName();
		Page<CulturalOffer> page = 
				this.userFollowingService
				.filter(filters, this.pageableTotal);
		List<CulturalOffer> offers = page.getContent();
		assertEquals(MainConstants.ONE_SIZE, offers.size());
		assertEquals(CulturalOfferConstants.ID_ONE, offers.get(0).getId());
		assertEquals(TypeConstants.ID_ONE, offers.get(0).getType().getId());
		assertEquals(CulturalOfferConstants.NAME_ONE, offers.get(0).getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offers.get(0).getLocation());
		assertTrue(page.isFirst());
		assertTrue(page.isLast());
	}
	
	@Test
	public void testFilterOneLocation() {
		FilterParamsDTO filters = this.filters.filtersOneLocation();
		Page<CulturalOffer> page = 
				this.userFollowingService
				.filter(filters, this.pageableTotal);
		List<CulturalOffer> offers = page.getContent();
		assertEquals(MainConstants.ONE_SIZE, offers.size());
		assertEquals(CulturalOfferConstants.ID_ONE, offers.get(0).getId());
		assertEquals(TypeConstants.ID_ONE, offers.get(0).getType().getId());
		assertEquals(CulturalOfferConstants.NAME_ONE, offers.get(0).getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offers.get(0).getLocation());
		assertTrue(page.isFirst());
		assertTrue(page.isLast());
	}
	
	@Test
	public void testFilterOneType() {
		FilterParamsDTO filters = this.filters.filtersOneType();
		Page<CulturalOffer> page = 
				this.userFollowingService
				.filter(filters, this.pageableTotal);
		List<CulturalOffer> offers = page.getContent();
		assertEquals(MainConstants.ONE_SIZE, offers.size());
		assertEquals(CulturalOfferConstants.ID_ONE, offers.get(0).getId());
		assertEquals(TypeConstants.ID_ONE, offers.get(0).getType().getId());
		assertEquals(CulturalOfferConstants.NAME_ONE, offers.get(0).getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offers.get(0).getLocation());
		assertTrue(page.isFirst());
		assertTrue(page.isLast());
	}

	@Test
	public void testFilterNone() {
		FilterParamsDTO filters = this.filters.filtersNone();
		Page<CulturalOffer> page = 
				this.userFollowingService
				.filter(filters, this.pageableTotal);
		List<CulturalOffer> offers = page.getContent();
		assertTrue(offers.isEmpty());
		assertTrue(page.isFirst());
		assertTrue(page.isLast());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testToggleSubscriptionExisting() {
		this.userFollowingService.toggleSubscription(CulturalOfferConstants.ID_ONE);
		assertNull(this.userFollowingRepository
				.findByUserIdAndCulturalOfferId
				(UserConstants.ID_ONE, CulturalOfferConstants.ID_ONE));
		this.userFollowingService.toggleSubscription(CulturalOfferConstants.ID_ONE);
		assertNotNull(this.userFollowingRepository
				.findByUserIdAndCulturalOfferId
				(UserConstants.ID_ONE, CulturalOfferConstants.ID_ONE));
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testToggleSubscriptionNonExisting() {
		this.userFollowingService.toggleSubscription(MainConstants.NON_EXISTING_ID);
	}
	
}
