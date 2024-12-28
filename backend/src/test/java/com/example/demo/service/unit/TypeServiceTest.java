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
import com.example.demo.constants.TypeConstants;
import com.example.demo.dto.UniqueCheckDTO;
import com.example.demo.model.Category;
import com.example.demo.model.Type;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.TypeRepository;
import com.example.demo.service.TypeService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TypeServiceTest {
	
	@Autowired
	private TypeService typeService;
	
	@MockBean
	private TypeRepository typeRepository;
	
	@MockBean
	private CategoryRepository categoryRepository;
	
	private Pageable pageableTotal = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.TOTAL_SIZE);
	private Pageable pageablePart = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.PART_SIZE);
	private Pageable pageableNonExisting = PageRequest.of(MainConstants.ONE_SIZE, MainConstants.TOTAL_SIZE);
	
	@Test
	public void testHasNameExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(null);
		param.setName(TypeConstants.NAME_ONE);
		Mockito.when(this.typeRepository.findByName(param.getName()))
		.thenReturn(new Type());
		assertTrue(this.typeService.hasName(param));
	}
	
	@Test
	public void testHasNameNonExisting() {
		UniqueCheckDTO param = new UniqueCheckDTO();
		param.setId(null);
		param.setName(TypeConstants.NON_EXISTING_NAME);
		Mockito.when(this.typeRepository.findByName(param.getName()))
		.thenReturn(null);
		assertFalse(this.typeService.hasName(param));
	}
	
	@Test
	public void testFilterNamesEmpty() {
		Mockito.when(this.typeRepository.filterNames(MainConstants.FILTER_ALL))
		.thenReturn(List.of(
				TypeConstants.NAME_ONE, 
				TypeConstants.NAME_TWO, 
				TypeConstants.NAME_THREE));
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
		Mockito.when(this.typeRepository.filterNames(TypeConstants.FILTER_NAMES_ALL))
		.thenReturn(List.of(
				TypeConstants.NAME_ONE, 
				TypeConstants.NAME_TWO, 
				TypeConstants.NAME_THREE));
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
		Mockito.when(this.typeRepository.filterNames(MainConstants.FILTER_ONE))
		.thenReturn(List.of(TypeConstants.NAME_ONE));
		List<String> names = 
				this.typeService
				.filterNames(MainConstants.FILTER_ONE);
		assertEquals(MainConstants.ONE_SIZE, names.size());
		assertEquals(TypeConstants.NAME_ONE, names.get(0));
	}
	
	@Test
	public void testFilterNamesNone() {
		Mockito.when(this.categoryRepository.filterNames(MainConstants.FILTER_NONE))
		.thenReturn(List.of());
		List<String> names = 
				this.typeService
				.filterNames(MainConstants.FILTER_NONE);
		assertTrue(names.isEmpty());
	}
	
	@Test
	public void testListAll() {
		Mockito.when(this.typeRepository.findAllByOrderByName(this.pageableTotal))
		.thenReturn(new PageImpl<>(List.of(
				this.listType(1), 
				this.listType(2), 
				this.listType(3))));
		List<Type> types = 
				this.typeService
				.list(this.pageableTotal).getContent();
		assertEquals(MainConstants.TOTAL_SIZE, types.size());
		assertEquals(TypeConstants.ID_ONE, types.get(0).getId());
		assertEquals(TypeConstants.NAME_ONE, types.get(0).getName());
		assertEquals(TypeConstants.ID_TWO, types.get(1).getId());
		assertEquals(TypeConstants.NAME_TWO, types.get(1).getName());
		assertEquals(TypeConstants.ID_THREE, types.get(2).getId());
		assertEquals(TypeConstants.NAME_THREE, types.get(2).getName());
	}
	
	@Test
	public void testListAllPaginated() {
		Mockito.when(this.typeRepository.findAllByOrderByName(this.pageablePart))
		.thenReturn(new PageImpl<>(List.of(
				this.listType(1), 
				this.listType(2))));
		List<Type> types = 
				this.typeService
				.list(this.pageablePart).getContent();
		assertEquals(MainConstants.TOTAL_SIZE - 1, types.size());
		assertEquals(TypeConstants.ID_ONE, types.get(0).getId());
		assertEquals(TypeConstants.NAME_ONE, types.get(0).getName());
		assertEquals(TypeConstants.ID_TWO, types.get(1).getId());
		assertEquals(TypeConstants.NAME_TWO, types.get(1).getName());
	}
	
	@Test
	public void testListAllNonExistingPage() {
		Mockito.when(this.typeRepository.findAllByOrderByName(this.pageableNonExisting))
		.thenReturn(new PageImpl<>(List.of()));
		List<Type> types = 
				this.typeService
				.list(this.pageableNonExisting).getContent();
		assertTrue(types.isEmpty());
	}
	
	@Test
	public void testDeleteExisting() {
		Mockito.when(this.typeRepository.count())
		.thenReturn((long) MainConstants.TOTAL_SIZE);
		long size = this.typeRepository.count();
		Type type = this.testingType();
		Mockito.when(this.typeRepository.save(type))
		.thenReturn(type);
		type = this.typeService.save(type, null);
		Mockito.when(this.typeRepository.count())
		.thenReturn(size + 1);
		assertEquals(size + 1, this.typeRepository.count());
		this.typeService.delete(size + 1);
		Mockito.when(this.typeRepository.count())
		.thenReturn(size);
		assertEquals(size, this.typeRepository.count());
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testDeleteWithCulturalOffer() {
		Mockito.doThrow(ConstraintViolationException.class)
		.when(this.typeRepository).deleteById(TypeConstants.ID_THREE);
		this.typeService.delete(CategoryConstants.ID_THREE);
	}
	
	@Test(expected = EmptyResultDataAccessException.class)
	public void testDeleteNonExisting() {
		Mockito.doThrow(EmptyResultDataAccessException.class)
		.when(this.typeRepository).deleteById(MainConstants.NON_EXISTING_ID);
		this.typeService.delete(MainConstants.NON_EXISTING_ID);
	}
	
	@Test
	public void testAddValid() {
		Mockito.when(this.typeRepository.count())
		.thenReturn((long) MainConstants.TOTAL_SIZE);
		long size = this.typeRepository.count();
		Type type = this.testingType();
		Mockito.when(this.typeRepository.save(type)).thenReturn(type);
		type = this.typeRepository.save(type);
		Mockito.when(this.typeRepository.count())
		.thenReturn(size + 1);
		assertEquals(size + 1, this.typeRepository.count());
		assertEquals(TypeConstants.NON_EXISTING_NAME, type.getName());
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddNullCategory() {
		Type type = this.testingType();
		type.setCategory(null);
		Mockito.when(this.typeRepository.save(type))
		.thenThrow(ConstraintViolationException.class);
		this.typeService.save(type, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddNullName() {
		Type type = this.testingType();
		type.setName(null);
		Mockito.when(this.typeRepository.save(type))
		.thenThrow(ConstraintViolationException.class);
		this.typeService.save(type, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddEmptyName() {
		Type type = this.testingType();
		type.setName("");
		Mockito.when(this.typeRepository.save(type))
		.thenThrow(ConstraintViolationException.class);
		this.typeService.save(type, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddBlankName() {
		Type type = this.testingType();
		type.setName("  ");
		Mockito.when(this.typeRepository.save(type))
		.thenThrow(ConstraintViolationException.class);
		this.typeService.save(type, null);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testAddNonUniqueName() {
		Type type = this.testingType();
		type.setName(TypeConstants.NAME_ONE);
		Mockito.when(this.typeRepository.save(type))
		.thenThrow(DataIntegrityViolationException.class);
		this.typeService.save(type, null);
	}
	
	@Test
	public void testUpdateValid() {
		Mockito.when(this.typeRepository.count())
		.thenReturn((long) MainConstants.TOTAL_SIZE);
		long size = this.typeRepository.count();
		Type type = this.testingType();
		type.setId(TypeConstants.ID_ONE);
		Mockito.when(this.typeRepository.save(type))
		.thenReturn(type);
		type = this.typeRepository.save(type);
		assertEquals(size, this.typeRepository.count());
		assertEquals(TypeConstants.ID_ONE, type.getId());
		assertEquals(TypeConstants.NON_EXISTING_NAME, type.getName());
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateNullCategory() {
		Type type = this.testingType();
		type.setId(TypeConstants.ID_ONE);
		type.setCategory(null);
		Mockito.when(this.typeRepository.save(type))
		.thenThrow(ConstraintViolationException.class);
		this.typeService.save(type, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateNullName() {
		Type type = this.testingType();
		type.setId(TypeConstants.ID_ONE);
		type.setName(null);
		Mockito.when(this.typeRepository.save(type))
		.thenThrow(ConstraintViolationException.class);
		this.typeService.save(type, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateEmptyName() {
		Type type = this.testingType();
		type.setId(TypeConstants.ID_ONE);
		type.setName("");
		Mockito.when(this.typeRepository.save(type))
		.thenThrow(ConstraintViolationException.class);
		this.typeService.save(type, null);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateBlankName() {
		Type type = this.testingType();
		type.setId(TypeConstants.ID_ONE);
		type.setName("  ");
		Mockito.when(this.typeRepository.save(type))
		.thenThrow(ConstraintViolationException.class);
		this.typeService.save(type, null);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void testUpdateNonUniqueName() {
		Type type = this.testingType();
		type.setId(TypeConstants.ID_ONE);
		type.setName(TypeConstants.NAME_TWO);
		Mockito.when(this.typeRepository.save(type))
		.thenThrow(DataIntegrityViolationException.class);
		this.typeService.save(type, null);
	}
	
	private Type testingType() {
		Type type = new Type();
		type.setCategory(this.categoryRepository.findById(CategoryConstants.ID_ONE).orElse(null));
		type.setName(TypeConstants.NON_EXISTING_NAME);
		return type;
	}
	
	private Type listType(int index) {
		if (index == 1) {
			Type type = new Type();
			type.setId(TypeConstants.ID_ONE);
			Category category = new Category();
			category.setId(CategoryConstants.ID_ONE);
			type.setCategory(category);
			type.setName(TypeConstants.NAME_ONE);
			return type;
		}
		if (index == 2) {
			Type type = new Type();
			type.setId(TypeConstants.ID_TWO);
			Category category = new Category();
			category.setId(CategoryConstants.ID_TWO);
			type.setCategory(category);
			type.setName(TypeConstants.NAME_TWO);
			return type;
		}
		Type type = new Type();
		type.setId(TypeConstants.ID_THREE);
		Category category = new Category();
		category.setId(CategoryConstants.ID_THREE);
		type.setCategory(category);
		type.setName(TypeConstants.NAME_THREE);
		return type;
	}

}
