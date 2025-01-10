package com.shopping_cart.shopping_cart.request;

import com.shopping_cart.shopping_cart.model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequest {
    private String name;
    private String description;
    private String brand;
    private BigDecimal price;
    private Integer inventory; // save as quantity
    private Category category;

}
