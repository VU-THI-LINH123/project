package com.example.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
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
import com.example.domain.Category;
import com.example.dto.response.CustomPageImpl;
import com.example.dto.response.ResultResponse;
import com.example.exception.CustomException;
import com.example.repository.CategoryRepository;

@Service
@Transactional
public class CategoryServiceImpl {
	@Autowired
	private CategoryRepository categoryRepository;

	public ResultResponse save(MultipartFile multipartFile, Category category) {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);

			Category savedCategory = categoryRepository.save(category);
			String uploadDir = "../category-images/" + savedCategory.getId();

			FileUploadUtil.cleanDir(uploadDir);
			try {
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			categoryRepository.save(category);
		}
		return ResultResponse.builder().statusCode(200).messageCode("api.success").message("Success!").result(category)
				.build();

	}

	public ResultResponse listByPage(int pageNum, int pageSize, String sortDir, String keyword) {
		Sort sort = Sort.by("name");
      sort=sortDir.equals("asc")?sort.ascending():sort.descending();
      sortDir=  sortDir.equals("asc")?"asc":"desc";
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

		Page<Category> pageCategories = null;

		if (keyword != null && keyword.trim().isEmpty() == false) {
			pageCategories = categoryRepository.search(keyword, pageable);
		} else {
			pageCategories = categoryRepository.findRootCategories(pageable);
		}

		List<Category> rootCategories = pageCategories.getContent();
		long startCount = (pageNum - 1) * pageSize + 1;
		long endCount = startCount + pageSize - 1;
		if (endCount > pageCategories.getTotalElements()) {
			endCount = pageCategories.getTotalElements();
		}

		if (keyword != null && !keyword.isEmpty()) {
			List<Category> searchResult = pageCategories.getContent();
			List<Category> categories = new ArrayList<Category>();
			for (Category category : searchResult) {
				category.setHasChildren(category.getChildren().size() > 0);
			     	categories.add(category.copyFull(category));
			}
			return ResultResponse.builder().statusCode(200).messageCode("api.success").message("Success!").result(new CustomPageImpl<Category>(pageNum, pageSize, pageCategories.getTotalPages(), startCount,
							endCount, pageCategories.getTotalElements(),categories,sortDir,"name", keyword)).build();
				

		} else {
			return ResultResponse.builder().statusCode(200).messageCode("api.success").message("Success!").result(new CustomPageImpl<Category>(pageNum, pageSize, pageCategories.getTotalPages(), startCount,
							endCount, pageCategories.getTotalElements(),listHierarchicalCategories(rootCategories, sortDir),sortDir,"name", keyword)).build();
		}
	}

	private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
		List<Category> hierarchicalCategories = new ArrayList<>();

		for (Category rootCategory : rootCategories) {
			hierarchicalCategories.add(Category.copyFull(rootCategory));

			Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);

			for (Category subCategory : children) {
				String name = "--" + subCategory.getName();
				hierarchicalCategories.add(Category.copyFull(subCategory, name));

				listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1, sortDir);
			}
		}

		return hierarchicalCategories;
	}

	public List<Category> listCategoriesUsedInForm() {
		List<Category> categoriesUsedInForm = new ArrayList<>();

		Iterable<Category> categoriesInDB = categoryRepository.findRootCategories(Sort.by("name").ascending());

		for (Category category : categoriesInDB) {
			categoriesUsedInForm.add(Category.copyIdAndName(category));

			Set<Category> children = sortSubCategories(category.getChildren(), "asc");

			for (Category subCategory : children) {
				String name = "--" + subCategory.getName();
				categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

				listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
			}
		}

		return categoriesUsedInForm;
	}

	private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
		SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
			@Override
			public int compare(Category cat1, Category cat2) {
				if (sortDir.equals("asc")) {
					return cat1.getName().compareTo(cat2.getName());
				} else {
					return cat2.getName().compareTo(cat1.getName());
				}
			}
		});

		sortedChildren.addAll(children);

		return sortedChildren;
	}

	private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel,
			String sortDir) {
		Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);
		int newSubLevel = subLevel + 1;

		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();

			hierarchicalCategories.add(Category.copyFull(subCategory, name));

			listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel, sortDir);
		}

	}

	private void listSubCategoriesUsedInForm(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = sortSubCategories(parent.getChildren(), "asc");

		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();

			categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

			listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, newSubLevel);
		}
	}

	public ResultResponse delete(Integer id) throws CustomException {
		Long countById = categoryRepository.countById(id);
		if (countById == null || countById == 0) {
			throw new CustomException(ResultResponse.STATUS_CODE_NOT_FOUND, "Could not find any category with ID",
					null);
		}
		try {
			categoryRepository.deleteById(id);
			String categoryDir = "../category-images/" + id;
			FileUploadUtil.removeDir(categoryDir);
		} catch (Exception ex) {

		}

		return ResultResponse.builder().statusCode(200).messageCode("api.success").message("Success!").result(null)
				.build();
	}

}
