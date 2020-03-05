package com.KOndziu.researchserver.allegroDTO;

import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    ResponseWrapper getFilterWrapper(){
        return new ResponseWrapper();
    }
}
