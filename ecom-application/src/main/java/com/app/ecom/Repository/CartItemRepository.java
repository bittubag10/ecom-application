package com.app.ecom.Repository;

import com.app.ecom.Model.CartItem;
import com.app.ecom.Model.Product;
import com.app.ecom.Model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    CartItem findByUserAndProduct(User user, Product product);

    @Transactional
    void deleteByUserAndProduct(User user, Product product);


    List<CartItem>findByUser(User user);

    void deleteByUser(User user);
}
