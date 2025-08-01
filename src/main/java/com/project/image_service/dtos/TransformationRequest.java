package com.project.image_service.dtos;

import lombok.Data;

@Data
public class TransformationRequest {
    private Transformations transformations;

    @Data
    public static class Transformations {
        private Resize resize;
        private Crop crop;
        private Integer rotate;
        private String format;
        private Filters filters;
    }

    @Data
    public static class Resize {
        private Integer width;
        private Integer height;
    }

    @Data
    public static class Crop {
        private Integer width;
        private Integer height;
        private Integer x;
        private Integer y;
    }

    @Data
    public static class Filters {
        private Boolean grayscale;
        private Boolean sepia;
    }
}