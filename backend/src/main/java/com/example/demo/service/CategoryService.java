package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.UniqueCheckDTO;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;

@Service
@Transactional(readOnly = true)
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public boolean hasName(UniqueCheckDTO param) {
		return this.categoryRepository.findByName(param.getName()) != null;
	}
	
	@Transactional(readOnly = true)
	public List<String> filterNames(String filter){
		return this.categoryRepository.filterNames(filter);
	}
		
	@Transactional(readOnly = true)
	public Page<Category> list(Pageable pageable){
		return this.categoryRepository.findAllByOrderByName(pageable);
	}
	
	@Transactional(readOnly = false)
	public void delete(long id) {
		this.categoryRepository.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public Category save(Category category) {
		return this.categoryRepository.save(category);
	}
		
}
