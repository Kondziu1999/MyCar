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

    private ResponseWrapper filterWrapper;
    private HashFilters hashFilters;
    private RestTemplate restTemplate;
    private AllegroQueryBuilder allegroQueryBuilder;

    @Autowired
    public AllegroConnector(ResponseWrapper filterWrapper,HashFilters hashFilters,
                            RestTemplate restTemplate,AllegroQueryBuilder allegroQueryBuilder) {

    this.filterWrapper=filterWrapper;
    this.hashFilters=hashFilters;
    this.restTemplate=restTemplate;
    this.allegroQueryBuilder=allegroQueryBuilder;
    }

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

    HttpEntity entity=getAllegroHttpEntity();
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
    public ResponseEntity<ItemsWrapper> getOffersForUser(@RequestBody UserPreferenceDTO userPreferenceDTO){
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
        return responseEntity;
    }


    public HttpEntity getAllegroHttpEntity(){
        HttpHeaders headers=new HttpHeaders();

        headers.add("Authorization","Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJhbGxlZ3JvX2FwaSJdLCJleHAiOjE1ODQwNTI1MDcsImp0aSI6ImY2MmIwMmQ3LTZhMDAtNGM1Yy04ZGFiLWU2MGMzZmI1YzQxMCIsImNsaWVudF9pZCI6IjhjOTM0NGEyODE1NjQyZjBhMDU5NzdiZDNhOWJkZmQ3In0.Cdbbx42Bv6tLbESPzhsUp2cG6jkT7FOCr360q4JZrZpLz-YSnxIUeLN2KA6G0hn9fx187BaJ-VpSamof8PAhgQRoUv2WG8SLuTURblOHegorpz2Q-aCIt1To88rsh7Dn1A9W4hqmNadlPIgWsmoALm7ID-OGlmDzL7X0_tRETeLpPJHcqw2voJY7VPbM4Cbv2cpfWJTXuRu5GxCOeTd-kWMMER-9byDfy7vs26JBruF-wUp-iuHgLubOoUxHT0lrSl5g3FbKJifkIcdJ8iwWiyAT4sVPuupBNjI_pq3eJDdWZ0qEvJocIUnY0tBTbRl-DoI2iSYj6XP9uGhXNjTTGA");
        headers.add("Accept","application/vnd.allegro.public.v1+json");

        HttpEntity entity=new HttpEntity(headers);
        return entity;
    }


}
