package com.novare.tredara.controllers;

import com.novare.tredara.payloads.ItemDTO;
import com.novare.tredara.services.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ItemController {

    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService){
        this.itemService = itemService;
    }

    @PostMapping("/item/create")
    public ResponseEntity<ItemDTO> createItem(@Valid @RequestBody ItemDTO itemDTO) {
        ItemDTO createItemDto = this.itemService.createItem(itemDTO);
        return new ResponseEntity<>(createItemDto, HttpStatus.CREATED);
    }

    @GetMapping("/items")
    public ResponseEntity<List<ItemDTO>> getAllItems() {
        return ResponseEntity.ok(this.itemService.getAllItems());
    }

    @GetMapping("/endinngSoonItems")
    public ResponseEntity<List<ItemDTO>> getEndingSoonItems()  {
        return ResponseEntity.ok(this.itemService.getEndingSoonItems());
    }
    @GetMapping("/latestItems")
    public ResponseEntity<List<ItemDTO>> getLatestItems()  {
        return ResponseEntity.ok(this.itemService.getLatestItems());
    }





}
