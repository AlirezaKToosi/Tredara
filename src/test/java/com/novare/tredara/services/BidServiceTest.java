package com.novare.tredara.services;

import com.novare.tredara.exceptions.BadRequestException;
import com.novare.tredara.exceptions.ResourceNotFoundException;
import com.novare.tredara.models.*;
import com.novare.tredara.payloads.BidDto;
import com.novare.tredara.repositories.BidRepo;
import com.novare.tredara.repositories.ItemRepo;
import com.novare.tredara.repositories.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

import static com.novare.tredara.Utils.getFutureTime;
import static com.novare.tredara.Utils.getPastTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class BidServiceTest {

    @Mock
    BidRepo bidRepo;

    @Mock
    UserRepo userRepo;

    @Mock
    ItemRepo itemRepo;

    BidService bidService;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp(){
       autoCloseable = MockitoAnnotations.openMocks(this);
       bidService = new BidService(bidRepo,userRepo,itemRepo);

        User user = new User("rohit agarwal","aggarrohit@gmail.com","password");

        Item item = new Item();
        item.setTitle("item title");
        item.setDescription("item description");
        item.setStartPrice(100);
        item.setStartDateTime(new Date());
        item.setEndDateTime(getFutureTime());
        item.setStatus(EItemStatus.STATUS_OPEN);
        item.setUser(user);

        Bid bid = new Bid();
        bid.setItem(item);
        bid.setUser(user);
       when(bidRepo.save(any())).thenReturn(bid);
    }



    @Test
    void getBidsByItemId_emptyBids() {
        when(bidRepo.findByItemIdOrderByAmountDesc(anyLong())).thenReturn(new ArrayList<Bid>());
        when(itemRepo.findById(anyLong())).thenReturn(Optional.of(new Item()));
        assertThat(bidService.getBidsByItemId(1L)).isNotNull();
    }

    @Test
    void getBidsByItemId_shouldFailForInvalidItemId() {
        assertThrows(ResourceNotFoundException.class,()->bidService.getBidsByItemId(1000L));
    }

    @Test
    void getBidsByItemId_nonEmptyBids() {
        List<Bid> bidList = new ArrayList<>();
        User user = new User("rohit agarwal","aggarrohit@gmail.com","password");

        Item item = new Item();
        item.setTitle("item title");
        item.setDescription("item description");
        item.setStartPrice(100);
        item.setStartDateTime(new Date());
        item.setEndDateTime(getFutureTime());
        item.setStatus(EItemStatus.STATUS_OPEN);
        item.setUser(user);

        Bid bid = new Bid();
        bid.setItem(item);
        bid.setUser(user);
        bidList.add(bid);
        bidList.add(bid);

        when(bidRepo.findByItemIdOrderByAmountDesc(anyLong())).thenReturn(bidList);
        when(itemRepo.findById(anyLong())).thenReturn(Optional.of(new Item()));

        assertThat(bidService.getBidsByItemId(1L)).isNotEmpty();
        assertThat(bidService.getBidsByItemId(1L)).hasSize(2);
        assertThat(bidService.getBidsByItemId(1L)).hasOnlyElementsOfType(BidDto.class);
    }

    @Mock
    Principal principal;

    @Test
    void addBid_shouldFailIfItemIdNotValid(){
        BidDto bidDto = new BidDto();
        User user = new User(101L,"aggarrohit@gmail.com","rohit agarwal","password", ERole.ROLE_CUSTOMER,null,null,null);

        Item item = new Item();
        item.setId(1L);
        item.setTitle("item title");
        item.setDescription("item description");
        item.setStartPrice(111);
        item.setStartDateTime(new Date());
        item.setEndDateTime(getFutureTime());
        item.setStatus(EItemStatus.STATUS_OPEN);
        item.setUser(user);

        bidDto.setBidTime(new Date());
        bidDto.setAmount(125);
        bidDto.setItemId(item.getId());

        when(itemRepo.findById(anyLong())).thenThrow(new ResourceNotFoundException("","",1L));

        assertThrows(ResourceNotFoundException.class,()->bidService.addBid(bidDto,principal));
    }

    @Test
    void addBid_shouldFailIfSellerIdEqualsUserId(){
        BidDto bidDto = new BidDto();
        User user = new User(101L,"aggarrohit@gmail.com","rohit agarwal","password", ERole.ROLE_CUSTOMER,null,null,null);

        Item item = new Item();
        item.setTitle("item title");
        item.setDescription("item description");
        item.setStartPrice(111);
        item.setStartDateTime(new Date());
        item.setEndDateTime(getFutureTime());
        item.setStatus(EItemStatus.STATUS_OPEN);
        item.setUser(user);

        bidDto.setBidTime(new Date());
        bidDto.setAmount(125);
        bidDto.setItemId(item.getId());

        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(principal.getName()).thenReturn("aggarrohit@gmail.com");

        assertThrows(BadRequestException.class,()->bidService.addBid(bidDto,principal));
    }

    @Test
    void addBid_shouldFailIfAmountLesserThanCurrentHighestBid(){
        BidDto bidDto = new BidDto();
        User user = new User(101L,"aggarrohit@gmail.com","rohit agarwal","password", ERole.ROLE_CUSTOMER,null,null,null);
        User user1 = new User(102L,"aggarrohit@gmail.com","rohit agarwal","password", ERole.ROLE_CUSTOMER,null,null,null);

        Item item = new Item();
        item.setId(1L);
        item.setTitle("item title");
        item.setDescription("item description");
        item.setStartPrice(1111);
        item.setStartDateTime(new Date());
        item.setEndDateTime(getFutureTime());
        item.setStatus(EItemStatus.STATUS_OPEN);
        item.setUser(user);

        bidDto.setBidTime(new Date());
        bidDto.setAmount(125);
        bidDto.setItemId(item.getId());

        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepo.findByEmail("aggarrohit1@gmail.com")).thenReturn(Optional.of(user1));
        when(principal.getName()).thenReturn("aggarrohit1@gmail.com");
        when(bidRepo.getCurrentHighestBid(anyLong())).thenReturn(130.0);

        assertThrows(BadRequestException.class,()->bidService.addBid(bidDto,principal));
    }

    @Test
    void addBid_shouldPassIfAmountGreaterThanCurrentHighestBid(){
        BidDto bidDto = new BidDto();
        User user = new User(101L,"aggarrohit@gmail.com","rohit agarwal","password", ERole.ROLE_CUSTOMER,null,null,null);
        User user1 = new User(102L,"aggarrohit@gmail.com","rohit agarwal","password", ERole.ROLE_CUSTOMER,null,null,null);

        Item item = new Item();
        item.setId(11L);
        item.setTitle("item title");
        item.setDescription("item description");
        item.setStartPrice(111);
        item.setStartDateTime(new Date());
        item.setEndDateTime(getFutureTime());
        item.setStatus(EItemStatus.STATUS_OPEN);
        item.setUser(user);

        bidDto.setBidTime(new Date());
        bidDto.setAmount(125);
        bidDto.setItemId(item.getId());

        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepo.findByEmail("aggarrohit1@gmail.com")).thenReturn(Optional.of(user1));
        when(principal.getName()).thenReturn("aggarrohit1@gmail.com");
        when(bidRepo.getCurrentHighestBid(anyLong())).thenReturn(120.0);


        assertDoesNotThrow(()->bidService.addBid(bidDto,principal));
    }

    @Test
    void addBid_shouldFailIfItemIsNotActive(){
        BidDto bidDto = new BidDto();
        User user = new User(101L,"aggarrohit@gmail.com","rohit agarwal","password", ERole.ROLE_CUSTOMER,null,null,null);
        User user1 = new User(102L,"aggarrohit@gmail.com","rohit agarwal","password", ERole.ROLE_CUSTOMER,null,null,null);

        Item item = new Item();
        item.setId(11L);
        item.setTitle("item title");
        item.setDescription("item description");
        item.setStartPrice(111);
        item.setStartDateTime(new Date());
        item.setEndDateTime(getFutureTime());
        item.setStatus(EItemStatus.STATUS_CLOSE);
        item.setUser(user);

        bidDto.setBidTime(new Date());
        bidDto.setAmount(125);
        bidDto.setItemId(item.getId());

        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepo.findByEmail("aggarrohit1@gmail.com")).thenReturn(Optional.of(user1));
        when(principal.getName()).thenReturn("aggarrohit1@gmail.com");
        when(bidRepo.getCurrentHighestBid(anyLong())).thenReturn(120.0);

        assertThrows(BadRequestException.class,()->bidService.addBid(bidDto,principal));
    }

    @Test
    void addBid_shouldPassIfItemIsActive(){

        BidDto bidDto = new BidDto();
        User user = new User(101L,"aggarrohit@gmail.com","rohit agarwal","password", ERole.ROLE_CUSTOMER,null,null,null);
        User user1 = new User(102L,"aggarrohit@gmail.com","rohit agarwal","password", ERole.ROLE_CUSTOMER,null,null,null);

        Item item = new Item();
        item.setId(11L);
        item.setTitle("item title");
        item.setDescription("item description");
        item.setStartPrice(111);
        item.setStartDateTime(new Date());
        item.setEndDateTime(getFutureTime());
        item.setStatus(EItemStatus.STATUS_OPEN);
        item.setUser(user);

        bidDto.setBidTime(new Date());
        bidDto.setAmount(125);
        bidDto.setItemId(item.getId());

        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepo.findByEmail("aggarrohit1@gmail.com")).thenReturn(Optional.of(user1));
        when(principal.getName()).thenReturn("aggarrohit1@gmail.com");
        when(bidRepo.getCurrentHighestBid(anyLong())).thenReturn(120.0);

        assertDoesNotThrow(()->bidService.addBid(bidDto,principal));
    }

    @Test
    void addBid_shouldFailIfItemEndTimeIsPassed(){

        BidDto bidDto = new BidDto();
        User user = new User(101L,"aggarrohit@gmail.com","rohit agarwal","password", ERole.ROLE_CUSTOMER,null,null,null);
        User user1 = new User(102L,"aggarrohit@gmail.com","rohit agarwal","password", ERole.ROLE_CUSTOMER,null,null,null);

        Item item = new Item();
        item.setId(11L);
        item.setTitle("item title");
        item.setDescription("item description");
        item.setStartPrice(111);
        item.setStartDateTime(new Date());
        item.setEndDateTime(getPastTime());
        item.setStatus(EItemStatus.STATUS_OPEN);
        item.setUser(user);

        bidDto.setBidTime(new Date());
        bidDto.setAmount(125);
        bidDto.setItemId(item.getId());

        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepo.findByEmail("aggarrohit1@gmail.com")).thenReturn(Optional.of(user1));
        when(principal.getName()).thenReturn("aggarrohit1@gmail.com");
        when(bidRepo.getCurrentHighestBid(anyLong())).thenReturn(120.0);

        assertThrows(BadRequestException.class,()->bidService.addBid(bidDto,principal));
    }


}