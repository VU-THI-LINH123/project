package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;
import org.apache.log4j.spi.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.example.jwt.AuthEntryPointJwt;

public class FileUploadUtil {
	public static void saveFile(String uploadDir, String fileName, 
			MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(uploadDir);
		
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ex) {
			throw new IOException("Could not save file: " + fileName, ex);
		}
		
	}
	
	public static void cleanDir(String dir) {
		Path dirPath = Paths.get(dir);
		System.out.println(dirPath);
		Stream<Path>liStream=null;
		try {
			Files.list(dirPath).forEach(file -> {
				System.out.println(file);
				if (!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					} catch (IOException ex) {
						System.out.println("Could not delete file: " + file);
					}
				}
			});
		} catch (IOException ex) {
			System.out.println("Could not list directory: " + dirPath);
		}
	}

	public static void removeDir(String dir) {
		cleanDir(dir);
		
		try {
			Files.delete(Paths.get(dir));
		} catch (IOException e) {
			
		}
		
	}
}
