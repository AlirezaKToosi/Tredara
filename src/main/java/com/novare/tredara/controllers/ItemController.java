package com.novare.tredara.controllers;

import com.novare.tredara.payloads.ItemDTO;
import com.novare.tredara.service.ItemService;
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

    @GetMapping("/items/search")
    public ResponseEntity<List<ItemDTO>> searchItemsByName(@RequestParam String title){
        List<ItemDTO> items = this.itemService.findByTitle(title);
        return ResponseEntity.ok(items);
    }

    @PostMapping("/item/create")
    public ResponseEntity<ItemDTO> createItem(@Valid @RequestBody ItemDTO itemDTO) throws SQLException {
        ItemDTO createItemDto = this.itemService.createItem(itemDTO);
        return new ResponseEntity<>(createItemDto, HttpStatus.CREATED);
    }

}
