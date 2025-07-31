package com.project.image_service.controllers;

import com.project.image_service.models.Image;
import com.project.image_service.models.User;
import com.project.image_service.services.ImageService;
import com.project.image_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        User user = userService.loadUserByUsername(userDetails.getUsername());
        Image savedImage = imageService.saveImage(file, user);
        String imageUrl = "/uploads/" + savedImage.getFilename();

        var response = Map.of(
                "id", savedImage.getId(),
                "originalFilename", savedImage.getOriginalFilename(),
                "contentType", savedImage.getContentType(),
                "size", savedImage.getSize(),
                "uploadDate", savedImage.getUploadDate(),
                "url", imageUrl
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getImage(@PathVariable Long id) throws IOException {
        Resource resource = imageService.getImageById(id);

        Image image = imageService.findById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getOriginalFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, image.getContentType())
                .body(resource);
    }

}
