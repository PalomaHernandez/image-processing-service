package com.project.image_service.services;

import com.project.image_service.models.Image;
import com.project.image_service.models.User;
import com.project.image_service.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    private final String uploadDir = "C:/Users/paloh/Pictures";

    public Image saveImage(MultipartFile file, User user) throws IOException {
        File uploadFolder = new File(uploadDir);
        if(!uploadFolder.exists()){
            uploadFolder.mkdirs();
        }

        String uniqueFilename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File destinationFile = new File(uploadFolder, uniqueFilename);
        file.transferTo(destinationFile);

        Image image = Image.builder()
                .filename(uniqueFilename)
                .originalFilename(file.getOriginalFilename())
                .contentType(file.getContentType())
                .size(file.getSize())
                .uploadDate(new Date())
                .user(user)
                .build();

        return imageRepository.save(image);
    }

    public Resource getImageById(Long id) throws MalformedURLException {
        Image image = imageRepository.findById(id).orElseThrow(()-> new RuntimeException("Image not found"));
        Path filePath = Paths.get(uploadDir).resolve(image.getFilename()).normalize();
        return new UrlResource(filePath.toUri());
    }

    public Image findById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Image not found"));
    }
}
