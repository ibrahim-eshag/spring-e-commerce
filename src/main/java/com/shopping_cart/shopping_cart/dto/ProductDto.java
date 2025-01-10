package com.shopping_cart.shopping_cart.dto;

import com.shopping_cart.shopping_cart.model.Category;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private String categoryName; // Only include category name (no lazy proxy)
    private List<ImageDto> images;
}
