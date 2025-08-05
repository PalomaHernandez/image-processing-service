package com.project.image_service.controllers;

import com.project.image_service.dtos.ImageResponse;
import com.project.image_service.dtos.PaginatedImagesResponse;
import com.project.image_service.dtos.TransformationRequest;
import com.project.image_service.models.Image;
import com.project.image_service.models.User;
import com.project.image_service.services.ImageService;
import com.project.image_service.services.RateLimitService;
import com.project.image_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    private final UserService userService;
    private final RateLimitService rateLimitService;

    @PostMapping
    public ImageResponse uploadImage(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        User user = userService.loadUserByUsername(userDetails.getUsername());
        Image savedImage = imageService.saveImage(file, user);

        return ImageResponse.from(savedImage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageResponse> getImage(@PathVariable Long id) {
        return ResponseEntity.ok().body(imageService.getImageById(id));
    }

    @GetMapping
    public ResponseEntity<PaginatedImagesResponse> getImagesPaginated(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(imageService.getPaginatedImages(page, limit));
    }

    @PostMapping("/{id}/transform")
    public ResponseEntity<?> transformImage(@PathVariable Long id, @RequestBody TransformationRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.loadUserByUsername(userDetails.getUsername());
        if(!rateLimitService.tryConsume(user.getId())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded");
        }
        try {
            Image transformedImage = imageService.transformImage(id, request);
            ImageResponse response = ImageResponse.from(transformedImage);
            return ResponseEntity.ok(response);
        } catch (IOException | URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image processing failed");
        }
    }
}
