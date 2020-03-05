package com.KOndziu.researchserver.connectors;


import com.KOndziu.researchserver.allegroDTO.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@RestController
public class AllegroConnector {


    @Value("${allegro.offers.uri}")
    private String allegroOffersUri;
    @Value("${allegro.offers.carsId.param}")
    private String allegroCarParam;

    @Autowired
    private ResponseWrapper filterWrapper;

    @PostConstruct
    private void getFilters(){
        filterWrapper=getModelsCategories();    //bean to hold car filters
    }

    @GetMapping("/CarFilters")
    public ResponseWrapper getModelsCategories(){
    RestTemplate restTemplate=new RestTemplate();
    HttpHeaders headers=new HttpHeaders();

    headers.add("Authorization","Bearer ");
    headers.add("Accept","application/vnd.allegro.public.v1+json");

    HttpEntity entity=new HttpEntity(headers);
    ResponseEntity<ResponseWrapper> responseEntity=restTemplate.exchange("https://api.allegro.pl/offers/listing?category.id=4029",
            HttpMethod.GET, entity, ResponseWrapper.class);
    return responseEntity.getBody();
    }

//    &parametr.11323=11323_2
//    &parametr.199=199_2
//    &parametr.13=13_246582

}
