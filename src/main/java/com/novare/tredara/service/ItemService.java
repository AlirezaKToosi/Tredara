package com.novare.tredara.service;

import com.novare.tredara.models.Item;
import com.novare.tredara.payloads.ItemDTO;
import com.novare.tredara.repositories.ItemRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    private ItemRepo itemRepo;

    private ModelMapper modelMapper;

    @Autowired
    public ItemService(ItemRepo itemRepo, ModelMapper modelMapper) {
        this.itemRepo = itemRepo;
        this.modelMapper = modelMapper;
    }

    public ItemDTO createItem(ItemDTO itemDTO) {

        /*
        Validations
         */

        Item item = this.dtoToItem(itemDTO);
        Item savedItem = this.itemRepo.save(item);
        return this.itemToDto(savedItem);
    }

    private Item dtoToItem(ItemDTO itemDTO) {
        Item item = this.modelMapper.map(itemDTO, Item.class);
        return item;
    }

    public ItemDTO itemToDto(Item item) {
        ItemDTO itemDTO = this.modelMapper.map(item, ItemDTO.class);
        return itemDTO;
    }

}
