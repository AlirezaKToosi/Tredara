package com.novare.tredara.services;

import com.novare.tredara.controllers.ItemController;
import com.novare.tredara.models.Item;
import com.novare.tredara.payloads.ItemInfoDTO;
import com.novare.tredara.repositories.ItemRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ItemServiceTest {
    @Mock
    ItemRepo itemRepo;
    @Mock
    BidService bidService;

    ItemService itemService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        itemService = new ItemService(bidService, itemRepo, null, null, null);
    }

    @Test
    void getItemInfo() throws SQLException {
        //Arrange
        //Add Dummy Data for Item
        Long itemId = 123L;
        Item item = new Item();
        item.setImage_url("image_url");
        item.setTitle("Item Title");
        item.setStartPrice(100.0);
        item.setDescription("Item Description");
        item.setEndDateTime(new Date());
        item.setBids(Collections.emptyList());

        when(itemRepo.findById(itemId)).thenReturn(Optional.of(item));
        when(bidService.getBidsByItemId(itemId)).thenReturn(Collections.emptyList());

        ItemInfoDTO itemInfoDTO=itemService.getItemInfo(itemId);

        assertNotNull(itemInfoDTO);
        assertEquals("image_url", itemInfoDTO.getImageUrl());
        assertEquals("Item Title", itemInfoDTO.getTitle());
        assertEquals(100, itemInfoDTO.getStartPrice());
        assertEquals("Item Description", itemInfoDTO.getDescription());
        assertEquals("no bid yet", itemInfoDTO.getLeadPrice());
        assertEquals("Bidding has ended", itemInfoDTO.getTimeToBidEnd());
        assertEquals(0, itemInfoDTO.getNumberOfBids());

    }
}