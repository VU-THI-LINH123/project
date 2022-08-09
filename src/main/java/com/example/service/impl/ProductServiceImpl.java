package com.example.service.impl;

import java.io.IOException;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.example.FileUploadUtil;
import com.example.domain.Product;
import com.example.domain.User;
import com.example.dto.response.CustomPageImpl;
import com.example.dto.response.ResultResponse;
import com.example.repository.ProductRepository;

@Service
@Transactional
public class ProductServiceImpl {
	@Autowired
	private ProductRepository repo;

	public ResultResponse listByPage(int pageNum, int pageSize, String sortField, String sortDir, String keyword,
			Integer categoryId) {
		Sort sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

		if (keyword != null && !keyword.isEmpty()) {
			if (categoryId != null && categoryId > 0) {
				Page<Product> pageProducts = repo.searchInCategory(categoryId, keyword, pageable);
				long startCount = (pageNum - 1) * pageSize + 1;
				long endCount = startCount + pageSize - 1;
				if (endCount > pageProducts.getTotalElements()) {
					endCount = pageProducts.getTotalElements();
				}
				return ResultResponse.builder().statusCode(200).messageCode("api.success").message("Success!")
						.result(new CustomPageImpl<Product>(pageNum, pageSize,pageProducts.getTotalPages(),startCount, endCount,pageProducts.getTotalElements(),pageProducts.getContent(), sortDir,sortField, keyword))
						.build();
			}
			Page<Product> pageProducts = repo.findAll(keyword, pageable);
			long startCount = (pageNum - 1) * pageSize + 1;
			long endCount = startCount + pageSize - 1;
			if (endCount > pageProducts.getTotalElements()) {
				endCount = pageProducts.getTotalElements();
			}
			return ResultResponse.builder().statusCode(200).messageCode("api.success").message("Success!")
					.result(new CustomPageImpl<Product>(pageNum, pageSize,pageProducts.getTotalPages(),startCount, endCount,pageProducts.getTotalElements(),pageProducts.getContent(), sortDir,sortField, keyword))
					.build();
		}

		if (categoryId != null && categoryId > 0) {
			Page<Product> pageProducts = repo.findAllInCategory(categoryId, pageable);
			long startCount = (pageNum - 1) * pageSize + 1;
			long endCount = startCount + pageSize - 1;
			if (endCount > pageProducts.getTotalElements()) {
				endCount = pageProducts.getTotalElements();
			}
			return ResultResponse.builder().statusCode(200).messageCode("api.success").message("Success!")
					.result(new CustomPageImpl<Product>(pageNum, pageSize,pageProducts.getTotalPages(),startCount, endCount,pageProducts.getTotalElements(),pageProducts.getContent(), sortDir,sortField, keyword))
					.build();
		}
		Page<Product> pageProducts = repo.findAll(pageable);
		long startCount = (pageNum - 1) * pageSize + 1;
		long endCount = startCount + pageSize - 1;
		if (endCount > pageProducts.getTotalElements()) {
			endCount = pageProducts.getTotalElements();
		}
		return ResultResponse.builder().statusCode(200).messageCode("api.success").message("Success!")
				.result(new CustomPageImpl<Product>(pageNum, pageSize,pageProducts.getTotalPages(),startCount, endCount,pageProducts.getTotalElements(),pageProducts.getContent(),sortDir,sortField, keyword))
				.build();
	}
	public ResultResponse save(Product product,MultipartFile multipartFile) {
		Product savedProduct=null;
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			product.setMainImage(fileName);
			
			savedProduct= save(product);
			String uploadDir = "../product-images/" + savedProduct.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			try {
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			} catch (IOException e) {
				
			}
			
		} else {
			savedProduct =save(product);
		}
		return ResultResponse.builder().statusCode(200).messageCode("api.success").message("Success!")
				.result(savedProduct)
				.build();
			}
	public Product save(Product product) {
		if (product.getId() == null) {
			product.setCreatedTime(new Date());
		}
		
		if (product.getAlias() == null || product.getAlias().isEmpty()) {
			String defaultAlias = product.getName().replaceAll(" ", "-");
			product.setAlias(defaultAlias);
		} else {
			product.setAlias(product.getAlias().replaceAll(" ", "-"));
		}
		
		product.setUpdatedTime(new Date());
		
		return repo.save(product);
	}
}
