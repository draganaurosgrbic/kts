package com.example.demo.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.constants.CulturalOfferConstants;
import com.example.demo.constants.MainConstants;
import com.example.demo.constants.TypeConstants;
import com.example.demo.constants.UserConstants;
import com.example.demo.model.CulturalOffer;
import com.example.demo.model.UserFollowing;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserFollowingRepositoryTest {
	
	@Autowired
	private UserFollowingRepository userFollowingRepository;
	
	private Pageable pageableTotal = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.TOTAL_SIZE);
	private Pageable pageablePart = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.PART_SIZE);
	private Pageable pageableNonExisting = PageRequest.of(MainConstants.ONE_SIZE, MainConstants.TOTAL_SIZE);

	@Test
	public void testFindExisting() {
		UserFollowing userFollowing = 
				this.userFollowingRepository
				.findByUserIdAndCulturalOfferId
				(UserConstants.ID_ONE, CulturalOfferConstants.ID_ONE);
		assertNotNull(userFollowing);
	}
	
	@Test
	public void testFindNonExisting() {
		UserFollowing userFollowing = 
				this.userFollowingRepository
				.findByUserIdAndCulturalOfferId
				(UserConstants.ID_TWO, CulturalOfferConstants.ID_TWO);
		assertNull(userFollowing);
	}
	
	@Test
	public void testFilterEmpty() {
		Page<CulturalOffer> page = 
				this.userFollowingRepository
				.filter(UserConstants.ID_ONE, 
						MainConstants.FILTER_ALL, 
						MainConstants.FILTER_ALL, 
						MainConstants.FILTER_ALL, 
						this.pageableTotal);
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
		Page<CulturalOffer> page = 
				this.userFollowingRepository
				.filter(UserConstants.ID_ONE, 
						MainConstants.FILTER_ALL, 
						MainConstants.FILTER_ALL, 
						MainConstants.FILTER_ALL, 
						this.pageablePart);
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
		Page<CulturalOffer> page = 
				this.userFollowingRepository
				.filter(UserConstants.ID_ONE, 
						MainConstants.FILTER_ALL, 
						MainConstants.FILTER_ALL, 
						MainConstants.FILTER_ALL, 
						this.pageableNonExisting);
		List<CulturalOffer> offers = page.getContent();
		assertTrue(offers.isEmpty());
		assertFalse(page.isFirst());
		assertTrue(page.isLast());
	}
	
	@Test
	public void testFilterAll() {
		Page<CulturalOffer> page = 
				this.userFollowingRepository
				.filter(UserConstants.ID_ONE, 
						CulturalOfferConstants.FILTER_NAMES_ALL, 
						CulturalOfferConstants.FILTER_LOCATIONS_ALL, 
						CulturalOfferConstants.FILTER_TYPES_ALL, 
						this.pageableTotal);
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
		Page<CulturalOffer> page = 
				this.userFollowingRepository
				.filter(UserConstants.ID_ONE, 
						CulturalOfferConstants.FILTER_NAMES_ALL, 
						CulturalOfferConstants.FILTER_LOCATIONS_ALL, 
						CulturalOfferConstants.FILTER_TYPES_ALL, 
						this.pageablePart);
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
		Page<CulturalOffer> page = 
				this.userFollowingRepository
				.filter(UserConstants.ID_ONE, 
						CulturalOfferConstants.FILTER_NAMES_ALL, 
						CulturalOfferConstants.FILTER_LOCATIONS_ALL, 
						CulturalOfferConstants.FILTER_TYPES_ALL, 
						this.pageableNonExisting);
		List<CulturalOffer> offers = page.getContent();
		assertTrue(offers.isEmpty());
		assertFalse(page.isFirst());
		assertTrue(page.isLast());
	}
	
	@Test
	public void testFilterOneName() {
		Page<CulturalOffer> page = 
				this.userFollowingRepository
				.filter(UserConstants.ID_ONE, 
						MainConstants.FILTER_ONE, 
						CulturalOfferConstants.FILTER_LOCATIONS_ALL, 
						CulturalOfferConstants.FILTER_TYPES_ALL, 
						this.pageableTotal);
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
		Page<CulturalOffer> page = 
				this.userFollowingRepository
				.filter(UserConstants.ID_ONE, 
						CulturalOfferConstants.FILTER_NAMES_ALL, 
						MainConstants.FILTER_ONE, 
						CulturalOfferConstants.FILTER_TYPES_ALL, 
						this.pageableTotal);
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
		Page<CulturalOffer> page = 
				this.userFollowingRepository
				.filter(UserConstants.ID_ONE, 
						CulturalOfferConstants.FILTER_NAMES_ALL, 
						CulturalOfferConstants.FILTER_LOCATIONS_ALL, 
						MainConstants.FILTER_ONE, 
						this.pageableTotal);
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
		Page<CulturalOffer> page = 
				this.userFollowingRepository
				.filter(UserConstants.ID_ONE, 
						MainConstants.FILTER_ALL, 
						MainConstants.FILTER_ALL, 
						MainConstants.FILTER_NONE, 
						this.pageableTotal);
		List<CulturalOffer> offers = page.getContent();
		assertTrue(offers.isEmpty());
		assertTrue(page.isFirst());
		assertTrue(page.isLast());
	}
	
	@Test
	public void testEmailsMore() {
		List<String> emails = 
				this.userFollowingRepository
				.subscribedEmails(CulturalOfferConstants.ID_ONE);
		assertEquals(MainConstants.PART_SIZE, emails.size());
		assertEquals(UserConstants.EMAIL_ONE, emails.get(0));
		assertEquals(UserConstants.EMAIL_TWO, emails.get(1));
	}
	
	@Test
	public void testEmailsOne() {
		List<String> emails = 
				this.userFollowingRepository
				.subscribedEmails(CulturalOfferConstants.ID_TWO);
		assertEquals(MainConstants.ONE_SIZE, emails.size());
		assertEquals(UserConstants.EMAIL_ONE, emails.get(0));
	}
	
	@Test
	public void testEmailsNone() {
		List<String> emails = 
				this.userFollowingRepository
				.subscribedEmails(MainConstants.NON_EXISTING_ID);
		assertTrue(emails.isEmpty());
	}

}
