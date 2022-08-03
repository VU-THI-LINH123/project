package com.example.dto.response;

import java.util.List;

import com.example.domain.Brand;
import com.example.domain.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BrandSearchResponse {
	private int currentPage;
	private int pageSize;
	private long totalPages;
	private long startCount;
	private long endCount;
	private long totalItems;
	private List<Brand> listBrand;
	private String sortField;
	private String sortDir;
	private String keyword;
}
