package com.KOndziu.researchserver.connectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Beans {

    @Bean
    RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

}
