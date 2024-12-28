package com.example.demo.service.unit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.constants.CulturalOfferConstants;
import com.example.demo.constants.MainConstants;
import com.example.demo.constants.Filters;
import com.example.demo.constants.TypeConstants;
import com.example.demo.constants.UserConstants;
import com.example.demo.dto.FilterParamsDTO;
import com.example.demo.model.CulturalOffer;
import com.example.demo.model.Type;
import com.example.demo.model.User;
import com.example.demo.model.UserFollowing;
import com.example.demo.repository.UserFollowingRepository;
import com.example.demo.service.UserFollowingService;
import com.example.demo.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserFollowingServiceTest {

	@Autowired
	private UserFollowingService userFollowingService;
	
	@MockBean
	private UserFollowingRepository userFollowingRepository;
	
	@MockBean
	private UserService userService;
	
	@Autowired
	private Filters filters;
	private Pageable pageableTotal = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.TOTAL_SIZE);
	private Pageable pageablePart = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.PART_SIZE);
	private Pageable pageableNonExisting = PageRequest.of(MainConstants.ONE_SIZE, MainConstants.TOTAL_SIZE);
	
	@Before
	public void setUp() {
		User user = new User();
		user.setId(UserConstants.ID_ONE);
		Mockito.when(this.userService.currentUser())
		.thenReturn(user);
	}
	
	@Test
	public void testFilterEmpty() {
		FilterParamsDTO filters = this.filters.filtersEmpty();
		Mockito.when(this.userFollowingRepository.filter(
				UserConstants.ID_ONE, 
				filters.getName(), 
				filters.getLocation(), 
				filters.getType(), 
				this.pageableTotal))
		.thenReturn(new PageImpl<>(List.of(
				this.filterOffer(1), 
				this.filterOffer(2), 
				this.filterOffer(3))));
		List<CulturalOffer> offers = 
				this.userFollowingService
				.filter(filters, this.pageableTotal).getContent();
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
	}
	
	@Test
	public void testFilterEmptyPaginated() {
		FilterParamsDTO filters = this.filters.filtersEmpty();
		Mockito.when(this.userFollowingRepository.filter(
				UserConstants.ID_ONE, 
				filters.getName(), 
				filters.getLocation(), 
				filters.getType(), 
				this.pageablePart))
		.thenReturn(new PageImpl<>(List.of(
				this.filterOffer(1), 
				this.filterOffer(2))));
		List<CulturalOffer> offers = 
				this.userFollowingService
				.filter(filters, this.pageablePart).getContent();
		assertEquals(MainConstants.PART_SIZE, offers.size());
		assertEquals(CulturalOfferConstants.ID_ONE, offers.get(0).getId());
		assertEquals(TypeConstants.ID_ONE, offers.get(0).getType().getId());
		assertEquals(CulturalOfferConstants.NAME_ONE, offers.get(0).getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offers.get(0).getLocation());
		assertEquals(CulturalOfferConstants.ID_TWO, offers.get(1).getId());
		assertEquals(TypeConstants.ID_TWO, offers.get(1).getType().getId());
		assertEquals(CulturalOfferConstants.NAME_TWO, offers.get(1).getName());
		assertEquals(CulturalOfferConstants.LOCATION_TWO, offers.get(1).getLocation());
	}
	
	@Test
	public void testFilterEmptyNonExistingPage() {
		FilterParamsDTO filters = this.filters.filtersEmpty();
		Mockito.when(this.userFollowingRepository.filter(
				UserConstants.ID_ONE, 
				filters.getName(), 
				filters.getLocation(), 
				filters.getType(), 
				this.pageableNonExisting))
		.thenReturn(new PageImpl<>(List.of()));
		List<CulturalOffer> offers = 
				this.userFollowingService
				.filter(filters, this.pageableNonExisting).getContent();
		assertTrue(offers.isEmpty());
	}
	
	@Test
	public void testFilterAll() {
		FilterParamsDTO filters = this.filters.filtersAll();
		Mockito.when(this.userFollowingRepository.filter(
				UserConstants.ID_ONE, 
				filters.getName(), 
				filters.getLocation(), 
				filters.getType(), 
				this.pageableTotal))
		.thenReturn(new PageImpl<>(List.of(
				this.filterOffer(1), 
				this.filterOffer(2), 
				this.filterOffer(3))));
		List<CulturalOffer> offers = 
				this.userFollowingService
				.filter(filters, this.pageableTotal).getContent();
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
	}
	
	@Test
	public void testFilterAllPaginated() {
		FilterParamsDTO filters = this.filters.filtersAll();
		Mockito.when(this.userFollowingRepository.filter(
				UserConstants.ID_ONE, 
				filters.getName(), 
				filters.getLocation(), 
				filters.getType(), 
				this.pageablePart))
		.thenReturn(new PageImpl<>(List.of(
				this.filterOffer(1), 
				this.filterOffer(2))));
		List<CulturalOffer> offers = 
				this.userFollowingService
				.filter(filters, this.pageablePart).getContent();
		assertEquals(MainConstants.PART_SIZE, offers.size());
		assertEquals(CulturalOfferConstants.ID_ONE, offers.get(0).getId());
		assertEquals(TypeConstants.ID_ONE, offers.get(0).getType().getId());
		assertEquals(CulturalOfferConstants.NAME_ONE, offers.get(0).getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offers.get(0).getLocation());
		assertEquals(CulturalOfferConstants.ID_TWO, offers.get(1).getId());
		assertEquals(TypeConstants.ID_TWO, offers.get(1).getType().getId());
		assertEquals(CulturalOfferConstants.NAME_TWO, offers.get(1).getName());
		assertEquals(CulturalOfferConstants.LOCATION_TWO, offers.get(1).getLocation());
	}
	
	@Test
	public void testFilterAllNonExistingPage() {
		FilterParamsDTO filters = this.filters.filtersAll();
		Mockito.when(this.userFollowingRepository.filter(
				UserConstants.ID_ONE, 
				filters.getName(), 
				filters.getLocation(), 
				filters.getType(), 
				this.pageableNonExisting))
		.thenReturn(new PageImpl<>(List.of()));
		List<CulturalOffer> offers = 
				this.userFollowingService
				.filter(filters, this.pageableNonExisting).getContent();
		assertTrue(offers.isEmpty());
	}
	
	@Test
	public void testFilterOneName() {
		FilterParamsDTO filters = this.filters.filtersOneName();
		Mockito.when(this.userFollowingRepository.filter(
				UserConstants.ID_ONE, 
				filters.getName(), 
				filters.getLocation(), 
				filters.getType(), 
				this.pageableTotal))
		.thenReturn(new PageImpl<>(List.of(this.filterOffer(1))));
		List<CulturalOffer> offers = 
				this.userFollowingService
				.filter(filters, this.pageableTotal).getContent();
		assertEquals(MainConstants.ONE_SIZE, offers.size());
		assertEquals(CulturalOfferConstants.ID_ONE, offers.get(0).getId());
		assertEquals(TypeConstants.ID_ONE, offers.get(0).getType().getId());
		assertEquals(CulturalOfferConstants.NAME_ONE, offers.get(0).getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offers.get(0).getLocation());
	}
	
	@Test
	public void testFilterOneLocation() {
		FilterParamsDTO filters = this.filters.filtersOneLocation();
		Mockito.when(this.userFollowingRepository.filter(
				UserConstants.ID_ONE, 
				filters.getName(), 
				filters.getLocation(), 
				filters.getType(), 
				this.pageableTotal))
		.thenReturn(new PageImpl<>(List.of(this.filterOffer(1))));
		List<CulturalOffer> offers = 
				this.userFollowingService
				.filter(filters, this.pageableTotal).getContent();
		assertEquals(MainConstants.ONE_SIZE, offers.size());
		assertEquals(CulturalOfferConstants.ID_ONE, offers.get(0).getId());
		assertEquals(TypeConstants.ID_ONE, offers.get(0).getType().getId());
		assertEquals(CulturalOfferConstants.NAME_ONE, offers.get(0).getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offers.get(0).getLocation());
	}
	
	@Test
	public void testFilterOneType() {
		FilterParamsDTO filters = this.filters.filtersOneType();
		Mockito.when(this.userFollowingRepository.filter(
				UserConstants.ID_ONE, 
				filters.getName(), 
				filters.getLocation(), 
				filters.getType(), 
				this.pageableTotal))
		.thenReturn(new PageImpl<>(List.of(this.filterOffer(1))));
		List<CulturalOffer> offers = 
				this.userFollowingService
				.filter(filters, this.pageableTotal).getContent();
		assertEquals(MainConstants.ONE_SIZE, offers.size());
		assertEquals(CulturalOfferConstants.ID_ONE, offers.get(0).getId());
		assertEquals(TypeConstants.ID_ONE, offers.get(0).getType().getId());
		assertEquals(CulturalOfferConstants.NAME_ONE, offers.get(0).getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offers.get(0).getLocation());
	}
	
	@Test
	public void testFilterNone() {
		FilterParamsDTO filters = this.filters.filtersNone();
		Mockito.when(this.userFollowingRepository.filter(
				UserConstants.ID_ONE, 
				filters.getName(), 
				filters.getLocation(), 
				filters.getType(), 
				this.pageableTotal))
		.thenReturn(new PageImpl<>(List.of()));
		List<CulturalOffer> offers = 
				this.userFollowingService
				.filter(filters, this.pageableTotal).getContent();
		assertTrue(offers.isEmpty());
	}
	
	@Test
	public void testToggleSubscriptionExisting() {
		Mockito.when(this.userFollowingRepository
				.findByUserIdAndCulturalOfferId(UserConstants.ID_ONE, CulturalOfferConstants.ID_ONE))
				.thenReturn(new UserFollowing());
		this.userFollowingService.toggleSubscription(CulturalOfferConstants.ID_ONE);
		Mockito.when(this.userFollowingRepository
				.findByUserIdAndCulturalOfferId(UserConstants.ID_ONE, CulturalOfferConstants.ID_ONE))
				.thenReturn(null);
		assertNull(this.userFollowingRepository
				.findByUserIdAndCulturalOfferId
				(UserConstants.ID_ONE, CulturalOfferConstants.ID_ONE));
		this.userFollowingService.toggleSubscription(CulturalOfferConstants.ID_ONE);
		Mockito.when(this.userFollowingRepository
				.findByUserIdAndCulturalOfferId(UserConstants.ID_ONE, CulturalOfferConstants.ID_ONE))
				.thenReturn(new UserFollowing());
		assertNotNull(this.userFollowingRepository
				.findByUserIdAndCulturalOfferId
				(UserConstants.ID_ONE, CulturalOfferConstants.ID_ONE));
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testToggleSubscriptionNonExisting() {
		Mockito.when(this.userFollowingRepository.findByUserIdAndCulturalOfferId(UserConstants.ID_ONE, MainConstants.NON_EXISTING_ID))
		.thenThrow(ConstraintViolationException.class);
		this.userFollowingService.toggleSubscription(MainConstants.NON_EXISTING_ID);
	}
	
	private CulturalOffer filterOffer(int index) {
		if (index == 1) {
			CulturalOffer offer = new CulturalOffer();
			offer.setId(CulturalOfferConstants.ID_ONE);
			Type type = new Type();
			type.setId(TypeConstants.ID_ONE);
			offer.setType(type);
			offer.setName(CulturalOfferConstants.NAME_ONE);
			offer.setLocation(CulturalOfferConstants.LOCATION_ONE);
			return offer;
		}
		if (index == 2) {
			CulturalOffer offer = new CulturalOffer();
			offer.setId(CulturalOfferConstants.ID_TWO);
			Type type = new Type();
			type.setId(TypeConstants.ID_TWO);
			offer.setType(type);
			offer.setName(CulturalOfferConstants.NAME_TWO);
			offer.setLocation(CulturalOfferConstants.LOCATION_TWO);
			return offer;
		}
		CulturalOffer offer = new CulturalOffer();
		offer.setId(CulturalOfferConstants.ID_THREE);
		Type type = new Type();
		type.setId(TypeConstants.ID_THREE);
		offer.setType(type);
		offer.setName(CulturalOfferConstants.NAME_THREE);
		offer.setLocation(CulturalOfferConstants.LOCATION_THREE);
		return offer;
	}
	
}
