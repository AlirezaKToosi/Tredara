package com.novare.tredara.repositories;

import com.novare.tredara.models.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepo extends JpaRepository<Bid, Long> {

    List<Bid> findByItemIdOrderByAmountDesc(Long itemId);

    List<Bid> findByItemId(Long itemId);

    @Query("SELECT MAX(b.amount) FROM bid b WHERE b.item.id = :itemId")
    Double getCurrentHighestBid(Long itemId);

}
