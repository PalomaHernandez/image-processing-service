package com.project.image_service.services.transformations;

import com.project.image_service.dtos.TransformationRequest;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class ResizeTransformation implements ImageTransformation{

    @Override
    public BufferedImage apply(BufferedImage image, TransformationRequest.Transformations transformations) {
        TransformationRequest.Resize resize = transformations.getResize();
        if (resize == null || resize.getWidth() == null || resize.getHeight() == null) {
            return image;
        }
        return Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, resize.getWidth(), resize.getHeight());
    }
}
