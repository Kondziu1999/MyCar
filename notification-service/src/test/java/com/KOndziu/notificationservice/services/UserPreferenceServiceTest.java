package com.KOndziu.notificationservice.services;

import com.KOndziu.notificationservice.dao.UserPreferenceDAO;
import com.KOndziu.notificationservice.dto.MarketCarDto;
import com.KOndziu.notificationservice.modules.UserPreference;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserPreferenceServiceTest {


    @Autowired
    UserPreferenceService userPreferenceService;

    @Test
    public void should_return_appropriate_result_string(){
        //given
        MarketCarDto marketCarDto=getTestMarketCarDto();
        //when
        String params=userPreferenceService.compileSearchString(marketCarDto);
        //then
        assertEquals(params,"search=carType:osobowy,mark:audi");
    }

    public MarketCarDto getTestMarketCarDto(){
        return MarketCarDto.builder()
                .carType("osobowy")
                .mark("audi")
                .build();
    }


}