package com.KOndziu.marketservice.dao;

import com.KOndziu.marketservice.modules.MarketCar;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class MarketCarDtoTest {


    @Test
    public void should_return_converted_object(){
        //given
        MarketCarDto dto=MarketCarDto.getDTO(getTestMarketCar(), Collections.EMPTY_SET);
        //then
        assertEquals(dto.getPicURLs(),Collections.EMPTY_SET);
        assertEquals(dto.getProdDate().toString(),"2010-10-10");
    }

    public MarketCar getTestMarketCar(){
        return MarketCar.builder()
                .carType("osobowy")
                .annoId(1)
                .color("czarny")
                .engForce(100.0)
                .engSize(1000.0)
                .locality("Kielce")
                .mileage(1000)
                .origin("Polska")
                .price(1000.0)
                .prodDate(LocalDate.of(2010,10,10))
                .province("Swietokrzyskie")
                .seats(5)
                .state("nowy")
                .build();
    }
}