package com.shopping_cart.shopping_cart.controller;

import com.shopping_cart.shopping_cart.dto.ImageDto;
import com.shopping_cart.shopping_cart.exceptions.ResourceNotfoundException;
import com.shopping_cart.shopping_cart.model.Image;
import com.shopping_cart.shopping_cart.response.ApiResponse;
import com.shopping_cart.shopping_cart.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private  final IImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadImages(
            @RequestBody List<MultipartFile> files,
            @RequestParam Long productId) {
        try {
            System.out.println("files: "+files+" productId: "+productId);
            List<ImageDto> imageDtos = imageService.saveImages(files,productId);
            return ResponseEntity.ok(new ApiResponse("upload success", imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("upload failed! ", e.getMessage()));
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> getImage(@PathVariable Long id) throws SQLException {
        Image image = imageService.getImageById(id);
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
        return  ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +image.getFileName() + "\"")
                .body(resource);
    }

    @PutMapping("/images/{imageId}")
    public ResponseEntity<ApiResponse> updateImage(@RequestBody MultipartFile file, @PathVariable Long imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            if(image != null){
                image = imageService.updateImage(file, imageId);
                return ResponseEntity.ok(new ApiResponse("image updated success!", image));
            }
        } catch (ResourceNotfoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("image upload failed! ", e.getMessage()));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("imageupload failed! ", INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }

    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<ApiResponse> deleteImage(@RequestParam Long imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            if(image != null){
                imageService.deleteImage(imageId);
                return ResponseEntity.ok(new ApiResponse("image deleted success!", image));
            }
        } catch (ResourceNotfoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("image delete failed! ", e.getMessage()));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("image delete failed! ", INTERNAL_SERVER_ERROR.getReasonPhrase()));
    }

}
