package com.example.demo.service.unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.constants.CulturalOfferConstants;
import com.example.demo.constants.MainConstants;
import com.example.demo.constants.Filters;
import com.example.demo.constants.TypeConstants;
import com.example.demo.dto.FilterParamsDTO;
import com.example.demo.dto.UniqueCheckDTO;
import com.example.demo.model.CulturalOffer;
import com.example.demo.model.Type;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.CulturalOfferRepository;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.TypeRepository;
import com.example.demo.service.CulturalOfferService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CulturalOfferServiceTest {

	@Autowired
	private CulturalOfferService culturalOfferService;
	
	@MockBean
	private CulturalOfferRepository culturalOfferRepository;
	
	@MockBean
	private TypeRepository typeRepository;

	@MockBean
	private CommentRepository commentRepository;
	
	@MockBean
	private NewsRepository newsRepository;
		
	@Autowired
	private Filters filters;
	private Pageable pageableTotal = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.TOTAL_SIZE);
	private Pageable pageablePart = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.PART_SIZE);
	private Pageable pageableNonExisting = PageRequest.of(MainConstants.ONE_SIZE, MainConstants.TOTAL_SIZE);
	
	@Test
	public void testHasNameNewOfferNonExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(null);
		param.setName(CulturalOfferConstants.NON_EXISTING_NAME);
		Mockito.when(this.culturalOfferRepository
				.hasName(param.getId(), param.getName())).thenReturn(null);
		assertFalse(this.culturalOfferService.hasName(param));
	}
	
	@Test
	public void testHasNameNewOfferExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(null);
		param.setName(CulturalOfferConstants.NAME_ONE);
		Mockito.when(this.culturalOfferRepository
				.hasName(param.getId(), param.getName())).thenReturn(new CulturalOffer());
		assertTrue(this.culturalOfferService.hasName(param));
	}
	
	@Test
	public void testHasNameOldOfferOwnName() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(CulturalOfferConstants.ID_ONE);
		param.setName(CulturalOfferConstants.NAME_ONE);
		Mockito.when(this.culturalOfferRepository
				.hasName(param.getId(), param.getName())).thenReturn(null);
		assertFalse(this.culturalOfferService.hasName(param));
	}
	
	@Test
	public void testHasNameOldOfferNonExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(CulturalOfferConstants.ID_ONE);
		param.setName(CulturalOfferConstants.NON_EXISTING_NAME);
		Mockito.when(this.culturalOfferRepository
				.hasName(param.getId(), param.getName())).thenReturn(null);
		assertFalse(this.culturalOfferService.hasName(param));
	}
	
	@Test
	public void testHasNameOldOfferExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(CulturalOfferConstants.ID_ONE);
		param.setName(CulturalOfferConstants.NAME_TWO);
		Mockito.when(this.culturalOfferRepository
				.hasName(param.getId(), param.getName())).thenReturn(new CulturalOffer());
		assertTrue(this.culturalOfferService.hasName(param));
	}
	
	@Test
	public void testFilterNamesEmpty() {
		Mockito.when(this.culturalOfferRepository.filterNames(MainConstants.FILTER_ALL))
		.thenReturn(List.of(
				CulturalOfferConstants.NAME_ONE, 
				CulturalOfferConstants.NAME_TWO, 
				CulturalOfferConstants.NAME_THREE));
		List<String> names = 
				this.culturalOfferService
				.filterNames(MainConstants.FILTER_ALL);
		assertEquals(MainConstants.TOTAL_SIZE, names.size());
		assertEquals(CulturalOfferConstants.NAME_ONE, names.get(0));
		assertEquals(CulturalOfferConstants.NAME_TWO, names.get(1));
		assertEquals(CulturalOfferConstants.NAME_THREE, names.get(2));
	}
	
	@Test
	public void testFilterNamesAll() {
		Mockito.when(this.culturalOfferRepository.filterNames(CulturalOfferConstants.FILTER_NAMES_ALL))
		.thenReturn(List.of(
				CulturalOfferConstants.NAME_ONE, 
				CulturalOfferConstants.NAME_TWO, 
				CulturalOfferConstants.NAME_THREE));
		List<String> names = 
				this.culturalOfferService
				.filterNames(CulturalOfferConstants.FILTER_NAMES_ALL);
		assertEquals(MainConstants.TOTAL_SIZE, names.size());
		assertEquals(CulturalOfferConstants.NAME_ONE, names.get(0));
		assertEquals(CulturalOfferConstants.NAME_TWO, names.get(1));
		assertEquals(CulturalOfferConstants.NAME_THREE, names.get(2));
	}
	
	@Test
	public void testFilterNamesOne() {
		Mockito.when(this.culturalOfferRepository.filterNames(MainConstants.FILTER_ONE))
		.thenReturn(List.of(CulturalOfferConstants.NAME_ONE));
		List<String> names = 
				this.culturalOfferService
				.filterNames(MainConstants.FILTER_ONE);
		assertEquals(MainConstants.ONE_SIZE, names.size());
		assertEquals(CulturalOfferConstants.NAME_ONE, names.get(0));
	}
	
	@Test
	public void testFilterNamesNone() {
		Mockito.when(this.culturalOfferRepository.filterNames(MainConstants.FILTER_NONE))
		.thenReturn(List.of());
		List<String> names = 
				this.culturalOfferService
				.filterNames(MainConstants.FILTER_NONE);
		assertTrue(names.isEmpty());
	}
	
	@Test
	public void testFilterLocationsEmpty() {
		Mockito.when(this.culturalOfferRepository.filterLocations(MainConstants.FILTER_ALL))
		.thenReturn(List.of(
				CulturalOfferConstants.LOCATION_ONE, 
				CulturalOfferConstants.LOCATION_TWO, 
				CulturalOfferConstants.LOCATION_THREE));
		List<String> locations = 
				this.culturalOfferService
				.filterLocations(MainConstants.FILTER_ALL);
		assertEquals(MainConstants.TOTAL_SIZE, locations.size());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, locations.get(0));
		assertEquals(CulturalOfferConstants.LOCATION_TWO, locations.get(1));
		assertEquals(CulturalOfferConstants.LOCATION_THREE, locations.get(2));
	}
	
	@Test
	public void testFilterLocationsAll() {
		Mockito.when(this.culturalOfferRepository.filterLocations(CulturalOfferConstants.FILTER_LOCATIONS_ALL))
		.thenReturn(List.of(
				CulturalOfferConstants.LOCATION_ONE, 
				CulturalOfferConstants.LOCATION_TWO, 
				CulturalOfferConstants.LOCATION_THREE));
		List<String> locations = 
				this.culturalOfferService
				.filterLocations(CulturalOfferConstants.FILTER_LOCATIONS_ALL);
		assertEquals(MainConstants.TOTAL_SIZE, locations.size());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, locations.get(0));
		assertEquals(CulturalOfferConstants.LOCATION_TWO, locations.get(1));
		assertEquals(CulturalOfferConstants.LOCATION_THREE, locations.get(2));
	}
	
	@Test
	public void testFilterLocationsOne() {
		Mockito.when(this.culturalOfferRepository.filterLocations(MainConstants.FILTER_ONE))
		.thenReturn(List.of(CulturalOfferConstants.LOCATION_ONE));
		List<String> locations = 
				this.culturalOfferService
				.filterLocations(MainConstants.FILTER_ONE);
		assertEquals(MainConstants.ONE_SIZE, locations.size());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, locations.get(0));
	}
	
	@Test
	public void testFilterLocationsNone() {
		Mockito.when(this.culturalOfferRepository.filterLocations(MainConstants.FILTER_NONE))
		.thenReturn(List.of());
		List<String> locations = 
				this.culturalOfferService
				.filterLocations(MainConstants.FILTER_NONE);
		assertTrue(locations.isEmpty());
	}
	
	@Test
	public void testFilterTypesEmpty() {
		Mockito.when(this.culturalOfferRepository.filterTypes(MainConstants.FILTER_ALL))
		.thenReturn(List.of(
				TypeConstants.NAME_ONE, 
				TypeConstants.NAME_TWO, 
				TypeConstants.NAME_THREE));
		List<String> types = 
				this.culturalOfferService
				.filterTypes(MainConstants.FILTER_ALL);
		assertEquals(MainConstants.TOTAL_SIZE, types.size());
		assertEquals(TypeConstants.NAME_ONE, types.get(0));
		assertEquals(TypeConstants.NAME_TWO, types.get(1));
		assertEquals(TypeConstants.NAME_THREE, types.get(2));
	}
	
	@Test
	public void testFilterTypesAll() {
		Mockito.when(this.culturalOfferRepository.filterTypes(CulturalOfferConstants.FILTER_TYPES_ALL))
		.thenReturn(List.of(
				TypeConstants.NAME_ONE, 
				TypeConstants.NAME_TWO, 
				TypeConstants.NAME_THREE));
		List<String> types = 
				this.culturalOfferService
				.filterTypes(CulturalOfferConstants.FILTER_TYPES_ALL);
		assertEquals(MainConstants.TOTAL_SIZE, types.size());
		assertEquals(TypeConstants.NAME_ONE, types.get(0));
		assertEquals(TypeConstants.NAME_TWO, types.get(1));
		assertEquals(TypeConstants.NAME_THREE, types.get(2));
	}
	
	@Test
	public void testFilterTypesOne() {
		Mockito.when(this.culturalOfferRepository.filterTypes(MainConstants.FILTER_ONE))
		.thenReturn(List.of(TypeConstants.NAME_ONE));
		List<String> types = 
				this.culturalOfferService
				.filterTypes(MainConstants.FILTER_ONE);
		assertEquals(MainConstants.ONE_SIZE, types.size());
		assertEquals(TypeConstants.NAME_ONE, types.get(0));
	}
	
	@Test
	public void testFilterTypesNone() {
		Mockito.when(this.culturalOfferRepository.filterTypes(MainConstants.FILTER_NONE))
		.thenReturn(List.of());
		List<String> types = 
				this.culturalOfferService
				.filterTypes(MainConstants.FILTER_NONE);
		assertTrue(types.isEmpty());
	}
	
	@Test
	public void testFilterEmpty() {
		FilterParamsDTO filters = this.filters.filtersEmpty();
		Mockito.when(this.culturalOfferRepository.filter(
				filters.getName(), 
				filters.getLocation(), 
				filters.getType(), 
				this.pageableTotal))
		.thenReturn(new PageImpl<>(List.of(
				this.filterOffer(1), 
				this.filterOffer(2), 
				this.filterOffer(3))));
		List<CulturalOffer> offers = 
				this.culturalOfferService
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
		Mockito.when(this.culturalOfferRepository.filter(
				filters.getName(), 
				filters.getLocation(), 
				filters.getType(), 
				this.pageablePart))
		.thenReturn(new PageImpl<>(List.of(
				this.filterOffer(1), 
				this.filterOffer(2))));
		List<CulturalOffer> offers = 
				this.culturalOfferService
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
		Mockito.when(this.culturalOfferRepository.filter(
				filters.getName(), 
				filters.getLocation(), 
				filters.getType(), 
				this.pageableNonExisting))
		.thenReturn(new PageImpl<>(List.of()));
		List<CulturalOffer> offers = 
				this.culturalOfferService
				.filter(filters, this.pageableNonExisting).getContent();
		assertTrue(offers.isEmpty());
	}
	
	@Test
	public void testFilterAll() {
		FilterParamsDTO filters = this.filters.filtersAll();
		Mockito.when(this.culturalOfferRepository.filter(
				filters.getName(), 
				filters.getLocation(), 
				filters.getType(), 
				this.pageableTotal))
		.thenReturn(new PageImpl<>(List.of(
				this.filterOffer(1), 
				this.filterOffer(2), 
				this.filterOffer(3))));
		List<CulturalOffer> offers = 
				this.culturalOfferService
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
		Mockito.when(this.culturalOfferRepository.filter(
				filters.getName(), 
				filters.getLocation(), 
				filters.getType(), 
				this.pageablePart))
		.thenReturn(new PageImpl<>(List.of(
				this.filterOffer(1), 
				this.filterOffer(2))));
		List<CulturalOffer> offers = 
				this.culturalOfferService
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
		Mockito.when(this.culturalOfferRepository.filter(
				filters.getName(), 
				filters.getLocation(), 
				filters.getType(), 
				this.pageableNonExisting))
		.thenReturn(new PageImpl<>(List.of()));
		List<CulturalOffer> offers = 
				this.culturalOfferService
				.filter(filters, this.pageableNonExisting).getContent();
		assertTrue(offers.isEmpty());
	}
	
	@Test
	public void testFilterOneName() {
		FilterParamsDTO filters = this.filters.filtersOneName();
		Mockito.when(this.culturalOfferRepository.filter(
				filters.getName(), 
				filters.getLocation(), 
				filters.getType(), 
				this.pageableTotal))
		.thenReturn(new PageImpl<>(List.of(this.filterOffer(1))));
		List<CulturalOffer> offers = 
				this.culturalOfferService
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
		Mockito.when(this.culturalOfferRepository.filter(
				filters.getName(), 
				filters.getLocation(), 
				filters.getType(), 
				this.pageableTotal))
		.thenReturn(new PageImpl<>(List.of(this.filterOffer(1))));
		List<CulturalOffer> offers = 
				this.culturalOfferService
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
		Mockito.when(this.culturalOfferRepository.filter(
				filters.getName(), 
				filters.getLocation(), 
				filters.getType(), 
				this.pageableTotal))
		.thenReturn(new PageImpl<>(List.of(this.filterOffer(1))));
		List<CulturalOffer> offers = 
				this.culturalOfferService
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
		Mockito.when(this.culturalOfferRepository.filter(
				filters.getName(), 
				filters.getLocation(), 
				filters.getType(), 
				this.pageableTotal))
		.thenReturn(new PageImpl<>(List.of()));
		List<CulturalOffer> offers = 
				this.culturalOfferService
				.filter(filters, this.pageableTotal).getContent();
		assertTrue(offers.isEmpty());
	}
	
	@Test
	public void testDeleteExisting() {
		Mockito.when(this.culturalOfferRepository.count())
		.thenReturn((long) MainConstants.TOTAL_SIZE);
		long size = this.culturalOfferRepository.count();
		this.culturalOfferService.delete(CulturalOfferConstants.ID_THREE);
		Mockito.when(this.culturalOfferRepository.count())
		.thenReturn(size - 1);
		assertEquals(size - 1, this.culturalOfferRepository.count());
		Mockito.when(this.culturalOfferRepository.findById(CulturalOfferConstants.ID_THREE))
		.thenReturn(Optional.empty());
		assertNull(this.culturalOfferRepository.findById(CulturalOfferConstants.ID_THREE).orElse(null));
		Mockito.when(this.commentRepository.findByCulturalOfferIdOrderByCreatedAtDesc(CulturalOfferConstants.ID_THREE, this.pageableTotal))
		.thenReturn(new PageImpl<>(List.of()));
		assertTrue(this.commentRepository.findByCulturalOfferIdOrderByCreatedAtDesc(CulturalOfferConstants.ID_THREE, this.pageableTotal).isEmpty());
		Mockito.when(this.newsRepository.filter(CulturalOfferConstants.ID_THREE, null, null, this.pageableTotal))
		.thenReturn(new PageImpl<>(List.of()));
		assertTrue(this.newsRepository.filter(CulturalOfferConstants.ID_THREE, null, null, this.pageableTotal).isEmpty());
	}
	
	@Test(expected = EmptyResultDataAccessException.class)
	public void testDeleteNonExisting() {
		Mockito.doThrow(EmptyResultDataAccessException.class)
		.when(this.culturalOfferRepository).deleteById(MainConstants.NON_EXISTING_ID);
		this.culturalOfferService.delete(MainConstants.NON_EXISTING_ID);
	}
		
	@Test
	public void testAddValid() {
		Mockito.when(this.culturalOfferRepository.count())
		.thenReturn((long) MainConstants.TOTAL_SIZE);
		long size = this.culturalOfferRepository.count();
		CulturalOffer offer = this.testingOffer();
		Mockito.when(this.culturalOfferRepository.save(offer))
		.thenReturn(offer);
		offer = this.culturalOfferService.save(offer, null);
		Mockito.when(this.culturalOfferRepository.count())
		.thenReturn(size + 1);
		assertEquals(size + 1, this.culturalOfferRepository.count());
		assertEquals(TypeConstants.ID_ONE, offer.getType().getId());
		assertEquals(CulturalOfferConstants.NON_EXISTING_NAME, offer.getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offer.getLocation());
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddNullType() {
		CulturalOffer offer = this.testingOffer();
		offer.setType(null);
		Mockito.when(this.culturalOfferRepository.save(offer))
		.thenThrow(ConstraintViolationException.class);
		this.culturalOfferService.save(offer, null);
	}
	
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddNullName() {
		CulturalOffer offer = this.testingOffer();
		offer.setName(null);
		Mockito.when(this.culturalOfferRepository.save(offer))
		.thenThrow(ConstraintViolationException.class);
		this.culturalOfferService.save(offer, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddEmptyName() {
		CulturalOffer offer = this.testingOffer();
		offer.setName("");
		Mockito.when(this.culturalOfferRepository.save(offer))
		.thenThrow(ConstraintViolationException.class);
		this.culturalOfferService.save(offer, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddBlankName() {
		CulturalOffer offer = this.testingOffer();
		offer.setName("  ");
		Mockito.when(this.culturalOfferRepository.save(offer))
		.thenThrow(ConstraintViolationException.class);
		this.culturalOfferService.save(offer, null);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testAddNonUniqueName() {
		CulturalOffer offer = this.testingOffer();
		offer.setName(CulturalOfferConstants.NAME_ONE);
		Mockito.when(this.culturalOfferRepository.save(offer))
		.thenThrow(DataIntegrityViolationException.class);
		this.culturalOfferService.save(offer, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddNullLocation() {
		CulturalOffer offer = this.testingOffer();
		offer.setLocation(null);
		Mockito.when(this.culturalOfferRepository.save(offer))
		.thenThrow(ConstraintViolationException.class);
		this.culturalOfferService.save(offer, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddEmptyLocation() {
		CulturalOffer offer = this.testingOffer();
		offer.setLocation("");
		Mockito.when(this.culturalOfferRepository.save(offer))
		.thenThrow(ConstraintViolationException.class);
		this.culturalOfferService.save(offer, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddBlankLocation() {
		CulturalOffer offer = this.testingOffer();
		offer.setLocation("  ");
		Mockito.when(this.culturalOfferRepository.save(offer))
		.thenThrow(ConstraintViolationException.class);
		this.culturalOfferService.save(offer, null);
	}
	
	@Test
	public void testUpdateValid() {
		Mockito.when(this.culturalOfferRepository.count())
		.thenReturn((long) MainConstants.TOTAL_SIZE);
		long size = this.culturalOfferRepository.count();
		CulturalOffer offer = this.testingOffer();
		offer.setId(CulturalOfferConstants.ID_ONE);
		Mockito.when(this.culturalOfferRepository.save(offer))
		.thenReturn(offer);
		offer = this.culturalOfferService.save(offer, null);
		assertEquals(size, this.culturalOfferRepository.count());
		assertEquals(CulturalOfferConstants.ID_ONE, offer.getId());
		assertEquals(TypeConstants.ID_ONE, offer.getType().getId());
		assertEquals(CulturalOfferConstants.NON_EXISTING_NAME, offer.getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offer.getLocation());
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateNullType() {
		CulturalOffer offer = this.testingOffer();
		offer.setId(CulturalOfferConstants.ID_ONE);
		offer.setType(null);
		Mockito.when(this.culturalOfferRepository.save(offer))
		.thenThrow(ConstraintViolationException.class);
		this.culturalOfferService.save(offer, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateNullName() {
		CulturalOffer offer = this.testingOffer();
		offer.setId(CulturalOfferConstants.ID_ONE);
		offer.setName(null);
		Mockito.when(this.culturalOfferRepository.save(offer))
		.thenThrow(ConstraintViolationException.class);
		this.culturalOfferService.save(offer, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateEmptyName() {
		CulturalOffer offer = this.testingOffer();
		offer.setId(CulturalOfferConstants.ID_ONE);
		offer.setName("");
		Mockito.when(this.culturalOfferRepository.save(offer))
		.thenThrow(ConstraintViolationException.class);
		this.culturalOfferService.save(offer, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateBlankName() {
		CulturalOffer offer = this.testingOffer();
		offer.setId(CulturalOfferConstants.ID_ONE);
		offer.setName("  ");
		Mockito.when(this.culturalOfferRepository.save(offer))
		.thenThrow(ConstraintViolationException.class);
		this.culturalOfferService.save(offer, null);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testUpdateNonUniqueName() {
		CulturalOffer offer = this.testingOffer();
		offer.setId(CulturalOfferConstants.ID_ONE);
		offer.setName(CulturalOfferConstants.NAME_TWO);
		Mockito.when(this.culturalOfferRepository.save(offer))
		.thenThrow(DataIntegrityViolationException.class);
		this.culturalOfferService.save(offer, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateNullLocation() {
		CulturalOffer offer = this.testingOffer();
		offer.setId(CulturalOfferConstants.ID_ONE);
		offer.setLocation(null);
		Mockito.when(this.culturalOfferRepository.save(offer))
		.thenThrow(ConstraintViolationException.class);
		this.culturalOfferService.save(offer, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateEmptyLocation() {
		CulturalOffer offer = this.testingOffer();
		offer.setId(CulturalOfferConstants.ID_ONE);
		offer.setLocation("");
		Mockito.when(this.culturalOfferRepository.save(offer))
		.thenThrow(ConstraintViolationException.class);
		this.culturalOfferService.save(offer, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateBlankLocation() {
		CulturalOffer offer = this.testingOffer();
		offer.setId(CulturalOfferConstants.ID_ONE);
		offer.setLocation("  ");
		Mockito.when(this.culturalOfferRepository.save(offer))
		.thenThrow(ConstraintViolationException.class);
		this.culturalOfferService.save(offer, null);
	}
	
	private CulturalOffer testingOffer() {
		CulturalOffer offer = new CulturalOffer();
		Type type = new Type();
		type.setId(TypeConstants.ID_ONE);
		offer.setType(type);
		offer.setName(CulturalOfferConstants.NON_EXISTING_NAME);
		offer.setLocation(CulturalOfferConstants.LOCATION_ONE);
		return offer;
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