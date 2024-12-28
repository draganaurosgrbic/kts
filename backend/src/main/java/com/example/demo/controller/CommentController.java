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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constants.Constants;
import com.example.demo.dto.CommentDTO;
import com.example.demo.dto.CommentUploadDTO;
import com.example.demo.dto.DoubleDTO;
import com.example.demo.mapper.CommentMapper;
import com.example.demo.model.Comment;
import com.example.demo.service.CommentService;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("permitAll()")
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private CommentMapper commentMapper;

	@GetMapping(value = "/api/cultural_offers/{culturalOfferId}/comments")
	public ResponseEntity<List<CommentDTO>> list(@PathVariable long culturalOfferId, @RequestParam int page, @RequestParam int size, HttpServletResponse response){
		Pageable pageable = PageRequest.of(page, size);
		Page<Comment> comments = this.commentService.list(culturalOfferId, pageable);
		response.setHeader(Constants.ENABLE_HEADER, Constants.FIRST_PAGE_HEADER + ", " + Constants.LAST_PAGE_HEADER);
		response.setHeader(Constants.FIRST_PAGE_HEADER, comments.isFirst() + "");
		response.setHeader(Constants.LAST_PAGE_HEADER, comments.isLast() + "");
		return new ResponseEntity<>(this.commentMapper.map(comments.toList()), HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('guest')")
	@DeleteMapping(value = "/api/comments/{id}")
	public ResponseEntity<DoubleDTO> delete(@PathVariable long id) {
		long culturalOfferId = this.commentService.delete(id);
		return new ResponseEntity<>(new DoubleDTO(this.commentService.totalRate(culturalOfferId)), HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('guest')")
	@PostMapping(value = "/api/comments")
	public ResponseEntity<DoubleDTO> save(@Valid @ModelAttribute CommentUploadDTO commentDTO) {
		Comment comment = this.commentService.save(this.commentMapper.map(commentDTO), commentDTO.getImages());
		return new ResponseEntity<>(new DoubleDTO(this.commentService.totalRate(comment.getCulturalOffer().getId())), HttpStatus.CREATED);
	}
		
}
