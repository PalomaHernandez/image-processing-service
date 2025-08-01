package com.project.image_service.services;

import com.project.image_service.dtos.TransformationRequest;
import com.project.image_service.services.transformations.ImageTransformation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.List;

@Service
@AllArgsConstructor
public class TransformationService {
    private final List<ImageTransformation> transformations;

    public BufferedImage transform(BufferedImage image, TransformationRequest request){
        BufferedImage result = image;
        var t = request.getTransformations();

        if(t == null) return result;

        for(ImageTransformation transformation: transformations){
            result = transformation.apply(result,t);
        }

        return result;
    }
}
