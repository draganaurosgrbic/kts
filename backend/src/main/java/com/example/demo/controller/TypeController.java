package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constants.Constants;
import com.example.demo.dto.BooleanDTO;
import com.example.demo.dto.StringDTO;
import com.example.demo.dto.TypeDTO;
import com.example.demo.dto.TypeUploadDTO;
import com.example.demo.dto.UniqueCheckDTO;
import com.example.demo.mapper.TypeMapper;
import com.example.demo.model.Type;
import com.example.demo.service.TypeService;

@RestController
@RequestMapping(value = "/api/types", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('admin')")
public class TypeController {

	@Autowired
	private TypeService typeService;
	
	@Autowired
	private TypeMapper typeMapper;
	
	@PostMapping(value = "/has_name")
	public ResponseEntity<BooleanDTO> hasName(@RequestBody UniqueCheckDTO param) {
		return new ResponseEntity<>(new BooleanDTO(this.typeService.hasName(param)), HttpStatus.OK);
	}
	
	@PostMapping(value = "/filter_names")
	public ResponseEntity<List<String>> filterNames(@RequestBody StringDTO filter){
		return new ResponseEntity<>(this.typeService.filterNames(filter.getValue()), HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<TypeDTO>> list(@RequestParam int page, @RequestParam int size, HttpServletResponse response){
		Pageable pageable = PageRequest.of(page, size);
		Page<Type> types = this.typeService.list(pageable);
		response.setHeader(Constants.ENABLE_HEADER, Constants.FIRST_PAGE_HEADER + ", " + Constants.LAST_PAGE_HEADER);
		response.setHeader(Constants.FIRST_PAGE_HEADER, types.isFirst() + "");
		response.setHeader(Constants.LAST_PAGE_HEADER, types.isLast() + "");
		return new ResponseEntity<>(this.typeMapper.map(types.toList()), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable long id) {
		this.typeService.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<TypeDTO> save(@Valid @ModelAttribute TypeUploadDTO typeDTO) {
		Type type = this.typeMapper.map(typeDTO);
		return new ResponseEntity<>(new TypeDTO(this.typeService.save(type,typeDTO.getPlacemarkIcon())), HttpStatus.CREATED);
	}
	
}
