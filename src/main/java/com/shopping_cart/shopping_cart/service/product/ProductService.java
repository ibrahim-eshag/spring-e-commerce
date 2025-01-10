package com.shopping_cart.shopping_cart.service.product;

import com.shopping_cart.shopping_cart.dto.ImageDto;
import com.shopping_cart.shopping_cart.dto.ProductDto;
import com.shopping_cart.shopping_cart.exceptions.ProductNotFoundException;
import com.shopping_cart.shopping_cart.exceptions.ResourceNotfoundException;
import com.shopping_cart.shopping_cart.model.Category;
import com.shopping_cart.shopping_cart.model.Image;
import com.shopping_cart.shopping_cart.model.Product;
import com.shopping_cart.shopping_cart.repository.CategoryRepository;
import com.shopping_cart.shopping_cart.repository.ImageRepository;
import com.shopping_cart.shopping_cart.repository.ProductRepository;
import com.shopping_cart.shopping_cart.request.AddProductRequest;
import com.shopping_cart.shopping_cart.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // for constructor injection , only final properties are injected
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;
    @Override
    public Product addProduct(AddProductRequest request) {
        // check if the category is found in the DB
        // if Yes, set is as the new product category
        // if no save it as a new category
        // se it as the product category
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()->{
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });

         request.setCategory(category);
         return productRepository.save(createProduct(request, category));
    }

    private boolean productExists(String name , String brand) {
        return productRepository.existsByNameAndBrand(name, brand);
    }

private Product createProduct(AddProductRequest request, Category category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
               category
        );
}

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(()->new ResourceNotfoundException("product with id: "+id + " is not found."));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete, ()-> {
                    throw new ResourceNotfoundException("product with id: "+id + " is not found.");
                });
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long id) {
        return productRepository.findById(id)
                .map(existingProduct->updateExistingProduct(existingProduct, request))
                .map(productRepository::save) // ::save e is called a method reference.
                .orElseThrow(()->new ProductNotFoundException("product with id: "+id + " is not found."));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category existingCategory = categoryRepository.findByName(request.getCategory().getName());
        if(existingCategory == null){
            existingCategory = categoryRepository.save(request.getCategory());
        }
        existingProduct.setCategory(existingCategory);
        return existingProduct;
    }


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand,name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
//        return (long) productRepository.findByBrandAndName(brand, name).size();
        return  productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }


}
