package com.agony.springai.repository;

import com.agony.springai.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author: Agony
 * @create: 2026/5/9 15:14
 * @describe:
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    // LIKE 模糊搜索，适合小数据量
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% OR p.description LIKE %:keyword%")
    List<Product> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}