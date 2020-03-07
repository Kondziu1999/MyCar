package com.KOndziu.researchserver.connectors;


import com.KOndziu.researchserver.DTO.UserPreferenceDTO;
import com.KOndziu.researchserver.allegroDTO.Cars.CarsWrapper;
import com.KOndziu.researchserver.allegroDTO.Cars.ItemsWrapper;
import com.KOndziu.researchserver.allegroDTO.Filters.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Optional;

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
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    AllegroQueryBuilder allegroQueryBuilder;

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
    @GetMapping("/GetCarModels/{markId}")
    public CarCategoriesWrapper getCarModels(@PathVariable Integer markId){
        String requestUrl=allegroOffersUri+"?category.id="+markId;
        //in subcategories models will occur
        ResponseEntity<ResponseWrapper> responseEntity=
                restTemplate.exchange(requestUrl,HttpMethod.GET,getAllegroHttpEntity(),ResponseWrapper.class);
        if(responseEntity.getStatusCode()== HttpStatus.OK){
            return responseEntity.getBody().getCategories();
        }
        //TODO add handling for CategoriesNotFound
        else
            return new CarCategoriesWrapper();
    }
    @PostMapping("/offers")
    public CarsWrapper getOffersForUser(@RequestBody UserPreferenceDTO userPreferenceDTO){
        //get mark id
        Integer categoryId=allegroQueryBuilder.getIdOfSearchMark(userPreferenceDTO.getMark());
        //get models
        CarCategoriesWrapper carCategoriesWrapper=getCarModels(categoryId);;
        Integer modelCategoryId=null;
        //check if model is given
        if(!userPreferenceDTO.getModel().isEmpty()){
                    CarCategory category=carCategoriesWrapper.getSubcategories().stream().filter(carCategory ->
                    carCategory.getName().toLowerCase().equals(userPreferenceDTO.getModel().toLowerCase()))
                    .findFirst().orElseThrow(() ->new RuntimeException("model not found"));
                    modelCategoryId=category.getId();
        }
        Optional<Integer> modelCategoryIdOptional=Optional.ofNullable(modelCategoryId);

        String filtersString=allegroQueryBuilder.compileToSearchString(userPreferenceDTO,modelCategoryIdOptional);
        ResponseEntity<ItemsWrapper> responseEntity=restTemplate.exchange(allegroOffersUri+filtersString,HttpMethod.GET,getAllegroHttpEntity(), ItemsWrapper.class);
        return responseEntity.getBody().getItems();
    }


    public HttpEntity getAllegroHttpEntity(){
        HttpHeaders headers=new HttpHeaders();

        headers.add("Authorization","Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJhbGxlZ3JvX2FwaSJdLCJleHAiOjE1ODM2MTUwNDMsImp0aSI6ImNmOTRmYjgxLTIwMjgtNGI2My1iNGIyLTBiMDJkZWU5YTNlNiIsImNsaWVudF9pZCI6IjhjOTM0NGEyODE1NjQyZjBhMDU5NzdiZDNhOWJkZmQ3In0.s_kg3RQ7CDnQUJZgeolB-0JBfwNj0B4Wop5xDZ4m7b6_MOobZsfEOI5fhCuimU7h5h0-FzJptoZ9KZtUoBtBlaNS8Hv1a8g22oKOZo6xhY2bLtlv7WiLmzDAS3YlxAWgOT1s5Z6kOU-hEJBb8zClnmqKtFojd2g7IiUMFShTLCBr0vKlUSKLFG10bZsnWKtcVW_es0r-aDmHvPyDmcfE6DhoAtsiviM4YaOEgiQ-61UgHy4ELcBqcJSNVGqmD8HrGfZcKYc1Ed0GIgvCkx0Xd_Fuk3AEbX6Ifor0bn7dinTQb2fTU-gDwXqXSeTuDDe8x-W71OrPYQqmvFP4bxe-ow");
        headers.add("Accept","application/vnd.allegro.public.v1+json");

        HttpEntity entity=new HttpEntity(headers);
        return entity;
    }
//    &parametr.11323=11323_2
//    &parametr.199=199_2
//    &parametr.13=13_246582

}
