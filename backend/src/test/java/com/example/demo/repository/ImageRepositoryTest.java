package com.example.demo.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.constants.ImageConstants;
import com.example.demo.model.Image;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageRepositoryTest {
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Test
	public void testFindByPathExisting() {
		Image image = 
				this.imageRepository
				.findByPath(ImageConstants.PATH_ONE);
		assertNotNull(image);
		assertEquals(ImageConstants.ID_ONE, image.getId());
		assertEquals(ImageConstants.PATH_ONE, image.getPath());
	}
	
	@Test
	public void testFindByPathNonExisting() {
		Image image = 
				this.imageRepository
				.findByPath(ImageConstants.NON_EXISTING_PATH);
		assertNull(image);
	}

}
