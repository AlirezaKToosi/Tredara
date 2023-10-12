package com.novare.tredara.repositories;

import com.novare.tredara.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BidRepoTest {

    @Autowired
    BidRepo bidRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ItemRepo itemRepo;

    Bid savedBid;
    Item savedItem;
    User savedUser;


    @BeforeEach
    void setUp(){
        User user = new User("rohit agarwal","aggarrohit@gmail.com","password");
        savedUser = userRepo.save(user);

        Item item = new Item();
        item.setTitle("item title");
        item.setDescription("item description");
        item.setStartPrice(100);
        item.setStartDateTime(new Date());
        item.setEndDateTime(new Date());
        item.setStatus(EItemStatus.STATUS_OPEN);
        item.setUser(user);

        savedItem = itemRepo.save(item);

        Bid bid = new Bid(null,1111,new Date(),user,item,null);
        savedBid = bidRepo.save(bid);
    }

    @Test
    void findByItemId() {
        assertThat(bidRepo.findByItemIdOrderByAmountDesc(savedItem.getId())).isNotEmpty();
        assertThat(bidRepo.findByItemIdOrderByAmountDesc(savedItem.getId()).get(0).getBidTime()).isEqualTo(savedBid.getBidTime());
        assertThat(bidRepo.findByItemIdOrderByAmountDesc(savedItem.getId())).hasSize(1);
        assertThat(bidRepo.findByItemIdOrderByAmountDesc(savedItem.getId())).hasOnlyElementsOfType(Bid.class);
    }

    @Test
    void findByItemId_withMultipleBidsForOneItem() {
        Bid bid = new Bid(null,1111,new Date(),savedUser,savedItem,null);
        bidRepo.save(bid);

        assertThat(bidRepo.findByItemIdOrderByAmountDesc(savedItem.getId())).isNotEmpty();
        assertThat(bidRepo.findByItemIdOrderByAmountDesc(savedItem.getId()).get(0).getBidTime()).isEqualTo(savedBid.getBidTime());
        assertThat(bidRepo.findByItemIdOrderByAmountDesc(savedItem.getId())).hasSize(2);
        assertThat(bidRepo.findByItemIdOrderByAmountDesc(savedItem.getId())).hasOnlyElementsOfType(Bid.class);
    }


    @Test
    void findById() {
        assertThat(bidRepo.findById(savedBid.getId())).isNotEmpty();
    }

    @Test
    void findById_notFound() {
        assertThat(bidRepo.findById(200L)).isEmpty();
    }

    @Test
    void getCurrentHighestBid(){
        assertThat(bidRepo.getCurrentHighestBid(savedItem.getId())).isEqualTo(1111);
    }

    @Test
    void getCurrentHighestBid_whenNoBidForItem(){
        assertThat(bidRepo.getCurrentHighestBid(100000L)).isNull();
    }
}