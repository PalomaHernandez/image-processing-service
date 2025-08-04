package com.project.image_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginatedImagesResponse {
    private List<ImageResponse> images;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
