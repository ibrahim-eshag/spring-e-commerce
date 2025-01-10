package com.shopping_cart.shopping_cart.repository;

import com.shopping_cart.shopping_cart.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository  extends JpaRepository<Image,Long> {
    List<Image> findByProductId(Long id);

}
