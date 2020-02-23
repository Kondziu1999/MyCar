package com.KOndziu.marketservice.controllers;

import com.KOndziu.marketservice.dao.MarketCarDto;
import com.KOndziu.marketservice.dao.MarketDto;
import com.KOndziu.marketservice.modules.MarketCar;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MarketControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void should_return_marketCar_with_specific_criteria() throws Exception {
        MvcResult mvcResult=mockMvc.perform(get("/market/search")
                .param("search","state:uzywany,price>2000,color:green")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        List<MarketCarDto> marketDtos=new LinkedList<>();
                marketDtos=mapper.readValue(mvcResult.getResponse().getContentAsString(),
                        new TypeReference<List<MarketCarDto>>() {});

        MarketCar marketCarDto=MarketCarDto.getMarket(marketDtos.get(0));

        assertEquals("uzywany",marketCarDto.getState());
        assertEquals("Green",marketCarDto.getColor());
        assertTrue(marketCarDto.getPrice()>2000);
    }

}