package com.example.demo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.TypeDTO;
import com.example.demo.dto.TypeUploadDTO;
import com.example.demo.model.Type;
import com.example.demo.repository.CategoryRepository;

@Component
public class TypeMapper {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Type map(TypeUploadDTO typeDTO) {
		Type type = new Type();
		type.setId(typeDTO.getId());
		type.setCategory(this.categoryRepository.findByName(typeDTO.getCategory()));
		type.setName(typeDTO.getName());
		return type;
	}
	
	public List<TypeDTO> map(List<Type> types) {
		return types.stream().map(TypeDTO::new).collect(Collectors.toList());
	}

}
