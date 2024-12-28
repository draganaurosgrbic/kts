package com.example.demo.service.unit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.constants.Constants;
import com.example.demo.constants.ImageConstants;
import com.example.demo.constants.MainConstants;
import com.example.demo.model.Image;
import com.example.demo.repository.ImageRepository;
import com.example.demo.service.ImageService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageServiceTest {

	@Autowired
	private ImageService imageService;
	
	@MockBean
	private ImageRepository imageRepository;
	
	@Test
	public void testAddValid() {
		Mockito.when(this.imageRepository.count())
		.thenReturn((long) MainConstants.ONE_SIZE);
		long size = this.imageRepository.count();
		Image image = this.testingImage();
		Mockito.when(this.imageRepository.save(image))
		.thenReturn(image);
		image = this.imageService.save(image);
		Mockito.when(this.imageRepository.count())
		.thenReturn(size + 1);
		assertEquals(size + 1, this.imageRepository.count());
		assertEquals(ImageConstants.NON_EXISTING_PATH, image.getPath());
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddNullPath() {
		Image image = this.testingImage();
		image.setPath(null);
		Mockito.when(this.imageRepository.save(image))
		.thenThrow(ConstraintViolationException.class);
		this.imageService.save(image);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddEmptyPath() {
		Image image = this.testingImage();
		image.setPath("");
		Mockito.when(this.imageRepository.save(image))
		.thenThrow(ConstraintViolationException.class);
		this.imageService.save(image);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testAddBlankPath() {
		Image image = this.testingImage();
		image.setPath("  ");
		Mockito.when(this.imageRepository.save(image))
		.thenThrow(ConstraintViolationException.class);
		this.imageService.save(image);
	}
	
	@Test
	public void testUpdateValid() {
		Mockito.when(this.imageRepository.count())
		.thenReturn((long) MainConstants.ONE_SIZE);
		long size = this.imageRepository.count();
		Image image = this.testingImage();
		image.setId(ImageConstants.ID_ONE);
		Mockito.when(this.imageRepository.save(image))
		.thenReturn(image);
		image = this.imageService.save(image);
		assertEquals(size, this.imageRepository.count());
		assertEquals(ImageConstants.ID_ONE, image.getId());
		assertEquals(ImageConstants.NON_EXISTING_PATH, image.getPath());
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateNullPath() {
		Image image = this.testingImage();
		image.setId(ImageConstants.ID_ONE);
		image.setPath(null);
		Mockito.when(this.imageRepository.save(image))
		.thenThrow(ConstraintViolationException.class);
		this.imageService.save(image);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateEmptyPath() {
		Image image = this.testingImage();
		image.setId(ImageConstants.ID_ONE);
		image.setPath("");
		Mockito.when(this.imageRepository.save(image))
		.thenThrow(ConstraintViolationException.class);
		this.imageService.save(image);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void testUpdateBlankPath() {
		Image image = this.testingImage();
		image.setId(ImageConstants.ID_ONE);
		image.setPath("  ");
		Mockito.when(this.imageRepository.save(image))
		.thenThrow(ConstraintViolationException.class);
		this.imageService.save(image);
	}
	
	@Test
	public void testStoreValid() throws IOException {
		Mockito.when(this.imageRepository.count())
		.thenReturn((long) MainConstants.ONE_SIZE);
		String path = this.imageService.store(this.testingMultipartValid());
		long size = this.imageRepository.count();
		assertEquals(Constants.BACKEND_URL + "/image" + size + ".jpg", path);
		File f = new File(Constants.STATIC_FOLDER + File.separatorChar + "image" + size + ".jpg");
		assertTrue(f.exists());
		f.delete();
	}
	
	@Test
	public void testStoreNullName() throws IOException {
		assertNull(this.imageService.store(this.testingMultipartNullName()));
	}
	
	@Test
	public void testStoreEmptyName() throws IOException {
		assertNull(this.imageService.store(this.testingMultipartEmptyName()));
	}
	
	@Test
	public void testStoreBlankName() throws IOException {
		assertNull(this.imageService.store(this.testingMultipartBlankName()));
	}
	
	private Image testingImage() {
		Image image = new Image();
		image.setPath(ImageConstants.NON_EXISTING_PATH);
		return image;
	}
	
	public MultipartFile testingMultipartValid() throws IOException {
		return new MockMultipartFile(ImageConstants.PATH_ONE, "dummyPic.jpg",
	            "image/jpeg",
	            "This is a dummy file content".getBytes(StandardCharsets.UTF_8));
	}

	public MultipartFile testingMultipartNullName() throws IOException {
		return new MockMultipartFile(ImageConstants.PATH_ONE, null,
	            "image/jpeg",
	            "This is a dummy file content".getBytes(StandardCharsets.UTF_8));
	}
	
	public MultipartFile testingMultipartEmptyName() throws IOException {
		return new MockMultipartFile(ImageConstants.PATH_ONE, "",
	            "image/jpeg",
	            "This is a dummy file content".getBytes(StandardCharsets.UTF_8));
	}
	
	public MultipartFile testingMultipartBlankName() throws IOException {
		return new MockMultipartFile(ImageConstants.PATH_ONE, "  ",
	            "image/jpeg",
	            "This is a dummy file content".getBytes(StandardCharsets.UTF_8));
	}

}
