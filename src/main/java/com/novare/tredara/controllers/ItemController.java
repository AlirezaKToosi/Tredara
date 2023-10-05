package com.novare.tredara.controllers;

import com.novare.tredara.payloads.ItemDTO;
import com.novare.tredara.services.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/item/create")
    public ResponseEntity<ItemDTO> createItem(@Valid @RequestBody ItemDTO itemDTO) throws SQLException {
        ItemDTO createItemDto = this.itemService.createItem(itemDTO);
        return new ResponseEntity<>(createItemDto, HttpStatus.CREATED);
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<ItemDTO> getItemDetails(@Valid @PathVariable Long itemId) throws SQLException {
        ItemDTO retrivedItemDTO = this.itemService.getItemDetails(itemId);
        return new ResponseEntity<>(retrivedItemDTO, HttpStatus.OK);
    }

}
