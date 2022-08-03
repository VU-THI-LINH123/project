package com.example.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.domain.Category;
import com.example.domain.User;

import com.example.dto.response.ResultResponse;
import com.example.exception.CustomException;
import com.example.export.CategoryCsvExporter;
import com.example.repository.CategoryRepository;
import com.example.service.impl.CategoryServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.bytebuddy.asm.Advice.Return;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	private CategoryServiceImpl categoryServiceImpl;
	@Autowired
	CategoryRepository categoryRepository;

	@PostMapping("")
	public ResultResponse saveCategory(@RequestParam("category") String category,
			@RequestParam("fileImage") MultipartFile multipartFile) throws IOException, CustomException {
		ObjectMapper mapper = new ObjectMapper();
		Category category2 = null;
		try {
			category2 = mapper.readValue(category, Category.class);
		} catch (JsonProcessingException e1) {
			throw new CustomException(ResultResponse.STATUS_CODE_BAD_REQUEST, ResultResponse.MESSEGE_CODE_NULL_TIENGHI,
					null);
		}
		return categoryServiceImpl.save(multipartFile, category2);
	}

	@GetMapping("/page/{pageNum}")
	public ResultResponse listByPage(@PathVariable(name = "pageNum") int pageNum,
			@RequestParam("pageSize") int pageSize, @RequestParam("sortDir") String sortDir,
			@RequestParam("keyword") String keyword) {
		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}

		return categoryServiceImpl.listByPage(pageNum, pageSize, sortDir, keyword);
	}

	@PutMapping("/{id}/enabled/{status}")
	public ResultResponse updateCategoryEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled) {
		categoryServiceImpl.updateCategoryEnabledStatus(id, enabled);

		return categoryServiceImpl.updateCategoryEnabledStatus(id, enabled);
	}

	@DeleteMapping("/categories/delete/{id}")
	public ResultResponse deleteCategory(@PathVariable(name = "id") Integer id) throws CustomException {

		return categoryServiceImpl.delete(id);
	}

	@GetMapping("/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<Category> listCategories = categoryServiceImpl.listCategoriesUsedInForm();
		CategoryCsvExporter exporter = new CategoryCsvExporter();
		exporter.export(listCategories, response);
	}
}
