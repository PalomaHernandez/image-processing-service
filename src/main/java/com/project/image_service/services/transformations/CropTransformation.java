package com.project.image_service.services.transformations;

import com.project.image_service.dtos.TransformationRequest;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class CropTransformation implements ImageTransformation{
    @Override
    public BufferedImage apply(BufferedImage image, TransformationRequest.Transformations transformations) {
        TransformationRequest.Crop crop = transformations.getCrop();
        if(crop == null) {
            return image;
        }
        Integer x = crop.getX();
        Integer y = crop.getY();
        Integer width = crop.getWidth();
        Integer height = crop.getHeight();
        if (width == null || height == null || x == null || y == null) {
            return image;
        }

        if (x < 0 || y < 0 || x + width > image.getWidth() || y + height > image.getHeight()) {
            throw new IllegalArgumentException("Crop area exceeds image bounds");
        }

        return Scalr.crop(image, x, y, width, height);
    }
}
