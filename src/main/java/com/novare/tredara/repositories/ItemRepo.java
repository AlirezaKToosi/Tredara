package com.novare.tredara.repositories;

import com.novare.tredara.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long> , JpaSpecificationExecutor<Item> {
    @Query("SELECT i FROM Item i WHERE LOWER(i.title) LIKE %:query% OR LOWER(i.description) LIKE %:query%")
    List<Item> searchByTitleOrDescriptionIgnoreCase(@Param("query") String query);

    @Query("SELECT i FROM Item i WHERE i.status = 0 AND i.endDateTime < CURRENT_TIMESTAMP")
    List<Item> serchByEndDateTimeAndOpenStatus();

    @Query("SELECT i FROM Item i WHERE i.user.id = :userId")
    List<Item> findByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT i FROM Item i JOIN i.bids b WHERE b.user.id = :userId")
    List<Item> findItemsByBidsUserId(@Param("userId") Long userId);

}
