package com.example.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.domain.Product;
@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {
	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1% " 
			+ "OR p.shortDescription LIKE %?1% "
			+ "OR p.fullDescription LIKE %?1% "
			+ "OR p.brand.name LIKE %?1% "
			+ "OR p.category.name LIKE %?1%")
	public Page<Product> findAll(String keyword, Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.category.id = ?1 ")	
	public Page<Product> findAllInCategory(Integer categoryId,Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE (p.category.id = ?1) AND "
			+ "(p.name LIKE %?2% " 
			+ "OR p.shortDescription LIKE %?2% "
			+ "OR p.fullDescription LIKE %?2% "
			+ "OR p.brand.name LIKE %?2% "
			+ "OR p.category.name LIKE %?2%)")			
	public Page<Product> searchInCategory(Integer categoryId,String keyword, Pageable pageable);
}
