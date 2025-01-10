package com.shopping_cart.shopping_cart.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String brand;


    private BigDecimal price;
    private Integer inventory; // save as quantity

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    @JsonBackReference // Marks this as the "child" side of the relationship
    private Category category;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    // remove images that are not referenced by  products
    private List<Image> images;

    public Product(String name, String brand, BigDecimal price, Integer inventory, String description, Category category) {
    this.name = name;
    this.brand = brand;
    this.price = price;
    this.inventory = inventory;
    this.description = description;
    this.category = category;
    }
}
