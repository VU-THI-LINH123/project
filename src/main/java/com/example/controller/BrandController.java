package com.example.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.domain.Brand;
import com.example.domain.Category;
import com.example.dto.response.CategoryDTO;
import com.example.dto.response.ResultResponse;
import com.example.exception.CustomException;
import com.example.service.impl.BrandServiceImpl;

@RestController
@RequestMapping("/brands")
public class BrandController {
	@Autowired
	private BrandServiceImpl brandServiceImpl;
	@GetMapping("/page/{pageNum}")
	public ResultResponse listByPage(@PathVariable(name = "pageNum") int pageNum,
			@RequestParam("pageSize") int pageSize, @RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir, @RequestParam("keyword") String keyword) {
		return brandServiceImpl.listByPage(pageNum, pageSize, sortField, sortDir, keyword);
	}
	@PostMapping("/save")
	public ResultResponse saveBrand(Brand brand, @RequestParam("fileImage") MultipartFile multipartFile) throws  CustomException {
		
		return brandServiceImpl.save(brand, multipartFile);	
	}
	@GetMapping("/{id}/categories")
	public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name = "id") Integer brandId) throws CustomException{
		List<CategoryDTO> listCategories = new ArrayList<>(); 
			Brand brand = brandServiceImpl.get(brandId);
			Set<Category> categories = brand.getCategories();
			
			for (Category category : categories) {
				CategoryDTO dto = new CategoryDTO(category.getId(), category.getName());
				listCategories.add(dto);
			}
			
			return listCategories;
	}
}
