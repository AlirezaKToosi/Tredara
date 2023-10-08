package com.novare.tredara.repositories;

import com.novare.tredara.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {
    List<Item> findByTitleContainingIgnoreCase(String title);
}
