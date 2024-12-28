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

import com.example.demo.constants.CategoryConstants;
import com.example.demo.constants.MainConstants;
import com.example.demo.model.Category;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryRepositoryTest {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	private Pageable pageableTotal = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.TOTAL_SIZE);
	private Pageable pageablePart = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.PART_SIZE);
	private Pageable pageableNonExisting = PageRequest.of(MainConstants.ONE_SIZE, MainConstants.TOTAL_SIZE);
	
	@Test
	public void testFindByNameExisting() {
		Category category = 
				this.categoryRepository
				.findByName(CategoryConstants.NAME_ONE);
		assertNotNull(category);
		assertEquals(CategoryConstants.ID_ONE, category.getId());
		assertEquals(CategoryConstants.NAME_ONE, category.getName());
	}

	@Test
	public void testFindByNameNonExisting() {
		Category category = 
				this.categoryRepository
				.findByName(CategoryConstants.NON_EXISTING_NAME);
		assertNull(category);
	}
	
	@Test
	public void testFilterNamesEmpty() {
		List<String> names = 
				this.categoryRepository
				.filterNames(MainConstants.FILTER_ALL);
		assertEquals(MainConstants.TOTAL_SIZE, names.size());
		assertEquals(CategoryConstants.NAME_ONE, names.get(0));
		assertEquals(CategoryConstants.NAME_TWO, names.get(1));
		assertEquals(CategoryConstants.NAME_THREE, names.get(2));
	}
	
	@Test
	public void testFilterNamesAll() {
		List<String> names = 
				this.categoryRepository
				.filterNames(CategoryConstants.FILTER_NAMES_ALL);
		assertEquals(MainConstants.TOTAL_SIZE, names.size());
		assertEquals(CategoryConstants.NAME_ONE, names.get(0));
		assertEquals(CategoryConstants.NAME_TWO, names.get(1));
		assertEquals(CategoryConstants.NAME_THREE, names.get(2));
	}
	
	@Test
	public void testFilterNamesOne() {
		List<String> names = 
				this.categoryRepository
				.filterNames(MainConstants.FILTER_ONE);
		assertEquals(MainConstants.ONE_SIZE, names.size());
		assertEquals(CategoryConstants.NAME_ONE, names.get(0));
	}
	
	@Test
	public void testFilterNamesNone() {
		List<String> names = 
				this.categoryRepository
				.filterNames(MainConstants.FILTER_NONE);
		assertTrue(names.isEmpty());
	}
	
	@Test
	public void testListAll() {
		Page<Category> page = 
				this.categoryRepository
				.findAllByOrderByName(this.pageableTotal);
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
				this.categoryRepository
				.findAllByOrderByName(this.pageablePart);
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
				this.categoryRepository
				.findAllByOrderByName(this.pageableNonExisting);
		List<Category> categories = page.getContent();
		assertEquals(MainConstants.NONE_SIZE, categories.size());
		assertFalse(page.isFirst());
		assertTrue(page.isLast());
	}


}
