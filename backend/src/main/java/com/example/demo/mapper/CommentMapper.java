package com.example.demo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CommentDTO;
import com.example.demo.dto.CommentUploadDTO;
import com.example.demo.model.Comment;
import com.example.demo.repository.CulturalOfferRepository;
import com.example.demo.repository.ImageRepository;
import com.example.demo.service.UserService;

@Component
public class CommentMapper {

	@Autowired
	private CulturalOfferRepository culturalOfferRepository;
		
	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private UserService userService;

	public List<CommentDTO> map(List<Comment> comments){
		return comments.stream().map(CommentDTO::new).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public Comment map(CommentUploadDTO commentDTO) {
		Comment comment = new Comment();
		comment.setId(commentDTO.getId());
		comment.setUser(this.userService.currentUser());
		comment.setCulturalOffer(this.culturalOfferRepository.findById(commentDTO.getCulturalOfferId()).orElse(null));
		comment.setRate(commentDTO.getRate());
		comment.setText(commentDTO.getText());
		if (commentDTO.getImagePaths() != null) {
			commentDTO.getImagePaths().stream().forEach(path -> comment.addImage(this.imageRepository.findByPath(path)));
		}
		return comment;
	}
	
}
