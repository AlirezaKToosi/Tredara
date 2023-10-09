package com.novare.tredara.controllers;

import com.novare.tredara.payloads.ItemInfoDTO;
import com.novare.tredara.services.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ItemControllerTest {
    @Mock
    private ItemService itemService;

    private ItemController itemController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        itemController = new ItemController(itemService);
    }

    @Test
    void getItemInfo() throws SQLException {
        Long itemId = 123L;
        ItemInfoDTO itemInfoDTO = new ItemInfoDTO();
        when(itemService.getItemInfo(itemId)).thenReturn(itemInfoDTO);

        ResponseEntity response = itemController.getItemInfo(itemId);

        assertSame(HttpStatus.OK,response.getStatusCode());
        assertSame(itemInfoDTO,response.getBody());

    }

    @Test
    void testGetItemInfoWithSQLException() throws SQLException {
        // Arrange
        Long itemId = 123L;
        when(itemService.getItemInfo(itemId)).thenThrow(SQLException.class);

        // Act
        ResponseEntity<ItemInfoDTO> response = itemController.getItemInfo(itemId);

        // Assert
        verify(itemService).getItemInfo(itemId);
        assertSame(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}