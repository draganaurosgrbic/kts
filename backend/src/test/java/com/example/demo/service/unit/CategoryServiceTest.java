package com.example.demo.service.unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

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

import com.example.demo.constants.CategoryConstants;
import com.example.demo.constants.MainConstants;
import com.example.demo.dto.UniqueCheckDTO;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryServiceTest {
	
	@Autowired
	private CategoryService categoryService;
	
	@MockBean
	private CategoryRepository categoryRepository;
	
	private Pageable pageableTotal = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.TOTAL_SIZE);
	private Pageable pageablePart = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.PART_SIZE);
	private Pageable pageableNonExisting = PageRequest.of(MainConstants.ONE_SIZE, MainConstants.TOTAL_SIZE);
	
	@Test
	public void testHasNameExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(null);
		param.setName(CategoryConstants.NAME_ONE);
		Mockito.when(this.categoryRepository.findByName(param.getName()))
		.thenReturn(new Category());
		assertTrue(this.categoryService.hasName(param));
	}
	
	@Test
	public void testHasNameNonExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(null);
		param.setName(CategoryConstants.NON_EXISTING_NAME);
		Mockito.when(this.categoryRepository.findByName(param.getName()))
		.thenReturn(null);
		assertFalse(this.categoryService.hasName(param));
	}
	
	@Test
	public void testFilterNamesEmpty() {
		Mockito.when(this.categoryRepository
				.filterNames(MainConstants.FILTER_ALL))
				.thenReturn(List.of(
				CategoryConstants.NAME_ONE, 
				CategoryConstants.NAME_TWO, 
				CategoryConstants.NAME_THREE));
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
		Mockito.when(this.categoryRepository
				.filterNames(CategoryConstants.FILTER_NAMES_ALL))
				.thenReturn(List.of(
						CategoryConstants.NAME_ONE, 
						CategoryConstants.NAME_TWO, 
						CategoryConstants.NAME_THREE));
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
		Mockito.when(this.categoryRepository
				.filterNames(MainConstants.FILTER_ONE))
				.thenReturn(List.of(
						CategoryConstants.NAME_ONE));
		List<String> names = 
				this.categoryService
				.filterNames(MainConstants.FILTER_ONE);
		assertEquals(MainConstants.ONE_SIZE, names.size());
		assertEquals(CategoryConstants.NAME_ONE, names.get(0));
	}
	
	@Test
	public void testFilterNamesNone() {
		Mockito.when(this.categoryRepository
				.filterNames(MainConstants.FILTER_NONE))
				.thenReturn(List.of());
		List<String> names = 
				this.categoryService
				.filterNames(MainConstants.FILTER_NONE);
		assertTrue(names.isEmpty());
	}
	
	@Test
	public void testListAll() {
		Mockito.when(this.categoryRepository
				.findAllByOrderByName(this.pageableTotal))
				.thenReturn(new PageImpl<>(List.of(
						this.listCategory(1), 
						this.listCategory(2), 
						this.listCategory(3))));
		List<Category> categories = 
				this.categoryService
				.list(this.pageableTotal).getContent();
		assertEquals(MainConstants.TOTAL_SIZE, categories.size());
		assertEquals(CategoryConstants.ID_ONE, categories.get(0).getId());
		assertEquals(CategoryConstants.NAME_ONE, categories.get(0).getName());
		assertEquals(CategoryConstants.ID_TWO, categories.get(1).getId());
		assertEquals(CategoryConstants.NAME_TWO, categories.get(1).getName());
		assertEquals(CategoryConstants.ID_THREE, categories.get(2).getId());
		assertEquals(CategoryConstants.NAME_THREE, categories.get(2).getName());
	}
	
	@Test
	public void testListAllPaginated() {
		Mockito.when(this.categoryRepository
				.findAllByOrderByName(this.pageablePart))
				.thenReturn(new PageImpl<>(List.of(
						this.listCategory(1), 
						this.listCategory(2))));
		List<Category> categories = 
				this.categoryService
				.list(this.pageablePart).getContent();
		assertEquals(MainConstants.PART_SIZE, categories.size());
		assertEquals(CategoryConstants.ID_ONE, categories.get(0).getId());
		assertEquals(CategoryConstants.NAME_ONE, categories.get(0).getName());
		assertEquals(CategoryConstants.ID_TWO, categories.get(1).getId());
		assertEquals(CategoryConstants.NAME_TWO, categories.get(1).getName());
	}
	
	@Test
	public void testListAllNonExistingPage() {
		Mockito.when(this.categoryRepository
				.findAllByOrderByName(this.pageableNonExisting))
				.thenReturn(new PageImpl<>(List.of()));
		List<Category> categories = 
				this.categoryService
				.list(this.pageableNonExisting).getContent();
		assertTrue(categories.isEmpty());
	}
	
	@Test
	public void testDeleteExisting() {
		Mockito.when(this.categoryRepository.count())
		.thenReturn((long) MainConstants.TOTAL_SIZE);
		long size = this.categoryRepository.count();
		Category category = this.testingCategory();
		Mockito.when(this.categoryRepository.save(category))
		.thenReturn(category);
		this.categoryService.save(category);
		Mockito.when(this.categoryRepository.count())
		.thenReturn(size + 1);
		assertEquals(size + 1, this.categoryRepository.count());
		this.categoryService.delete(size + 1);
		Mockito.when(this.categoryRepository.count())
		.thenReturn(size);
		assertEquals(size, this.categoryRepository.count());
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testDeleteWithType() {
		Mockito.doThrow(ConstraintViolationException.class)
		.when(this.categoryRepository).deleteById(CategoryConstants.ID_THREE);
		this.categoryService.delete(CategoryConstants.ID_THREE);
	}
	
	@Test(expected = EmptyResultDataAccessException.class)
	public void testDeleteNonExisting() {
		Mockito.doThrow(EmptyResultDataAccessException.class)
		.when(this.categoryRepository).deleteById(MainConstants.NON_EXISTING_ID);
		this.categoryService.delete(MainConstants.NON_EXISTING_ID);
	}
	
	@Test
	public void testAddValid() {
		Mockito.when(this.categoryRepository.count())
		.thenReturn((long) MainConstants.TOTAL_SIZE);
		long size = this.categoryRepository.count();
		Category category = this.testingCategory();
		Mockito.when(this.categoryRepository.save(category))
		.thenReturn(category);
		category = this.categoryRepository.save(category);
		Mockito.when(this.categoryRepository.count())
		.thenReturn(size + 1);
		assertEquals(size + 1, this.categoryRepository.count());
		assertEquals(CategoryConstants.NON_EXISTING_NAME, category.getName());
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddNullName() {
		Category category = this.testingCategory();
		category.setName(null);
		Mockito.when(this.categoryRepository.save(category))
		.thenThrow(ConstraintViolationException.class);
		this.categoryService.save(category);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddEmptyName() {
		Category category = this.testingCategory();
		category.setName("");
		Mockito.when(this.categoryRepository.save(category))
		.thenThrow(ConstraintViolationException.class);
		this.categoryService.save(category);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddBlankName() {
		Category category = this.testingCategory();
		category.setName("  ");
		Mockito.when(this.categoryRepository.save(category))
		.thenThrow(ConstraintViolationException.class);
		this.categoryService.save(category);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testAddNonUniqueName() {
		Category category = this.testingCategory();
		category.setName(CategoryConstants.NAME_ONE);
		Mockito.when(this.categoryRepository.save(category))
		.thenThrow(DataIntegrityViolationException.class);
		this.categoryService.save(category);
	}
	
	@Test
	public void testUpdateValid() {
		Mockito.when(this.categoryRepository.count())
		.thenReturn((long) MainConstants.TOTAL_SIZE);
		long size = this.categoryRepository.count();
		Category category = this.testingCategory();
		category.setId(CategoryConstants.ID_ONE);
		Mockito.when(this.categoryRepository.save(category))
		.thenReturn(category);
		category = this.categoryRepository.save(category);
		assertEquals(size, this.categoryRepository.count());
		assertEquals(CategoryConstants.ID_ONE, category.getId());
		assertEquals(CategoryConstants.NON_EXISTING_NAME, category.getName());
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateNullName() {
		Category category = this.testingCategory();
		category.setId(CategoryConstants.ID_ONE);
		category.setName(null);
		Mockito.when(this.categoryRepository.save(category))
		.thenThrow(ConstraintViolationException.class);
		this.categoryService.save(category);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateEmptyName() {
		Category category = this.testingCategory();
		category.setId(CategoryConstants.ID_ONE);
		category.setName("");
		Mockito.when(this.categoryRepository.save(category))
		.thenThrow(ConstraintViolationException.class);
		this.categoryService.save(category);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateBlankName() {
		Category category = this.testingCategory();
		category.setId(CategoryConstants.ID_ONE);
		category.setName("  ");
		Mockito.when(this.categoryRepository.save(category))
		.thenThrow(ConstraintViolationException.class);
		this.categoryService.save(category);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testUpdateNonUniqueName() {
		Category category = this.testingCategory();
		category.setId(CategoryConstants.ID_ONE);
		category.setName(CategoryConstants.NAME_TWO);
		Mockito.when(this.categoryRepository.save(category))
		.thenThrow(DataIntegrityViolationException.class);
		this.categoryService.save(category);
	}
	
	private Category testingCategory() {
		Category category = new Category();
		category.setName(CategoryConstants.NON_EXISTING_NAME);
		return category;
	}
	
	private Category listCategory(int index) {
		if (index == 1) {
			Category category = new Category();
			category.setId(CategoryConstants.ID_ONE);
			category.setName(CategoryConstants.NAME_ONE);
			return category;
		}
		if (index == 2) {
			Category category = new Category();
			category.setId(CategoryConstants.ID_TWO);
			category.setName(CategoryConstants.NAME_TWO);
			return category;
		}
		Category category = new Category();
		category.setId(CategoryConstants.ID_THREE);
		category.setName(CategoryConstants.NAME_THREE);
		return category;
	}

}
