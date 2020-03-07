package com.KOndziu.researchserver.allegroDTO;

import com.KOndziu.researchserver.DTO.UserPreferenceDTO;
import com.KOndziu.researchserver.allegroDTO.Filters.AllegroQueryBuilder;
import com.KOndziu.researchserver.allegroDTO.Filters.HashFilters;
import com.KOndziu.researchserver.allegroDTO.Filters.ResponseWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

class AllegroQueryBuilderTest {

    @Autowired
    ResponseWrapper responseWrapper;
    @Autowired
    HashFilters hashFilters;
    @Autowired
    AllegroQueryBuilder allegroQueryBuilder;

    @BeforeEach
    public void setUp(){

    }

    @Test
    public void shouldReturnAppropriateQueryString(){
        //given
        UserPreferenceDTO userPreferenceDTO=prepareDTO();
        //when
        String searchQuery=allegroQueryBuilder.compileToSearchString(userPreferenceDTO, Optional.empty());
        //then

        assertTrue(searchQuery.startsWith("?category.id="));
        assertTrue(searchQuery.contains("&price.from=10000"));
        assertTrue(searchQuery.contains("&parameter.3=3_4"));//color
        assertTrue(searchQuery.contains("&parameter.14.from=100&parameter.14.to=200"));//power
        assertTrue(searchQuery.contains("&parameter.1.from=2010"));
        assertTrue(searchQuery.contains("&parameter.1.to=2015"));

        System.out.println(searchQuery);

    }

    public UserPreferenceDTO prepareDTO(){
        return UserPreferenceDTO.builder()
                .carType("hatchback")
                .priceFrom(10000.0)
                .mark("audi")
                .color("czarny")
                .engForceFrom(100.0)
                .engForceTo(200.0)
                .prodDateFrom(LocalDate.of(2010,10,10))
                .prodDateTo(LocalDate.of(2015,10,10))
                .build();
    }

}

