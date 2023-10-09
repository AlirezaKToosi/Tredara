package com.novare.tredara.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novare.tredara.payloads.BidDto;
import com.novare.tredara.repositories.UserRepo;
import com.novare.tredara.services.BidService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BidController.class)
class BidControllerTest {

    @TestConfiguration
    static class DefaultConfigWithoutCsrf{
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable);
            return http.build();
        }
    }

    @MockBean
    BidService bidService;

    @MockBean
    UserRepo userRepo;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }


    @Test
    @WithMockUser
    void createBid() throws Exception {

        BidDto bidDto = new BidDto();
        bidDto.setBidTime(new Date());
        bidDto.setItemId(11L);
        bidDto.setAmount(100);

        Principal principal = (UserPrincipal) () -> "aggarrohit@gmail.com";


       when(bidService.addBid(bidDto,principal)).thenReturn(bidDto);

       String requestJson = objectMapper.writeValueAsString(bidDto);

        mockMvc.perform(post("/api/v1/bid")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated());

    }

    @Test
    @WithMockUser
    void getItemBids() throws Exception {
       when(bidService.getBidsByItemId(anyLong())).thenReturn(new ArrayList<BidDto>());

       mockMvc.perform(get("/api/v1/bid/1"))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getItemBids_shouldFailWithNoItemId() throws Exception {
        when(bidService.getBidsByItemId(anyLong())).thenReturn(new ArrayList<BidDto>());

        mockMvc.perform(get("/api/v1/bid/"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}