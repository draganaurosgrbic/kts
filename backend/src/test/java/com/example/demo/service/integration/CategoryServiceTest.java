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
import com.example.demo.constants.CulturalOfferConstants;
import com.example.demo.constants.MainConstants;
import com.example.demo.dto.UniqueCheckDTO;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceTest {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CategoryRepository categoryRepository;
		
	private Pageable pageableTotal = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.TOTAL_SIZE);
	private Pageable pageablePart = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.PART_SIZE);
	private Pageable pageableNonExisting = PageRequest.of(MainConstants.ONE_SIZE, MainConstants.TOTAL_SIZE);
	
	@Test
	public void testHasNameNonExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(null);
		param.setName(CategoryConstants.NON_EXISTING_NAME);
		assertFalse(this.categoryService.hasName(param));
	}
	
	@Test
	public void testHasNameExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(null);
		param.setName(CategoryConstants.NAME_ONE);
		assertTrue(this.categoryService.hasName(param));
	}
	
	@Test
	public void testFilterNamesEmpty() {
		List<String> names = 
				this.categoryService
				.filterNames(MainConstants.FILTER_ALL);
		assertEquals(MainConstants.TOTAL_SIZE, names.size());
		assertEquals(CategoryConstants.NAME_ONE, names.get(0));
		assertEquals(CategoryConstants.NAME_TWO, names.get(1));
		assertEquals(CategoryConstants.NAME_THREE, names.get(2));
	}
	
	@Test
	public void testFilterNamesAll() {
		List<String> names = 
				this.categoryService
				.filterNames(CategoryConstants.FILTER_NAMES_ALL);
		assertEquals(MainConstants.TOTAL_SIZE, names.size());
		assertEquals(CategoryConstants.NAME_ONE, names.get(0));
		assertEquals(CategoryConstants.NAME_TWO, names.get(1));
		assertEquals(CategoryConstants.NAME_THREE, names.get(2));
	}
	
	@Test
	public void testFilterNamesOne() {
		List<String> names = 
				this.categoryService
				.filterNames(MainConstants.FILTER_ONE);
		assertEquals(MainConstants.ONE_SIZE, names.size());
		assertEquals(CategoryConstants.NAME_ONE, names.get(0));
	}
	
	@Test
	public void testFilterNamesNone() {
		List<String> names = 
				this.categoryService
				.filterNames(MainConstants.FILTER_NONE);
		assertTrue(names.isEmpty());
	}
	
	@Test
	public void testListAll() {
		Page<Category> page = 
				this.categoryService
				.list(this.pageableTotal);
		List<Category> categories = page.getContent();
		assertEquals(MainConstants.TOTAL_SIZE, categories.size());
		assertEquals(CategoryConstants.ID_ONE, categories.get(0).getId());
		assertEquals(CategoryConstants.NAME_ONE, categories.get(0).getName());
		assertEquals(CategoryConstants.ID_TWO, categories.get(1).getId());
		assertEquals(CategoryConstants.NAME_TWO, categories.get(1).getName());
		assertEquals(CategoryConstants.ID_THREE, categories.get(2).getId());
		assertEquals(CategoryConstants.NAME_THREE, categories.get(2).getName());
		assertTrue(page.isFirst());
		assertTrue(page.isLast());
	}
	
	@Test
	public void testListAllPaginated() {
		Page<Category> page = 
				this.categoryService
				.list(this.pageablePart);
		List<Category> categories = page.getContent();
		assertEquals(MainConstants.PART_SIZE, categories.size());
		assertEquals(CategoryConstants.ID_ONE, categories.get(0).getId());
		assertEquals(CategoryConstants.NAME_ONE, categories.get(0).getName());
		assertEquals(CategoryConstants.ID_TWO, categories.get(1).getId());
		assertEquals(CategoryConstants.NAME_TWO, categories.get(1).getName());
		assertTrue(page.isFirst());
		assertFalse(page.isLast());
	}
	
	@Test
	public void testListAllNonExistingPage() {
		Page<Category> page = 
				this.categoryService
				.list(this.pageableNonExisting);
		List<Category> categories = page.getContent();
		assertEquals(MainConstants.NONE_SIZE, categories.size());
		assertFalse(page.isFirst());
		assertTrue(page.isLast());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testDeleteExisting() {
		long id = this.categoryRepository.save(this.testingCategory()).getId();
		long size = this.categoryRepository.count();
		this.categoryService.delete(id);
		assertEquals(size - 1, this.categoryRepository.count());
		assertNull(this.categoryRepository.findById(id).orElse(null));
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	@Transactional
	@Rollback(true)
	public void testDeleteWithType() {
		this.categoryService.delete(CategoryConstants.ID_ONE);
		this.categoryRepository.count();
	}
	
	@Test(expected = EmptyResultDataAccessException.class)
	@Transactional
	@Rollback(true)
	public void testDeleteNonExisting() {
		this.categoryService.delete(MainConstants.NON_EXISTING_ID);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testAddValid() {
		long size = this.categoryRepository.count();
		Category category = this.testingCategory();
		category = this.categoryService.save(category);
		assertEquals(size + 1, this.categoryRepository.count());
		assertEquals(CategoryConstants.NON_EXISTING_NAME, category.getName());
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddNullName() {
		Category category = this.testingCategory();
		category.setName(null);
		this.categoryService.save(category);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddEmptyName() {
		Category category = this.testingCategory();
		category.setName("");
		this.categoryService.save(category);
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddBlankName() {
		Category category = this.testingCategory();
		category.setName("  ");
		this.categoryService.save(category);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	@Transactional
	@Rollback(true)
	public void testAddNonUniqueName() {
		Category category = this.testingCategory();
		category.setName(CategoryConstants.NAME_ONE);
		this.categoryService.save(category);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testUpdateValid() {
		long size = this.categoryRepository.count();
		Category category = this.testingCategory();
		category.setId(CulturalOfferConstants.ID_ONE);
		category = this.categoryRepository.save(category);
		assertEquals(size, this.categoryRepository.count());
		assertEquals(CategoryConstants.ID_ONE, category.getId());
		assertEquals(CategoryConstants.NON_EXISTING_NAME, category.getName());
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateNullName() {
		Category category = this.testingCategory();
		category.setId(CulturalOfferConstants.ID_ONE);
		category.setName(null);
		this.categoryService.save(category);
		this.categoryRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateEmptyName() {
		Category category = this.testingCategory();
		category.setId(CulturalOfferConstants.ID_ONE);
		category.setName("");
		this.categoryService.save(category);
		this.categoryRepository.count();
	}
	
	@Test(expected = ConstraintViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateBlankName() {
		Category category = this.testingCategory();
		category.setId(CulturalOfferConstants.ID_ONE);
		category.setName("  ");
		this.categoryService.save(category);
		this.categoryRepository.count();
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	@Transactional
	@Rollback(true)
	public void testUpdateNonUniqueName() {
		Category category = this.testingCategory();
		category.setId(CulturalOfferConstants.ID_ONE);
		category.setName(CategoryConstants.NAME_TWO);
		this.categoryService.save(category);
		this.categoryRepository.count();
	}
	
	private Category testingCategory() {
		Category category = new Category();
		category.setName(CategoryConstants.NON_EXISTING_NAME);
		return category;
	}

}
