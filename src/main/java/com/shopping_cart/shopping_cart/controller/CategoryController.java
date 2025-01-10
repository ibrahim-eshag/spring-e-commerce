package com.shopping_cart.shopping_cart.controller;

import com.shopping_cart.shopping_cart.exceptions.AlreadyExistException;
import com.shopping_cart.shopping_cart.exceptions.ResourceNotfoundException;
import com.shopping_cart.shopping_cart.model.Category;
import com.shopping_cart.shopping_cart.response.ApiResponse;
import com.shopping_cart.shopping_cart.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllCategories(){
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("success getting all categories", categories));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("failed getting all categories: "+ e.getMessage(), null));
        }
    }


    @PostMapping
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category categoryName, Long id){
        try {
            Category category = categoryService.addCategory(categoryName);
            return ResponseEntity.ok(new ApiResponse("success adding category", category));
        } catch (AlreadyExistException e){
          return ResponseEntity.status(CONFLICT).body(new ApiResponse("category already exist: "+ e.getMessage(), null));
        }
        catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("failed adding category: "+ e.getMessage(),null));
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id){
        try {
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("success getting category", category));
        } catch (ResourceNotfoundException e){
         return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("category not found: "+ e.getMessage(), null));
        }
        catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("failed getting category: "+ e.getMessage(), null));
        }
    }

    @GetMapping("/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name){
        try {
            Category category = categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("success getting category", category));
        } catch (ResourceNotfoundException e){
         return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("category not found: "+ e.getMessage(), null));
        }
        catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("failed getting category: "+ e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody Category categoryName, Long id){
        try {
            Category category = categoryService.updateCategory(categoryName,id);
            return ResponseEntity.ok(new ApiResponse("success updating category", category));
        } catch (ResourceNotfoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("category not found: "+ e.getMessage(), null));
        }
        catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("failed updating category: "+ e.getMessage(),null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id){
        try {
             categoryService.deleteCategory(id);
            return ResponseEntity.ok(new ApiResponse("success deleted category",null));
        } catch (ResourceNotfoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("category not found: "+ e.getMessage(), null));
        }
        catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("failed getting category: "+ e.getMessage(), null));
        }
    }
}
