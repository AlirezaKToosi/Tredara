package com.novare.tredara.services;

import com.novare.tredara.models.Item;
import com.novare.tredara.payloads.ItemDTO;
import com.novare.tredara.repositories.ImageRepo;
import com.novare.tredara.repositories.ItemRepo;
import com.novare.tredara.repositories.UserRepo;
import com.novare.tredara.utils.FileUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ItemService {

    FileSystemStorageService fileSystemStorageService;
    private ItemRepo itemRepo;
    private ModelMapper modelMapper;
    private UserRepo userRepo;

    @Autowired
    public ItemService(ItemRepo itemRepo, ModelMapper modelMapper, UserRepo userRepo, ImageRepo imageRepo, FileSystemStorageService fileSystemStorageService) {
        this.itemRepo = itemRepo;
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
        this.fileSystemStorageService = fileSystemStorageService;
    }

    public ItemDTO createItem(ItemDTO itemDTO) {
        String logMessage;

        if (itemDTO.getImage_url() != null) {
            logMessage = "Trying to convert base64 image and store it to filesystem..";
            log.info(logMessage);

            String imageDataString = FileUtil.getImageFromBase64(itemDTO.getImage_url());
            byte[] imageDecodedBytes = Base64.decodeBase64(imageDataString);

            String imageURL = this.fileSystemStorageService.storeBase64(imageDecodedBytes);

            String baseURL = "http://localhost:8080/files/";
            String complete_image_URL = baseURL + imageURL;

            logMessage = "image successfully stored, image url is: " + complete_image_URL;
            log.info(logMessage);
            itemDTO.setImage_url(complete_image_URL);
        }
        Item item = this.dtoToItem(itemDTO);
        Item savedItem = this.itemRepo.save(item);
        logMessage = "Item is saved in database";
        log.info(logMessage);
        return this.itemToDto(savedItem);
    }

    public List<ItemDTO> getAllItems() {
        List<Item> items = this.itemRepo.findAll();
        List<ItemDTO> itemDTOS = items.stream().map(user -> this.itemToDto(user)).collect(Collectors.toList());
        return itemDTOS;
    }

    public List<ItemDTO> getEndingSoonItems(){
        String sortBy = "endDateTime";
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.ASC, sortBy);
        Page<Item> endingSoonitems = this.itemRepo.findAll(pageable);


        List<ItemDTO> itemDTOS = endingSoonitems.stream().map(user -> this.itemToDto(user)).collect(Collectors.toList());

        return itemDTOS;
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
