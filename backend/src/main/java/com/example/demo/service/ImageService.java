package com.example.demo.service;

import java.io.File;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.constants.Constants;
import com.example.demo.model.Image;
import com.example.demo.repository.ImageRepository;

@Service
@Transactional(readOnly = true)
public class ImageService {
	
	@Autowired
	private ImageRepository imageRepository;

	@Transactional(readOnly = false)
	public Image save(Image image) {
		return this.imageRepository.save(image);
	}
	
	@Transactional(readOnly = true)
	public String store(MultipartFile data) {
		FileOutputStream fout = null;
		try {
			String fileName = data.getOriginalFilename();
			if (fileName == null || fileName.trim().equalsIgnoreCase("")) {
				return null;
			}
			String[] array = fileName.split("\\.");
			String extension = array[array.length - 1];
			fileName = "image" + this.imageRepository.count() + "." + extension;
			fout = new FileOutputStream(new File(Constants.STATIC_FOLDER + File.separatorChar + fileName));
			fout.write(data.getBytes());
			fout.close();
			return Constants.BACKEND_URL + "/" + fileName;
		}
		catch(Exception e) {
			if (fout != null) {
				try {
					fout.close();
				} 
				catch (Exception e2) {
					return null;
				}
			}
			return null;
		}
	}
	
}
