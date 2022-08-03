package com.example.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.FileUploadUtil;
import com.example.domain.Role;
import com.example.domain.User;
import com.example.dto.response.ResultResponse;
import com.example.dto.response.UserSearchResponse;
import com.example.exception.CustomException;
import com.example.exception.CustomExceptionHandler;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
@Service
@Transactional
public class UserServiceImpl {

	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public User getByEmail(String email) {
		return userRepo.getUserByEmail(email);
	}
	
	public List<User> listAll() {
		return (List<User>) userRepo.findAll(Sort.by("firstName").ascending());
	}
	public ResultResponse listByPage(int pageNum,int pageSize, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
				
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
		Page<User>result=null;
		
		if (keyword != null) {
			result= userRepo.findAll(keyword, pageable);
		}
		else { 
			result= userRepo.findAll(pageable);
		
		}
	       long startCount = (pageNum - 1) * pageSize+ 1;
		    long endCount = startCount + pageSize - 1;
			if (endCount > result.getTotalElements()) {
				endCount = result.getTotalElements();
			}
	       return ResultResponse.builder()
					.statusCode(200)
					.messageCode("api.success")
					.message("Success!")
					.result(new UserSearchResponse(pageNum, pageSize,result.getTotalPages(),startCount,endCount,result.getTotalElements(),result.getContent(), sortField, sortDir, keyword))
					.build();
	}
	public ResultResponse save( MultipartFile multipartFile, User user) throws CustomException
	{
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = save(user);
			
			String uploadDir = "user-photos/" + savedUser.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			try {
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			} catch (IOException e) {
				 throw new CustomException(0, fileName, uploadDir);
			}
			
		} else {
			if (user.getPhotos().isEmpty()) user.setPhotos(null);
			save(user);
		}
		return ResultResponse.builder()
				.statusCode(200)
				.messageCode("api.success")
				.message("Success!")
				.result(user)
				.build();
	}
	

	public User save( User user) {
		boolean isUpdatingUser = (user.getId() != null);
		
		if (isUpdatingUser) {
			User existingUser = userRepo.findById(user.getId()).get();
			
			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
			
		} else {		
			encodePassword(user);
		}
		
		return userRepo.save(user);
		
	}
	
	public User updateAccount(User userInForm) {
		User userInDB = userRepo.findById(userInForm.getId()).get();
		
		if (!userInForm.getPassword().isEmpty()) {
			userInDB.setPassword(userInForm.getPassword());
			encodePassword(userInDB);
		}
		
		if (userInForm.getPhotos() != null) {
			userInDB.setPhotos(userInForm.getPhotos());
		}
		
		userInDB.setFirstName(userInForm.getFirstName());
		userInDB.setLastName(userInForm.getLastName());
		
		return userRepo.save(userInDB);
	}
	
	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}
	
	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = userRepo.getUserByEmail(email);
		
		if (userByEmail == null) return true;
		
		boolean isCreatingNew = (id == null);
		
		if (isCreatingNew) {
			if (userByEmail != null) return false;
		} else {
			if (userByEmail.getId() != id) {
				return false;
			}
		}
		
		return true;
	}
	public ResultResponse delete(Integer id) throws CustomException {
		userRepo.deleteById(id);
		return ResultResponse.builder()
				.statusCode(200)
				.messageCode("api.success")
				.message("Success!")
				.result(null)
				.build();
	}
	
	public ResultResponse updateUserEnabledStatus(Integer id, boolean enabled) {
	        userRepo.updateEnabledStatus(id, enabled);
		return ResultResponse.builder()
				.statusCode(200)
				.messageCode("api.success")
				.message("Success!")
				.result(null)
				.build();
	}
}
