package com.ssafy.smartstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ssafy.smartstore.dto.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product , Integer> {

	@Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<Product> searchByKeyword(@Param("keyword") String keyword);

    // 카테고리로 검색하는 쿼리
    List<Product> findByCategoryIgnoreCase(String category);
}