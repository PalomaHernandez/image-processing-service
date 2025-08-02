package com.project.image_service.dtos;

import com.project.image_service.models.Image;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ImageResponse {
    private Long id;
    private String filename;
    private String originalFilename;
    private String url;
    private String contentType;
    private long size;
    private Date uploadDate;
    private long userId;

    public static ImageResponse from(Image image) {
        return ImageResponse.builder()
                .id(image.getId())
                .filename(image.getFilename())
                .originalFilename(image.getOriginalFilename())
                .url(image.getUrl())
                .contentType(image.getContentType())
                .size(image.getSize())
                .uploadDate(image.getUploadDate())
                .userId(image.getUser().getId())
                .build();
    }
}
