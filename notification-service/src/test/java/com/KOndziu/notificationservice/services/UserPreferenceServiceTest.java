package com.KOndziu.notificationservice.services;


import com.KOndziu.notificationservice.dto.MarketCarDto;
import com.KOndziu.notificationservice.modules.UserPreference;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

@AutoConfigureMockMvc
class UserPreferenceServiceTest {


    @Autowired
    UserPreferenceService userPreferenceService;

    private LocalDate testDate;
    private Double testEngSize;
    private Double testEngForce;

    @BeforeEach
    public void setUp(){
        testDate=LocalDate.parse("2010-10-10");
        testEngSize=1600.0;
        testEngForce=130.0;
    }

    @Test
    public void should_return_appropriate_result_string(){
        //given
        MarketCarDto marketCarDto=getTestMarketCarDto();
        //when
        String params=userPreferenceService.compileSearchString(marketCarDto);
        //then
        assertEquals(params,"search=carType:osobowy,mark:audi");
    }




    @Test
    public void should_return_user_preferences_with_given_parameters(){
        //given
        MarketCarDto marketCarDto=getTestMarketCarDto();
        String params=userPreferenceService.compileSearchString(marketCarDto);
        //when
        List<UserPreference> preferences=userPreferenceService.findPreferences(params);
        UserPreference firstResult=preferences.get(0);
        assertEquals("Audi",firstResult.getMark());
        assertEquals("osobowy",firstResult.getCarType());
        assertTrue(4000.0<=firstResult.getPriceTo());
        assertTrue(4000.0>=firstResult.getPriceFrom());
        assertTrue(testDate.isBefore(firstResult.getProdDateTo()));
        assertTrue(testDate.isAfter(firstResult.getProdDateFrom()));
        assertTrue(testEngSize>=firstResult.getEngSizeFrom());
        assertTrue(testEngSize<=firstResult.getEngSizeTo());
        assertTrue(testEngForce>=firstResult.getEngForceFrom());
        assertTrue(testEngForce<=firstResult.getEngForceTo());

    }




    public MarketCarDto getTestMarketCarDto(){
        return MarketCarDto.builder()
                .carType("osobowy")
                .mark("audi")
                .price(4000.0)
                .prodDate(testDate)
                .engSize(testEngSize)
                .engForce(testEngForce)
                .build();
    }


}