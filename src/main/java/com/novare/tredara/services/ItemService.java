package com.novare.tredara.services;

import com.novare.tredara.exceptions.ResourceNotFoundException;
import com.novare.tredara.models.Item;
import com.novare.tredara.payloads.ItemDTO;
import com.novare.tredara.payloads.ItemInfoDTO;
import com.novare.tredara.repositories.ItemRepo;
import com.novare.tredara.repositories.UserRepo;
import com.novare.tredara.utils.DateUtils;
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
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Date;
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
    public ItemService(ItemRepo itemRepo, ModelMapper modelMapper, UserRepo userRepo, FileSystemStorageService fileSystemStorageService) {
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
    @Transactional(readOnly = true)
    public ItemInfoDTO getItemInfo(Long itemId) throws SQLException {
        Item item = this.itemRepo.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));

        ItemInfoDTO itemInfoDTO = new ItemInfoDTO();
        itemInfoDTO.setImageUrl(item.getImage_url());
        itemInfoDTO.setTitle(item.getTitle());
        itemInfoDTO.setStartPrice(item.getStartPrice());
        itemInfoDTO.setDescription(item.getDescription());

        String timeToBidEnd = DateUtils.getTimeToBidEnd(item.getEndDateTime());
        itemInfoDTO.setTimeToBidEnd(timeToBidEnd);

        int numberOfBids = item.getBids().size();
        itemInfoDTO.setNumberOfBids(numberOfBids);

        return itemInfoDTO;
    }

    public List<ItemDTO> getEndingSoonItems(){
        String sortBy = "endDateTime";
        Pageable pageable = PageRequest.of(0, 8, Sort.Direction.ASC, sortBy);
        Page<Item> endingSoonitems = this.itemRepo.findAll(pageable);


        List<ItemDTO> itemDTOS = endingSoonitems.stream().map(user -> this.itemToDto(user)).collect(Collectors.toList());

        return itemDTOS;
    }
    public List<ItemDTO> getLatestItems() {

        String sortBy = "startDateTime";
        Pageable pageable = PageRequest.of(0, 8, Sort.Direction.ASC, sortBy);
        Page<Item> endingSoonitems = this.itemRepo.findAll(pageable);


        List<ItemDTO> itemDTOS = endingSoonitems.stream().map(user -> this.itemToDto(user)).collect(Collectors.toList());

        return itemDTOS;
    }


    public List<ItemDTO> searchByNameAndDescription(String query) {
        List<Item> items = itemRepo.searchByTitleOrDescriptionIgnoreCase(query);
        return items.stream().map(this::itemToDto).collect(Collectors.toList());
    }

    public List<ItemDTO> getItemsCreatedByUser(Long userId) {
        userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        List<Item> items = this.itemRepo.findByUserId(userId);
        return  items.stream().map(this::itemToDto).collect(Collectors.toList());
    }

    public List<ItemDTO> getItemsByUserBids(Long userId) {
        userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        List<Item> items = itemRepo.findItemsByBidsUserId(userId);
        return items.stream().map(this::itemToDto).collect(Collectors.toList());
    }

//    public List<ItemDTO> getItemsWonByUser(Long userId) {
//        List<Item> items = itemRepo.findItemsWonByUser(userId);
//        return items.stream().map(this::itemToDto).collect(Collectors.toList());
//    }


    private Item dtoToItem(ItemDTO itemDTO) {
        Item item = this.modelMapper.map(itemDTO, Item.class);
        return item;
    }

    public ItemDTO itemToDto(Item item) {
        ItemDTO itemDTO = this.modelMapper.map(item, ItemDTO.class);
        return itemDTO;
    }

}


