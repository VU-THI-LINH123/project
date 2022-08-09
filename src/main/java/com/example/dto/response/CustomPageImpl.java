package com.example.dto.response;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomPageImpl<T>   {
    private int number;
    private int size;
    private int totalPages;
    private long startCount;
    private long endCount;
    private long totalElements;
    private List<T> content;
    private String sortDir;
    private String sortField;
    private String keyword;
 
}