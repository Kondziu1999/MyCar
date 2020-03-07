package com.KOndziu.researchserver.allegroDTO.Filters;

import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    ResponseWrapper getFilterWrapper(){
        return new ResponseWrapper();
    }

    @Bean
    HashFilters getHashFilters(){
        return new HashFilters();
    }
}
