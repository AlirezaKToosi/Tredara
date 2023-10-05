package com.novare.tredara.repositories;

import com.novare.tredara.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepo extends JpaRepository<Image, Long> {
   Optional<Image> findByitem_id(Long itemId);
}
