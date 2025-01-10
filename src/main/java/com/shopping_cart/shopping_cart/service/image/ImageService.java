package com.shopping_cart.shopping_cart.service.image;

import com.shopping_cart.shopping_cart.dto.ImageDto;
import com.shopping_cart.shopping_cart.model.Image;
import com.shopping_cart.shopping_cart.model.Product;
import com.shopping_cart.shopping_cart.repository.ImageRepository;
import com.shopping_cart.shopping_cart.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private  final ImageRepository imageRepository;
    private final ProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("image with id: "+id+" is not found."));
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List< ImageDto> savedImageDto = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl = "/api/v1/images/download/";
                String downloadUrl = buildDownloadUrl+image.getId();
                image.setUrl(downloadUrl);
                Image savedImage = imageRepository.save(image);

                savedImage.setUrl(buildDownloadUrl+savedImage.getId());
                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setUrl(savedImage.getUrl());
                savedImageDto.add(imageDto);

            } catch (IOException | SQLException e){
                throw new RuntimeException(e);
            }
        }
        return savedImageDto;
    }

    @Override
    public Image updateImage(MultipartFile file, Long id) {
            Image image = getImageById(id);
        try {
            image.setUrl(file.getOriginalFilename());
            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);

            return image;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteImage(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete,()->{
            throw new IllegalArgumentException("image with id: "+id+" is not found.");
        });
    }
}
