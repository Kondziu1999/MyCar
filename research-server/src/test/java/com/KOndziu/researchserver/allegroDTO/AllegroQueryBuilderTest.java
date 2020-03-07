package com.KOndziu.researchserver.allegroDTO;

import com.KOndziu.researchserver.DTO.UserPreferenceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        String searchQuery=allegroQueryBuilder.compileToSearchString(userPreferenceDTO);
        //then
        assertEquals(searchQuery,"?category.id=4031&parameter.13=13_1&price.from=10000");
    }

    public UserPreferenceDTO prepareDTO(){
        return UserPreferenceDTO.builder()
                .carType("hatchback")
                .priceFrom(10000.0)
                .mark("audi")
                .build();
    }

}

