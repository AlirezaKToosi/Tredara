package com.novare.tredara.controllers;

import com.novare.tredara.payloads.BidDto;
import com.novare.tredara.services.BidService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bid")
public class BidController {

    private final BidService bidService;

    @Autowired
    public BidController(BidService bidService){
        this.bidService = bidService;
    }

    @PostMapping
    public ResponseEntity<BidDto> createBid(@Valid @RequestBody BidDto bidDto, Principal principal) throws AuthenticationException {
        return new ResponseEntity<>(bidService.addBid(bidDto,principal), HttpStatus.CREATED);
    }


    @GetMapping("/{itemId}")
    public ResponseEntity<List<BidDto>> getItemBids(@PathVariable(required = true) Long itemId) {
        return new ResponseEntity<>(bidService.getBidsByItemId(itemId), HttpStatus.OK);
    }

}
