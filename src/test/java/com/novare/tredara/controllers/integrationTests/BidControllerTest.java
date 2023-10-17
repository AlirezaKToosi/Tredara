package com.novare.tredara.controllers.integrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novare.tredara.models.EItemStatus;
import com.novare.tredara.models.Item;
import com.novare.tredara.models.User;
import com.novare.tredara.payloads.BidDto;
import com.novare.tredara.repositories.BidRepo;
import com.novare.tredara.repositories.ItemRepo;
import com.novare.tredara.repositories.UserRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;


import static com.novare.tredara.Utils.getFutureTime;
import static com.novare.tredara.Utils.getPastTime;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class BidControllerTest {

    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    BidRepo bidRepo;

    private Item savedItem;
    private User savedUser1;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();

        User user1 = new User("rohit1","aggarrohit@gmail.com","password");
        savedUser1 = userRepo.save(user1);

        User user2 = new User("rohit2","aggarrohit2@gmail.com","password");
        User savedUser2 = userRepo.save(user2);

        Item item = new Item();
        item.setTitle("item title");
        item.setDescription("item description");
        item.setStartPrice(100);
        item.setStartDateTime(new Date());
        item.setEndDateTime(getFutureTime());
        item.setStatus(EItemStatus.STATUS_OPEN);
        item.setUser(savedUser1);

        savedItem = itemRepo.save(item);
    }

    @AfterEach
    void cleanUp(){
        bidRepo.deleteAll();
        itemRepo.deleteAll();
        userRepo.deleteAll();
    }



    @Test
    @WithMockUser(username = "aggarrohit@gmail.com")
    void createBid_shouldFailForOwnItemBidding() throws Exception {

        BidDto bidDto = new BidDto();
        bidDto.setBidTime(new Date());
        bidDto.setItemId(savedItem.getId());
        bidDto.setAmount(100);

       String requestJson = objectMapper.writeValueAsString(bidDto);

        mockMvc.perform(post("/api/v1/bid")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(username = "aggarrohit2@gmail.com")
    void createBid_shouldPass() throws Exception {

        BidDto bidDto = new BidDto();
        bidDto.setBidTime(new Date());
        bidDto.setItemId(savedItem.getId());
        bidDto.setAmount(110);

        String requestJson = objectMapper.writeValueAsString(bidDto);

        mockMvc.perform(post("/api/v1/bid")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated());

    }

    @Test
    @WithMockUser(username = "aggarrohit2@gmail.com")
    void createBid_shouldFailWhenBidAmountLesserThanInitialPrice() throws Exception {

        BidDto bidDto = new BidDto();
        bidDto.setBidTime(new Date());
        bidDto.setItemId(savedItem.getId());
        bidDto.setAmount(99);

        String requestJson = objectMapper.writeValueAsString(bidDto);

        mockMvc.perform(post("/api/v1/bid")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(username = "aggarrohit2@gmail.com")
    void createBid_shouldFailWhenBidAmountLesserThanHighestBid() throws Exception {

        BidDto bidDto = new BidDto();
        bidDto.setBidTime(new Date());
        bidDto.setItemId(savedItem.getId());
        bidDto.setAmount(110);

        String requestJson = objectMapper.writeValueAsString(bidDto);

        mockMvc.perform(post("/api/v1/bid")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                );

        bidDto.setAmount(105);

        mockMvc.perform(post("/api/v1/bid")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("bid should be greater than the current highest bid"));

    }

    @Test
    @WithMockUser(username = "aggarrohit2@gmail.com")
    void createBid_shouldFailWhenInvalidItemId() throws Exception {

        BidDto bidDto = new BidDto();
        bidDto.setBidTime(new Date());
        bidDto.setItemId(100L);
        bidDto.setAmount(110);

        String requestJson = objectMapper.writeValueAsString(bidDto);

        mockMvc.perform(post("/api/v1/bid")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("item not found with itemId : 100"));

    }

    @Test
    @WithMockUser(username = "aggarrohit2@gmail.com")
    void createBid_shouldFailWhenItemIsNotActive() throws Exception {

        Item item = new Item();
        item.setTitle("item title");
        item.setDescription("item description");
        item.setStartPrice(100);
        item.setStartDateTime(new Date());
        item.setEndDateTime(getFutureTime());
        item.setStatus(EItemStatus.STATUS_CLOSE);
        item.setUser(savedUser1);

        savedItem = itemRepo.save(item);

        BidDto bidDto = new BidDto();
        bidDto.setBidTime(new Date());
        bidDto.setItemId(savedItem.getId());
        bidDto.setAmount(110);

        String requestJson = objectMapper.writeValueAsString(bidDto);

        mockMvc.perform(post("/api/v1/bid")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Item is not active"));

    }

    @Test
    @WithMockUser(username = "aggarrohit2@gmail.com")
    void createBid_shouldFailWhenItemEndDateIsPassed() throws Exception {

        Item item = new Item();
        item.setTitle("item title");
        item.setDescription("item description");
        item.setStartPrice(100);
        item.setStartDateTime(new Date());
        item.setEndDateTime(getPastTime());
        item.setStatus(EItemStatus.STATUS_OPEN);
        item.setUser(savedUser1);

        savedItem = itemRepo.save(item);

        BidDto bidDto = new BidDto();
        bidDto.setBidTime(new Date());
        bidDto.setItemId(savedItem.getId());
        bidDto.setAmount(110);

        String requestJson = objectMapper.writeValueAsString(bidDto);

        mockMvc.perform(post("/api/v1/bid")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Item is not active"));

    }

    @Test
    @WithMockUser(username = "aggarrohit2@gmail.com")
    void getItemBids() throws Exception {

        BidDto bidDto = new BidDto();
        bidDto.setBidTime(new Date());
        bidDto.setItemId(savedItem.getId());
        bidDto.setAmount(110);

        String requestJson = objectMapper.writeValueAsString(bidDto);

        mockMvc.perform(post("/api/v1/bid")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                );
        Long itemId = savedItem.getId();
        mockMvc.perform(get("/api/v1/bid/"+itemId)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "aggarrohit2@gmail.com")
    void getItemBids_shouldFailWithNoItemId() throws Exception {

        BidDto bidDto = new BidDto();
        bidDto.setBidTime(new Date());
        bidDto.setItemId(savedItem.getId());
        bidDto.setAmount(110);

        String requestJson = objectMapper.writeValueAsString(bidDto);

        mockMvc.perform(post("/api/v1/bid")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
        );

        mockMvc.perform(get("/api/v1/bid/")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "aggarrohit2@gmail.com")
    void getItemBids_shouldFailWithWrongItemId() throws Exception {

        BidDto bidDto = new BidDto();
        bidDto.setBidTime(new Date());
        bidDto.setItemId(savedItem.getId());
        bidDto.setAmount(110);

        String requestJson = objectMapper.writeValueAsString(bidDto);

        mockMvc.perform(post("/api/v1/bid")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
        );

        mockMvc.perform(get("/api/v1/bid/"+100)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("item not found with itemId : 100"));
    }


}