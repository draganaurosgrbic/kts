package com.example.demo.service.integration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constants.CulturalOfferConstants;
import com.example.demo.constants.MainConstants;
import com.example.demo.constants.Filters;
import com.example.demo.constants.TypeConstants;
import com.example.demo.dto.FilterParamsDTO;
import com.example.demo.dto.UniqueCheckDTO;
import com.example.demo.model.CulturalOffer;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.CulturalOfferRepository;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.TypeRepository;
import com.example.demo.service.CulturalOfferService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CulturalOfferServiceTest {

	@Autowired
	private CulturalOfferService culturalOfferService;
	
	@Autowired
	private CulturalOfferRepository culturalOfferRepository;
	
	@Autowired
	private TypeRepository typeRepository;

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
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
		assertFalse(this.culturalOfferService.hasName(param));
	}
	
	@Test
	public void testHasNameNewOfferExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(null);
		param.setName(CulturalOfferConstants.NAME_ONE);
		assertTrue(this.culturalOfferService.hasName(param));
	}
	
	@Test
	public void testHasNameOldOfferOwnName() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(CulturalOfferConstants.ID_ONE);
		param.setName(CulturalOfferConstants.NAME_ONE);
		assertFalse(this.culturalOfferService.hasName(param));
	}
	
	@Test
	public void testHasNameOldOfferNonExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(CulturalOfferConstants.ID_ONE);
		param.setName(CulturalOfferConstants.NON_EXISTING_NAME);
		assertFalse(this.culturalOfferService.hasName(param));
	}
	
	@Test
	public void testHasNameOldOfferExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(CulturalOfferConstants.ID_ONE);
		param.setName(CulturalOfferConstants.NAME_TWO);
		assertTrue(this.culturalOfferService.hasName(param));
	}
	
	@Test
	public void testFilterNamesEmpty() {
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
		List<String> names = 
				this.culturalOfferService
				.filterNames(MainConstants.FILTER_ONE);
		assertEquals(MainConstants.ONE_SIZE, names.size());
		assertEquals(CulturalOfferConstants.NAME_ONE, names.get(0));
	}
	
	@Test
	public void testFilterNamesNone() {
		List<String> names = 
				this.culturalOfferService
				.filterNames(MainConstants.FILTER_NONE);
		assertTrue(names.isEmpty());
	}
	
	@Test
	public void testFilterLocationsEmpty() {
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
		List<String> locations = 
				this.culturalOfferService
				.filterLocations(MainConstants.FILTER_ONE);
		assertEquals(MainConstants.ONE_SIZE, locations.size());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, locations.get(0));
	}
	
	@Test
	public void testFilterLocationsNone() {
		List<String> locations = 
				this.culturalOfferService
				.filterLocations(MainConstants.FILTER_NONE);
		assertTrue(locations.isEmpty());
	}
	
	@Test
	public void testFilterTypesEmpty() {
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
		List<String> types = 
				this.culturalOfferService
				.filterTypes(MainConstants.FILTER_ONE);
		assertEquals(MainConstants.ONE_SIZE, types.size());
		assertEquals(TypeConstants.NAME_ONE, types.get(0));
	}
	
	@Test
	public void testFilterTypesNone() {
		List<String> types = 
				this.culturalOfferService
				.filterTypes(MainConstants.FILTER_NONE);
		assertTrue(types.isEmpty());
	}
		
	@Test
	public void testFilterEmpty() {
		FilterParamsDTO filters = this.filters.filtersEmpty();
		Page<CulturalOffer> page = 
				this.culturalOfferService
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
				this.culturalOfferService
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
				this.culturalOfferService
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
				this.culturalOfferService
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
				this.culturalOfferService
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
				this.culturalOfferService
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
				this.culturalOfferService
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
				this.culturalOfferService
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
				this.culturalOfferService
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
				this.culturalOfferService
				.filter(filters, this.pageableTotal);
		List<CulturalOffer> offers = page.getContent();
		assertTrue(offers.isEmpty());
		assertTrue(page.isFirst());
		assertTrue(page.isLast());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testDeleteExisting() {
		long size = this.culturalOfferRepository.count();
		this.culturalOfferService.delete(CulturalOfferConstants.ID_THREE);
		assertEquals(size - 1, this.culturalOfferRepository.count());
		assertNull(this.culturalOfferRepository.findById(CulturalOfferConstants.ID_THREE).orElse(null));
		assertTrue(this.commentRepository.findByCulturalOfferIdOrderByCreatedAtDesc(CulturalOfferConstants.ID_THREE, this.pageableTotal).isEmpty());
		assertTrue(this.newsRepository.filter(CulturalOfferConstants.ID_THREE, null, null, this.pageableTotal).isEmpty());
	}
	
	@Test(expected = EmptyResultDataAccessException.class)
	@Transactional
	@Rollback(true)
	public void testDeleteNonExisting() {
		this.culturalOfferService.delete(MainConstants.NON_EXISTING_ID);
	}
		
	@Test
	@Transactional
	@Rollback(true)
	public void testAddValid() {
		long size = this.culturalOfferRepository.count();
		CulturalOffer offer = this.testingOffer();
		offer = this.culturalOfferService.save(offer, null);
		assertEquals(size + 1, this.culturalOfferRepository.count());
		assertEquals(TypeConstants.ID_ONE, offer.getType().getId());
		assertEquals(CulturalOfferConstants.NON_EXISTING_NAME, offer.getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offer.getLocation());
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddNullType() {
		CulturalOffer offer = this.testingOffer();
		offer.setType(null);
		this.culturalOfferService.save(offer, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddNullName() {
		CulturalOffer offer = this.testingOffer();
		offer.setName(null);
		this.culturalOfferService.save(offer, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddEmptyName() {
		CulturalOffer offer = this.testingOffer();
		offer.setName("");
		this.culturalOfferService.save(offer, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddBlankName() {
		CulturalOffer offer = this.testingOffer();
		offer.setName("  ");
		this.culturalOfferService.save(offer, null);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddNonUniqueName() {
		CulturalOffer offer = this.testingOffer();
		offer.setName(CulturalOfferConstants.NAME_ONE);
		this.culturalOfferService.save(offer, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddNullLocation() {
		CulturalOffer offer = this.testingOffer();
		offer.setLocation(null);
		this.culturalOfferService.save(offer, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddEmptyLocation() {
		CulturalOffer offer = this.testingOffer();
		offer.setLocation("");
		this.culturalOfferService.save(offer, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddBlankLocation() {
		CulturalOffer offer = this.testingOffer();
		offer.setLocation("  ");
		this.culturalOfferService.save(offer, null);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testUpdateValid() {
		long size = this.culturalOfferRepository.count();
		CulturalOffer offer = this.testingOffer();
		offer.setId(CulturalOfferConstants.ID_ONE);
		offer = this.culturalOfferService.save(offer, null);
		assertEquals(size, this.culturalOfferRepository.count());
		assertEquals(CulturalOfferConstants.ID_ONE, offer.getId());
		assertEquals(TypeConstants.ID_ONE, offer.getType().getId());
		assertEquals(CulturalOfferConstants.NON_EXISTING_NAME, offer.getName());
		assertEquals(CulturalOfferConstants.LOCATION_ONE, offer.getLocation());
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateNullType() {
		CulturalOffer offer = this.testingOffer();
		offer.setId(CulturalOfferConstants.ID_ONE);
		offer.setType(null);
		this.culturalOfferService.save(offer, null);
		this.culturalOfferRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateNullName() {
		CulturalOffer offer = this.testingOffer();
		offer.setId(CulturalOfferConstants.ID_ONE);
		offer.setName(null);
		this.culturalOfferService.save(offer, null);
		this.culturalOfferRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateEmptyName() {
		CulturalOffer offer = this.testingOffer();
		offer.setId(CulturalOfferConstants.ID_ONE);
		offer.setName("");
		this.culturalOfferService.save(offer, null);
		this.culturalOfferRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateBlankName() {
		CulturalOffer offer = this.testingOffer();
		offer.setId(CulturalOfferConstants.ID_ONE);
		offer.setName("  ");
		this.culturalOfferService.save(offer, null);
		this.culturalOfferRepository.count();
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateNonUniqueName() {
		CulturalOffer offer = this.testingOffer();
		offer.setId(CulturalOfferConstants.ID_ONE);
		offer.setName(CulturalOfferConstants.NAME_TWO);
		this.culturalOfferService.save(offer, null);
		this.culturalOfferRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateNullLocation() {
		CulturalOffer offer = this.testingOffer();
		offer.setId(CulturalOfferConstants.ID_ONE);
		offer.setLocation(null);
		this.culturalOfferService.save(offer, null);
		this.culturalOfferRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateEmptyLocation() {
		CulturalOffer offer = this.testingOffer();
		offer.setId(CulturalOfferConstants.ID_ONE);
		offer.setLocation("");
		this.culturalOfferService.save(offer, null);
		this.culturalOfferRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateBlankLocation() {
		CulturalOffer offer = this.testingOffer();
		offer.setId(CulturalOfferConstants.ID_ONE);
		offer.setLocation("  ");
		this.culturalOfferService.save(offer, null);
		this.culturalOfferRepository.count();
	}
	
	private CulturalOffer testingOffer() {
		CulturalOffer offer = new CulturalOffer();
		offer.setType(this.typeRepository.findById(TypeConstants.ID_ONE).orElse(null));
		offer.setName(CulturalOfferConstants.NON_EXISTING_NAME);
		offer.setLocation(CulturalOfferConstants.LOCATION_ONE);
		return offer;
	}
		
}
