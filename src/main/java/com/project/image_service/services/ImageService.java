package com.project.image_service.services;

import com.project.image_service.dtos.ImageResponse;
import com.project.image_service.dtos.TransformationRequest;
import com.project.image_service.models.Image;
import com.project.image_service.models.User;
import com.project.image_service.repositories.ImageRepository;
import com.project.image_service.utils.FormatHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final CloudinaryService cloudinaryService;
    private final TransformationService transformationService;

    public Image saveImage(MultipartFile file, User user) throws IOException {
        String imageUrl = cloudinaryService.uploadImage(file);

        String uniqueFilename = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Image image = Image.builder()
                .filename(uniqueFilename)
                .originalFilename(file.getOriginalFilename())
                .url(imageUrl)
                .contentType(file.getContentType())
                .size(file.getSize())
                .uploadDate(new Date())
                .user(user)
                .build();

        return imageRepository.save(image);
    }

    public ImageResponse getImageById(Long id) {
        Image image = imageRepository.findById(id).orElseThrow(()-> new RuntimeException("Image not found"));
        return ImageResponse.from(image);
    }

    public Image findById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Image not found"));
    }

    public Image transformImage(Long id, TransformationRequest request) throws IOException, URISyntaxException {
        Image image = findById(id);
        URI uri = new URI(image.getUrl());
        BufferedImage original = ImageIO.read(uri.toURL());

        BufferedImage transformed = transformationService.transform(original, request);

        String format = FormatHelper.normalizeFormat(request.getTransformations().getFormat());
        String newFileName = "transformed_" + UUID.randomUUID() + "." + format;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(transformed, format, baos);
        byte[] imageBytes = baos.toByteArray();

        Map<String, Object> uploadResult = cloudinaryService.uploadImageBytes(imageBytes, newFileName, format);
        String url = (String) uploadResult.get("secure_url");
        long size = ((Number) uploadResult.get("bytes")).longValue();


        Image newImage = Image.builder()
                .filename(newFileName)
                .originalFilename(image.getOriginalFilename())
                .url(url)
                .contentType("image/" + format)
                .size(size)
                .uploadDate(new Date())
                .user(image.getUser())
                .build();

        return imageRepository.save(newImage);
    }
}
