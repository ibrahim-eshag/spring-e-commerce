package com.shopping_cart.shopping_cart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String name;
    //    private String description;
    //    private Image image;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference // Marks this as the "parent" side of the relationship
    @JsonIgnore
    private List<Product> products;

    public Category(String name) {
        this.name = name;
    }
}
