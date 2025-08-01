package com.project.image_service.services.transformations;

import com.project.image_service.dtos.TransformationRequest;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class FilterTransformation implements ImageTransformation{
    @Override
    public BufferedImage apply(BufferedImage image, TransformationRequest.Transformations transformations) {
        TransformationRequest.Filters filters = transformations.getFilters();

        if (filters == null) {
            return image;
        }

        BufferedImage result = image;

        if (filters.getGrayscale() != null && filters.getGrayscale()) {
            result = Scalr.apply(result, Scalr.OP_GRAYSCALE);
        }

        if(filters.getSepia() != null && filters.getSepia()) {
            result = applySepia(result);
        }

        return result;
    }

    private BufferedImage applySepia(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage sepia = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x, y);

                int newPixel = getNewPixel(p);
                sepia.setRGB(x, y, newPixel);
            }
        }

        return sepia;
    }

    private static int getNewPixel(int p) {
        int a = (p >> 24) & 0xff;
        int r = (p >> 16) & 0xff;
        int g = (p >> 8) & 0xff;
        int b = p & 0xff;

        int tr = (int)(0.393 * r + 0.769 * g + 0.189 * b);
        int tg = (int)(0.349 * r + 0.686 * g + 0.168 * b);
        int tb = (int)(0.272 * r + 0.534 * g + 0.131 * b);

        tr = Math.min(tr, 255);
        tg = Math.min(tg, 255);
        tb = Math.min(tb, 255);

        return (a << 24) | (tr << 16) | (tg << 8) | tb;
    }
}
