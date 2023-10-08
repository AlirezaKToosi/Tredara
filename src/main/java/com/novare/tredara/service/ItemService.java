package com.novare.tredara.service;

import com.novare.tredara.exceptions.ResourceNotFoundException;
import com.novare.tredara.models.Image;
import com.novare.tredara.models.Item;
import com.novare.tredara.models.User;
import com.novare.tredara.payloads.ItemDTO;
import com.novare.tredara.repositories.ImageRepo;
import com.novare.tredara.repositories.ItemRepo;
import com.novare.tredara.repositories.UserRepo;
import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private ItemRepo itemRepo;

    private ModelMapper modelMapper;

    private UserRepo userRepo;

    private ImageRepo imageRepo;

    @Autowired
    public ItemService(ItemRepo itemRepo, ModelMapper modelMapper, UserRepo userRepo, ImageRepo imageRepo) {
        this.itemRepo = itemRepo;
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
        this.imageRepo= imageRepo;
    }

    public ItemDTO createItem(ItemDTO itemDTO) throws SQLException {

        byte[] decodedByte = Base64.decodeBase64(itemDTO.getImageString());
        Blob imageBlob = new SerialBlob(decodedByte);

        Item item = this.dtoToItem(itemDTO);
        Item savedItem = this.itemRepo.save(item);

        Image image = new Image();
        image.setItem(savedItem);
        image.setImageBlob(imageBlob);

        imageRepo.save(image);
        

       ItemDTO savedtemDTO =  itemToDto(savedItem);
       savedtemDTO.setImageString(itemDTO.getImageString());
       return savedtemDTO;

    }

    public List<ItemDTO> searchByNameAndDescription(String query) {
        List<Item> items = itemRepo.searchByTitleOrDescriptionIgnoreCase(query);
        return items.stream().map(this::itemToDto).collect(Collectors.toList());
    }

    private Item dtoToItem(ItemDTO itemDTO) {
        User user = this.userRepo.findById(itemDTO.getUserID()).orElseThrow(() -> new ResourceNotFoundException("User", "Id", itemDTO.getUserID()));

        Item item = new Item();
        item.setTitle(itemDTO.getTitle());
        item.setDescription(itemDTO.getDescription());
        item.setStartPrice(itemDTO.getStartPrice());
        item.setStartDateTime(itemDTO.getStartDateTime());
        item.setEndDateTime(itemDTO.getEndDateTime());
        item.setStatus(itemDTO.getStatus());
        item.setUser(user);

        return item;
    }

    public ItemDTO itemToDto(Item item) {
        ItemDTO itemDTO = this.modelMapper.map(item, ItemDTO.class);
        return itemDTO;
    }

}
