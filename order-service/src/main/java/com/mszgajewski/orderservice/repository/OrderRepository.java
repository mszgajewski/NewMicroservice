package com.mszgajewski.orderservice.repository;

import com.mszgajewski.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}