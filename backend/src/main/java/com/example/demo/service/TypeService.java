package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.UniqueCheckDTO;
import com.example.demo.model.Image;
import com.example.demo.model.Type;
import com.example.demo.repository.TypeRepository;

@Service
@Transactional(readOnly = true)
public class TypeService {
	
	@Autowired
	private TypeRepository typeRepository;
	
	@Autowired
	private ImageService imageService;
	
	@Transactional(readOnly = true)
	public boolean hasName(UniqueCheckDTO param) {
		return this.typeRepository.findByName(param.getName()) != null;
	}
	
	@Transactional(readOnly = true)
	public List<String> filterNames(String filter){
		return this.typeRepository.filterNames(filter);
	}
	
	@Transactional(readOnly = true)
	public Page<Type> list(Pageable pageable) {
		return this.typeRepository.findAllByOrderByName(pageable);
	}

	@Transactional(readOnly = false)
	public void delete(long id) {
		this.typeRepository.deleteById(id);
	}

	@Transactional(readOnly = false)
	public Type save(Type type, MultipartFile upload) {
		if(upload != null) {
			Image image = new Image(this.imageService.store(upload));
			type.setPlacemarkIcon(image.getPath());
			this.imageService.save(image);
		}
		return this.typeRepository.save(type);
	}
		
}
