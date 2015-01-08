package org.coursera.capstone.gift;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.coursera.capstone.gift.repository.ImageEntity;

public class ImageFileManager {

	
	public static ImageFileManager get() throws IOException {
		return new ImageFileManager();
	}
	
	private Path targetDir_ = Paths.get("images");
	
	// The VideoFileManager.get() method should be used
	// to obtain an instance
	private ImageFileManager() throws IOException{
		if(!Files.exists(targetDir_)){
			Files.createDirectories(targetDir_);
		}
	}
	
	// Private helper method for resolving video file paths
	private Path getImagePath(ImageEntity v){
		assert(v != null);
		
		return targetDir_.resolve("image"+v.getId()+".jpg");
	}
	
	
	public boolean hasImageData(ImageEntity v){
		Path source = getImagePath(v);
		return Files.exists(source);
	}
	
	
	public void copyData(ImageEntity v, OutputStream out) throws IOException {
		Path source = getImagePath(v);
		if(!Files.exists(source)){
			throw new FileNotFoundException("Unable to find the referenced image file for imageId:"+v.getId());
		}
		Files.copy(source, out);
	}
	
	
	public void saveData(ImageEntity v, InputStream imageData) throws IOException{
		assert(imageData != null);
		
		Path target = getImagePath(v);
		Files.copy(imageData, target, StandardCopyOption.REPLACE_EXISTING);
	}
	
}
