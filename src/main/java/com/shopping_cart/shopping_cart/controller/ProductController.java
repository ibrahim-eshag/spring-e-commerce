package com.shopping_cart.shopping_cart.controller;

import com.shopping_cart.shopping_cart.dto.ProductDto;
import com.shopping_cart.shopping_cart.exceptions.ResourceNotfoundException;
import com.shopping_cart.shopping_cart.model.Product;
import com.shopping_cart.shopping_cart.request.AddProductRequest;
import com.shopping_cart.shopping_cart.request.ProductUpdateRequest;
import com.shopping_cart.shopping_cart.response.ApiResponse;
import com.shopping_cart.shopping_cart.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    final IProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("get all products success ", convertedProducts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        // Ctrl (CMD) + T    (to Surround the code with codeblocks).
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(new ApiResponse("success getting product", product));
        } catch (ResourceNotfoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("product not found", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("failed getting product: " + e.getMessage(), null));
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name) {
        try {
            List<Product> products = productService.getProductsByName(name);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            if (products.isEmpty())
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("no product found with name: " + name, null));

            return ResponseEntity.ok(new ApiResponse("success getting product by name", convertedProducts));
        } catch (ResourceNotfoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("products by name not found", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("failed getting products by name: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
        try {
            Product createdProduct = productService.addProduct(product);
            return ResponseEntity.ok(new ApiResponse("add product success", createdProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("failed adding product: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest request, @PathVariable Long id) {
        try {
            Product updatedProduct = productService.updateProduct(request, id);
            return ResponseEntity.ok(new ApiResponse("update product success", updatedProduct));
        } catch (ResourceNotfoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("product not found", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("failed updating product: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok(new ApiResponse("delete product success", null));
        } catch (ResourceNotfoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("product not found", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("failed deleting product: " + e.getMessage(), null));
        }
    }

    @GetMapping("/brand-and-name")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(@RequestParam String brandName, @RequestParam String productName) {
        try {
            List<Product> products = productService.getProductsByBrandAndName(brandName, productName);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);

            if (products.isEmpty())
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("no product found with name: " + productName + ", and brand name: " + brandName, null));
            return ResponseEntity.ok(new ApiResponse("success getting products by brand and name", convertedProducts));
        } catch (ResourceNotfoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("products by name not found", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("couldn't get products ", null));
        }
    }


    @GetMapping("/category-and-brand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrandName(@RequestParam String categoryName, @RequestParam String brandName) {
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(categoryName, brandName);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            if (products.isEmpty())
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("no product found with category: " + categoryName + ", and brand name: " + brandName, null));
            return ResponseEntity.ok(new ApiResponse("success getting products by brand and name", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("couldn't get products ", null));
        }
    }

    @GetMapping("/brand-name")
    public ResponseEntity<ApiResponse> getProductsByBrandName(@RequestParam String brandName) {
        try {
            List<Product> products = productService.getProductsByBrand(brandName);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            if (products.isEmpty())
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("no product found with brand name: " + brandName, null));
            return ResponseEntity.ok(new ApiResponse("success getting products by brand name", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("couldn't get products ", null));
        }
    }


    @GetMapping("/category-name")
    public ResponseEntity<ApiResponse> getProductsByCategoryName(@RequestParam String categoryName) {
        try {
            List<Product> products = productService.getProductsByCategory(categoryName);
            List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
            if (products.isEmpty())
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("no product found with category name: " + categoryName, null));
            return ResponseEntity.ok(new ApiResponse("get products by category name success", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("couldn't get products ", null));
        }
    }


    @GetMapping("/count-by-category-name")
    public ResponseEntity<ApiResponse> getCountProductsByBrandAndName(@RequestParam String brandName, @RequestParam String name) {
        try {
            Long productsCount = productService.countProductsByBrandAndName(brandName, name);
            return ResponseEntity.ok(new ApiResponse("success getting products Count by category name", productsCount));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("couldn't get products count ", null));
        }
    }

}
