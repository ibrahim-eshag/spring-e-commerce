package com.shopping_cart.shopping_cart.service.image;

import com.shopping_cart.shopping_cart.dto.ImageDto;
import com.shopping_cart.shopping_cart.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    List<ImageDto> saveImages(List<MultipartFile> file, Long productId);
    Image updateImage(MultipartFile file, Long id);
    void deleteImage(Long id);
}
