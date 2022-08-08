package com.example.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.FileUploadUtil;
import com.example.domain.Product;
import com.example.dto.response.ResultResponse;
import com.example.service.impl.ProductServiceImpl;

@RestController
@RequestMapping("/products")
public class ProductController {
	@Autowired
	private ProductServiceImpl productServiceImpl;
	@GetMapping("/page/{pageNum}")
	public ResultResponse listByPage(
			@PathVariable(name = "pageNum") int pageNum,
			@RequestParam("pageSize")int pageSize,
			@RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir,
			@RequestParam("keyword") String keyword,
			@RequestParam("categoryId") Integer categoryId
			) {
		return  productServiceImpl.listByPage(pageNum,pageSize, sortField, sortDir, keyword, categoryId);		
	}
	@PostMapping("/")
	public ResultResponse saveProduct(Product product,
			@RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
		
		return productServiceImpl.save(product, multipartFile);
	}
	
}
