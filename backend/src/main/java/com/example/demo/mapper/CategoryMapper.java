package com.example.demo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.model.Category;

@Component
public class CategoryMapper {
	
	public Category map(CategoryDTO categoryDTO) {
		Category category = new Category();
		category.setId(categoryDTO.getId());
		category.setName(categoryDTO.getName());
		return category;
	}
	
	public List<CategoryDTO> map(List<Category> categories) {
		return categories.stream().map(CategoryDTO::new).collect(Collectors.toList());
	}
	
}
