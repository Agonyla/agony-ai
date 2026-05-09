package com.agony.springai.repository;

import com.agony.springai.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: Agony
 * @create: 2026/5/9 15:23
 * @describe:
 */
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}