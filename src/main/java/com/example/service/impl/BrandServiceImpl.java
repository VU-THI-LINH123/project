package com.example.service.impl;

import java.io.IOException;
import java.util.NoSuchElementException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.example.FileUploadUtil;
import com.example.domain.Brand;
import com.example.dto.response.CustomPageImpl;
import com.example.dto.response.ResultResponse;
import com.example.exception.CustomException;
import com.example.repository.BrandRepository;

@Service
public class BrandServiceImpl {
	private static final Logger logger = LoggerFactory.getLogger(BrandServiceImpl.class);
	@Autowired
	private BrandRepository brandRepository;
	public ResultResponse listAll()
	{
		Sort sort=Sort.by("name").ascending();
		 return ResultResponse.builder().statusCode(200).messageCode("api.success").message("Success!")
				.result(brandRepository.findAll(sort))
				.build();
	}
	public  ResultResponse listByPage(int pageNum,int pageSize, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
				
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
		Page<Brand>result=null;
		
		if (keyword != null) {
			result= brandRepository.findAll(keyword, pageable);
		}
		else { 
			result= brandRepository.findAll(pageable);
		
		}
	       long startCount = (pageNum - 1) * pageSize+ 1;
		    long endCount = startCount + pageSize - 1;
			if (endCount > result.getTotalElements()) {
				endCount = result.getTotalElements();
			}
			return ResultResponse.builder().statusCode(200).messageCode("api.success").message("Success!")
					.result(new CustomPageImpl(pageNum, pageSize,result.getTotalPages(),startCount, endCount,result.getTotalElements(),result.getContent(), sort, keyword))
					.build();
	}
	public Brand get(Integer id) throws CustomException  {
		try {
			return brandRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CustomException(0, null, null);
		}
	}
	public ResultResponse save(Brand brand,MultipartFile multipartFile) throws CustomException {
		Brand savedBrand=null;
		 logger.error("Invalid JWT signature: {}");
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);
			
			 savedBrand = brandRepository.save(brand);
			String uploadDir = "../brand-logos/" + savedBrand.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			try {
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			} catch (IOException e) {
				
				 throw new CustomException(0, fileName, uploadDir);
			}
			
		} else {
			savedBrand= brandRepository.save(brand);
		}
		return  ResultResponse.builder()
				.statusCode(200)
				.messageCode("api.success")
				.message("Success!")
				.result(savedBrand)
				.build();
	}
}
