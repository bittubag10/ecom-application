package com.app.ecom.Repository;

import com.app.ecom.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
}
