package com.KOndziu.researchserver.connectors;


import com.KOndziu.researchserver.allegroDTO.CarFilter;
import com.KOndziu.researchserver.allegroDTO.HashFilters;
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
import java.util.HashMap;

@RestController
public class AllegroConnector {


    @Value("${allegro.offers.uri}")
    private String allegroOffersUri;
    @Value("${allegro.offers.carsId.param}")
    private String allegroCarParam;

    @Autowired
    private ResponseWrapper filterWrapper;
    @Autowired
    private HashFilters hashFilters;

    @PostConstruct
    private void getFilters(){
        ResponseWrapper filterWrapper=getModelsCategories();    //bean to hold car filters
        this.filterWrapper.setCategories(filterWrapper.getCategories());
        this.filterWrapper.setFilters(filterWrapper.getFilters());
        HashMap<String, CarFilter> filters= new HashMap<>();
        filterWrapper.getFilters().forEach(filter ->{
            filters.put(filter.getName().toLowerCase(),filter);
        });
        hashFilters.setHashFilters(filters);

    }

    @GetMapping("/CarFilters")
    public ResponseWrapper getModelsCategories(){
    RestTemplate restTemplate=new RestTemplate();
    HttpHeaders headers=new HttpHeaders();

    headers.add("Authorization","Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJhbGxlZ3JvX2FwaSJdLCJleHAiOjE1ODM2MTUwNDMsImp0aSI6ImNmOTRmYjgxLTIwMjgtNGI2My1iNGIyLTBiMDJkZWU5YTNlNiIsImNsaWVudF9pZCI6IjhjOTM0NGEyODE1NjQyZjBhMDU5NzdiZDNhOWJkZmQ3In0.s_kg3RQ7CDnQUJZgeolB-0JBfwNj0B4Wop5xDZ4m7b6_MOobZsfEOI5fhCuimU7h5h0-FzJptoZ9KZtUoBtBlaNS8Hv1a8g22oKOZo6xhY2bLtlv7WiLmzDAS3YlxAWgOT1s5Z6kOU-hEJBb8zClnmqKtFojd2g7IiUMFShTLCBr0vKlUSKLFG10bZsnWKtcVW_es0r-aDmHvPyDmcfE6DhoAtsiviM4YaOEgiQ-61UgHy4ELcBqcJSNVGqmD8HrGfZcKYc1Ed0GIgvCkx0Xd_Fuk3AEbX6Ifor0bn7dinTQb2fTU-gDwXqXSeTuDDe8x-W71OrPYQqmvFP4bxe-ow");
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
