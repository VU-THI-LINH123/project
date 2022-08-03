package com.example.dto.response;

import java.util.List;

import com.example.domain.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserSearchResponse {
	private int currentPage;
	private int pageSize;
	private long totalPages;
	private long startCount;
	private long endCount;
	private long totalItems;
	private List<User> listUsers;
	private String sortField;
	private String sortDir;
	private String keyword;

}