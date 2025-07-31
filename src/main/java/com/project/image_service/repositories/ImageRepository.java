package com.project.image_service.repositories;

import com.project.image_service.models.Image;
import com.project.image_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByUser(User user);
}
