package com.project.image_service.services.transformations;

import com.project.image_service.dtos.TransformationRequest;

import java.awt.image.BufferedImage;

public interface ImageTransformation {
    BufferedImage apply(BufferedImage image, TransformationRequest.Transformations transformations);
}
