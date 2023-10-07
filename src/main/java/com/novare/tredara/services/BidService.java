package com.novare.tredara.services;

import com.novare.tredara.exceptions.BadRequestException;
import com.novare.tredara.exceptions.ResourceNotFoundException;
import com.novare.tredara.models.Bid;
import com.novare.tredara.models.EItemStatus;
import com.novare.tredara.models.Item;
import com.novare.tredara.models.User;
import com.novare.tredara.payloads.BidDto;
import com.novare.tredara.repositories.BidRepo;
import com.novare.tredara.repositories.ItemRepo;
import com.novare.tredara.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class BidService {

    BidRepo bidRepo;
    UserRepo userRepo;
    ItemRepo itemRepo;

    @Autowired
    public BidService(BidRepo bidRepo,UserRepo userRepo,ItemRepo itemRepo) {
        this.bidRepo = bidRepo;
        this.userRepo = userRepo;
        this.itemRepo = itemRepo;
    }

    public List<BidDto> getBidsByItemId(Long itemId){
        //checking for valid itemId
        getItemByItemId(itemId);
        return bidRepo.findByItemIdOrderByAmountDesc(itemId).stream().map(this::toBidDto).toList();
    }

    public BidDto addBid(BidDto bidDto, Principal principal) throws AuthenticationException {
        //checking for valid itemId
        Item item = getItemByItemId(bidDto.getItemId());

        Bid bid = toBid(bidDto,principal);

        if(getCurrentHighestBid(item)>=bidDto.getAmount()) throw new BadRequestException("bid should be greater than the current highest bid");

        return toBidDto(bidRepo.save(bid));
    }

    private BidDto toBidDto(Bid bid){
        BidDto bidDto = new BidDto();

        bidDto.setId(bid.getId());
        bidDto.setBidTime(bid.getBidTime());
        bidDto.setAmount(bid.getAmount());
        bidDto.setItemId(bid.getItem().getId());
        bidDto.setBidderName(bid.getUser().getFullName());

        return bidDto;
    }

    private Bid toBid(BidDto bidDto,Principal principal) throws AuthenticationException,ResourceNotFoundException,BadRequestException {
        Bid bid = new Bid();

        if(principal==null) throw new AuthenticationException("User not authenticated");
        //getting current logged-in user and checking if no user logged in
        User currentuser = userRepo.findByEmail(principal.getName()).orElseThrow(()->new AuthenticationException("User not authenticated"));
        //getting item with itemId and checking for valid item id
        Item item = getItemByItemId(bidDto.getItemId());

        //checks if user trying to bid on own item
        if(item.getUser().getId().equals(currentuser.getId())) throw new BadRequestException("user not allowed to bid on own item");

        boolean itemStatusIsClosed = item.getStatus()== EItemStatus.STATUS_CLOSE;
        boolean itemEndTimePassed = item.getEndDateTime().before(new Date());
        if(itemStatusIsClosed || itemEndTimePassed) throw new BadRequestException("Item is not active");

        bid.setBidTime(new Date());
        bid.setAmount(bidDto.getAmount());
        bid.setUser(currentuser);
        bid.setItem(item);

        return bid;
    }

    private Item getItemByItemId(Long itemId) {
        return itemRepo.findById(itemId).orElseThrow(()->new ResourceNotFoundException("item","itemId", itemId));
    }

    private Double getCurrentHighestBid(Item item){
        Double currentHighestBid = bidRepo.getCurrentHighestBid(item.getId());
        if(currentHighestBid==null) return  item.getStartPrice();
        return  currentHighestBid;
    }

}
