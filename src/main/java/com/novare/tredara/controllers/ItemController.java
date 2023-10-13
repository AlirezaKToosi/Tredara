package com.novare.tredara.controllers;

import com.novare.tredara.exceptions.ResourceNotFoundException;
import com.novare.tredara.payloads.ItemDTO;
import com.novare.tredara.payloads.ItemInfoDTO;
import com.novare.tredara.security.jwt.JwtTokenUtil;
import com.novare.tredara.services.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ItemController {

    private ItemService itemService;
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public ItemController(ItemService itemService,JwtTokenUtil jwtTokenUtil) {
        this.itemService = itemService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/items/search")
    public ResponseEntity<List<ItemDTO>> searchItems(@RequestParam String query) {
        List<ItemDTO> items = this.itemService.searchByNameAndDescription(query);
        return ResponseEntity.ok(items);
    }

    @PostMapping("/item/create")
    public ResponseEntity<ItemDTO> createItem(@Valid @RequestBody ItemDTO itemDTO, HttpServletRequest request) {
        String username = jwtTokenUtil.getUsernameFromRequest(request);
        if (username != null) {
            ItemDTO createdItemDto = itemService.createItem(itemDTO, username);
            return new ResponseEntity<>(createdItemDto, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/items")
    public ResponseEntity<List<ItemDTO>> getAllItems() {
        return ResponseEntity.ok(this.itemService.getAllItems());
    }

    @GetMapping("/endingSoonItems")
    public ResponseEntity<List<ItemDTO>> getEndingSoonItems() {
        return ResponseEntity.ok(this.itemService.getEndingSoonItems());
    }

    @GetMapping("/latestItems")
    public ResponseEntity<List<ItemDTO>> getLatestItems() {
        return ResponseEntity.ok(this.itemService.getLatestItems());
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<ItemInfoDTO> getItemInfo(@PathVariable Long itemId) {
        try {
            ItemInfoDTO itemInfoDTO = itemService.getItemInfo(itemId);
            return ResponseEntity.ok(itemInfoDTO);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/items/createdByUser/{userId}")
    public ResponseEntity<List<ItemDTO>> getItemsCreatedByUser(@PathVariable Long userId) {
        try {
            List<ItemDTO> items = this.itemService.getItemsCreatedByUser(userId);
            return ResponseEntity.ok(items);
        }
        catch(ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/items/bidedByUser/{userId}")
    public ResponseEntity<List<ItemDTO>> getItemsBidedByUser(@PathVariable Long userId) {
        try {
            List<ItemDTO> items = this.itemService.getItemsByUserBids(userId);
            return ResponseEntity.ok(items);
        }
        catch(ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

