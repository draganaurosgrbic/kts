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

import com.example.demo.constants.CategoryConstants;
import com.example.demo.constants.MainConstants;
import com.example.demo.constants.TypeConstants;
import com.example.demo.dto.UniqueCheckDTO;
import com.example.demo.model.Type;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.TypeRepository;
import com.example.demo.service.TypeService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TypeServiceTest {
	
	@Autowired
	private TypeService typeService;
	
	@Autowired
	private TypeRepository typeRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	private Pageable pageableTotal = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.TOTAL_SIZE);
	private Pageable pageablePart = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.PART_SIZE);
	private Pageable pageableNonExisting = PageRequest.of(MainConstants.ONE_SIZE, MainConstants.TOTAL_SIZE);

	@Test
	public void testHasNameNonExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(null);
		param.setName(TypeConstants.NON_EXISTING_NAME);
		assertFalse(this.typeService.hasName(param));
	}
	
	@Test
	public void testHasNameExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(null);
		param.setName(TypeConstants.NAME_ONE);
		assertTrue(this.typeService.hasName(param));
	}
	
	@Test
	public void testFilterNamesEmpty() {
		List<String> names = 
				this.typeService
				.filterNames(MainConstants.FILTER_ALL);
		assertEquals(MainConstants.TOTAL_SIZE, names.size());
		assertEquals(TypeConstants.NAME_ONE, names.get(0));
		assertEquals(TypeConstants.NAME_TWO, names.get(1));
		assertEquals(TypeConstants.NAME_THREE, names.get(2));
	}
	
	@Test
	public void testFilterNamesAll() {
		List<String> names = 
				this.typeService
				.filterNames(TypeConstants.FILTER_NAMES_ALL);
		assertEquals(MainConstants.TOTAL_SIZE, names.size());
		assertEquals(TypeConstants.NAME_ONE, names.get(0));
		assertEquals(TypeConstants.NAME_TWO, names.get(1));
		assertEquals(TypeConstants.NAME_THREE, names.get(2));
	}
	
	@Test
	public void testFilterNamesOne() {
		List<String> names = 
				this.typeService
				.filterNames(MainConstants.FILTER_ONE);
		assertEquals(MainConstants.ONE_SIZE, names.size());
		assertEquals(TypeConstants.NAME_ONE, names.get(0));
	}
	
	@Test
	public void testFilterNamesNone() {
		List<String> names = 
				this.typeService
				.filterNames(MainConstants.FILTER_NONE);
		assertTrue(names.isEmpty());
	}
	
	@Test
	public void testListAll() {
		Page<Type> page = 
				this.typeService
				.list(this.pageableTotal);
		List<Type> types = page.getContent();
		assertEquals(MainConstants.TOTAL_SIZE, types.size());
		assertEquals(TypeConstants.ID_ONE, types.get(0).getId());
		assertEquals(CategoryConstants.ID_ONE, types.get(0).getCategory().getId());
		assertEquals(TypeConstants.NAME_ONE, types.get(0).getName());
		assertEquals(TypeConstants.ID_TWO, types.get(1).getId());
		assertEquals(CategoryConstants.ID_TWO, types.get(1).getCategory().getId());
		assertEquals(TypeConstants.NAME_TWO, types.get(1).getName());
		assertEquals(TypeConstants.ID_THREE, types.get(2).getId());
		assertEquals(CategoryConstants.ID_THREE, types.get(2).getCategory().getId());
		assertEquals(TypeConstants.NAME_THREE, types.get(2).getName());
		assertTrue(page.isFirst());
		assertTrue(page.isLast());
	}
	
	@Test
	public void testListAllPaginated() {
		Page<Type> page = 
				this.typeService
				.list(this.pageablePart);
		List<Type> types = page.getContent();
		assertEquals(MainConstants.PART_SIZE, types.size());
		assertEquals(TypeConstants.ID_ONE, types.get(0).getId());
		assertEquals(CategoryConstants.ID_ONE, types.get(0).getCategory().getId());
		assertEquals(TypeConstants.NAME_ONE, types.get(0).getName());
		assertEquals(TypeConstants.ID_TWO, types.get(1).getId());
		assertEquals(CategoryConstants.ID_TWO, types.get(1).getCategory().getId());
		assertEquals(TypeConstants.NAME_TWO, types.get(1).getName());
		assertTrue(page.isFirst());
		assertFalse(page.isLast());
	}
	
	@Test
	public void testListAllNonExistingPage() {
		Page<Type> page = 
				this.typeService
				.list(this.pageableNonExisting);
		List<Type> types = page.getContent();
		assertTrue(types.isEmpty());
		assertFalse(page.isFirst());
		assertTrue(page.isLast());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testDeleteExisting() {
		long id = this.typeRepository.save(this.testingType()).getId();
		long size = this.typeRepository.count();
		this.typeService.delete(id);
		assertEquals(size - 1, this.typeRepository.count());
		assertNull(this.typeRepository.findById(id).orElse(null));
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	@Transactional
	@Rollback(true)
	public void testDeleteWithCulturalOffer() {
		this.typeService.delete(CategoryConstants.ID_ONE);
		this.typeRepository.count();
	}
	
	@Test(expected = EmptyResultDataAccessException.class)
	@Transactional
	@Rollback(true)
	public void testDeleteNonExisting() {
		this.typeService.delete(MainConstants.NON_EXISTING_ID);
	}

	@Test
	@Transactional
	@Rollback(true)
	public void testAddValid() {
		long size = this.typeRepository.count();
		Type type = this.testingType();
		type = this.typeService.save(type, null);
		assertEquals(size + 1, this.typeRepository.count());
		assertEquals(CategoryConstants.ID_ONE, type.getCategory().getId());
		assertEquals(TypeConstants.NON_EXISTING_NAME, type.getName());
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddNullCategory() {
		Type type = this.testingType();
		type.setCategory(null);
		this.typeService.save(type, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddNullName() {
		Type type = this.testingType();
		type.setName(null);
		this.typeService.save(type, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddEmptyName() {
		Type type = this.testingType();
		type.setName("");
		this.typeService.save(type, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddBlankName() {
		Type type = this.testingType();
		type.setName("  ");
		this.typeService.save(type, null);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddNonUniqueName() {
		Type type = this.testingType();
		type.setName(TypeConstants.NAME_ONE);
		this.typeService.save(type, null);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testUpdateValid() {
		long size = this.typeRepository.count();
		Type type = this.testingType();
		type.setId(TypeConstants.ID_ONE);
		type = this.typeService.save(type, null);
		assertEquals(size, this.typeRepository.count());
		assertEquals(TypeConstants.ID_ONE, type.getId());
		assertEquals(CategoryConstants.ID_ONE, type.getCategory().getId());
		assertEquals(TypeConstants.NON_EXISTING_NAME, type.getName());
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateNullCategory() {
		Type type = this.testingType();
		type.setId(TypeConstants.ID_ONE);
		type.setCategory(null);
		this.typeService.save(type, null);
		this.typeRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateNullName() {
		Type type = this.testingType();
		type.setId(TypeConstants.ID_ONE);
		type.setName(null);
		this.typeService.save(type, null);
		this.typeRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateEmptyName() {
		Type type = this.testingType();
		type.setId(TypeConstants.ID_ONE);
		type.setName("");
		this.typeService.save(type, null);
		this.typeRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateBlankName() {
		Type type = this.testingType();
		type.setId(TypeConstants.ID_ONE);
		type.setName("  ");
		this.typeService.save(type, null);
		this.typeRepository.count();
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateNonUniqueName() {
		Type type = this.testingType();
		type.setId(TypeConstants.ID_ONE);
		type.setName(TypeConstants.NAME_TWO);
		this.typeService.save(type, null);
		this.typeRepository.count();
	}
	
	private Type testingType() {
		Type type = new Type();
		type.setCategory(this.categoryRepository.findById(CategoryConstants.ID_ONE).orElse(null));
		type.setName(TypeConstants.NON_EXISTING_NAME);
		return type;
	}
}
