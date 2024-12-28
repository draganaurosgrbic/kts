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
import com.example.demo.constants.TypeConstants;
import com.example.demo.model.Type;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TypeRepositoryTest {
	
	@Autowired
	private TypeRepository typeRepository;
	
	private Pageable pageableTotal = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.TOTAL_SIZE);
	private Pageable pageablePart = PageRequest.of(MainConstants.NONE_SIZE, MainConstants.PART_SIZE);
	private Pageable pageableNonExisting = PageRequest.of(MainConstants.ONE_SIZE, MainConstants.TOTAL_SIZE);
	
	@Test
	public void testFindByNameExisting() {
		Type type = 
				this.typeRepository
				.findByName(TypeConstants.NAME_ONE);
		assertNotNull(type);
		assertEquals(TypeConstants.ID_ONE, type.getId());
		assertEquals(CategoryConstants.ID_ONE, type.getCategory().getId());
		assertEquals(TypeConstants.NAME_ONE, type.getName());
	}

	@Test
	public void testFindByNameNonExisting() {
		Type type = 
				this.typeRepository
				.findByName(TypeConstants.NON_EXISTING_NAME);
		assertNull(type);
	}
	
	@Test
	public void testFilterNamesEmpty() {
		List<String> names = 
				this.typeRepository
				.filterNames(MainConstants.FILTER_ALL);
		assertEquals(MainConstants.TOTAL_SIZE, names.size());
		assertEquals(TypeConstants.NAME_ONE, names.get(0));
		assertEquals(TypeConstants.NAME_TWO, names.get(1));
		assertEquals(TypeConstants.NAME_THREE, names.get(2));
	}
	
	@Test
	public void testFilterNamesAll() {
		List<String> names = 
				this.typeRepository
				.filterNames(TypeConstants.FILTER_NAMES_ALL);
		assertEquals(MainConstants.TOTAL_SIZE, names.size());
		assertEquals(TypeConstants.NAME_ONE, names.get(0));
		assertEquals(TypeConstants.NAME_TWO, names.get(1));
		assertEquals(TypeConstants.NAME_THREE, names.get(2));
	}
	
	@Test
	public void testFilterNamesOne() {
		List<String> names = 
				this.typeRepository
				.filterNames(MainConstants.FILTER_ONE);
		assertEquals(MainConstants.ONE_SIZE, names.size());
		assertEquals(TypeConstants.NAME_ONE, names.get(0));
	}
	
	@Test
	public void testFilterNamesNone() {
		List<String> names = 
				this.typeRepository
				.filterNames(MainConstants.FILTER_NONE);
		assertTrue(names.isEmpty());
	}
	
	@Test
	public void testListAll() {
		Page<Type> page = 
				this.typeRepository
				.findAllByOrderByName(this.pageableTotal);
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
				this.typeRepository
				.findAllByOrderByName(this.pageablePart);
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
				this.typeRepository
				.findAllByOrderByName(this.pageableNonExisting);
		List<Type> types = page.getContent();
		assertTrue(types.isEmpty());
		assertFalse(page.isFirst());
		assertTrue(page.isLast());
	}
	
}
