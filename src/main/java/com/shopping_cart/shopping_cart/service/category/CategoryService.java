package com.shopping_cart.shopping_cart.service.category;

import com.shopping_cart.shopping_cart.exceptions.AlreadyExistException;
import com.shopping_cart.shopping_cart.exceptions.ResourceNotfoundException;
import com.shopping_cart.shopping_cart.model.Category;
import com.shopping_cart.shopping_cart.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private  final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(()->new ResourceNotfoundException("category with id: "+id+" is not found."));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category)
                .filter(c->!categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save)
                .orElseThrow(()->new AlreadyExistException(category.getName()+" already exist."));
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id))
                .map(oldCategory->{
                    oldCategory.setName(category.getName());
                    return categoryRepository.save(oldCategory);
                })
                .orElseThrow(()->new ResourceNotfoundException("category with id: "+id+" is not found."));
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete,()->{
            throw new ResourceNotfoundException("category with id: "+id+" is not found.");
        });
    }
}
