package com.project.image_service.services.transformations;


import com.project.image_service.dtos.TransformationRequest;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class RotateTransformation implements ImageTransformation {
    @Override
    public BufferedImage apply(BufferedImage image, TransformationRequest.Transformations transformations) {
        Integer angle = transformations.getRotate();
        if (angle == null) {
            return image;
        }
        switch(angle) {
            case 90:
                return Scalr.rotate(image, Scalr.Rotation.CW_90);
            case 180:
                return Scalr.rotate(image, Scalr.Rotation.CW_180);
            case 270:
                return Scalr.rotate(image, Scalr.Rotation.CW_270);
            default:
                return image;
        }
    }
}
