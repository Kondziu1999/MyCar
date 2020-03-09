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
    public ItemsWrapper getOffersForUser(@RequestBody UserPreferenceDTO userPreferenceDTO){
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
        return responseEntity.getBody();
    }


    public HttpEntity getAllegroHttpEntity(){
        HttpHeaders headers=new HttpHeaders();

        headers.add("Authorization","Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJhbGxlZ3JvX2FwaSJdLCJleHAiOjE1ODM4MjI4NzcsImp0aSI6IjAxMTFiZGYxLTYzMDEtNDE3Zi04MzIxLWZlNDZjNjRlNjFkMyIsImNsaWVudF9pZCI6IjhjOTM0NGEyODE1NjQyZjBhMDU5NzdiZDNhOWJkZmQ3In0.5r38FR18IdZa5orn0kEciwpY4cGYe2NWOH4eeTVvr7zvhFXdrol9Yq38QHs1VCkko2c1EjLoMDA1gOQqlJIHs7SvdhQCO6rIYV2scTSvLV0UXh7l9bl_-VWUE5cBTIGzhJKcWK0rKGo2YDn3qoDsUOnMrN9gvSqdT1uBAkSZ1gPKJNHrTue4H8sHK_B4W-PbgdyZNjNfZXlVTrBF3tHW4nNroUBPjbTBG3H4dobGqJshlaRkWG7n4RW_vBI7DEYVwBT868XI5RO29AdWXmTiQYlvjY0IEvo6WM5TiW1lZgVCQJacNVwbXx1Jot-k0A7lk24Q3ZkNPOUzCHLsFPoJRg");
        headers.add("Accept","application/vnd.allegro.public.v1+json");

        HttpEntity entity=new HttpEntity(headers);
        return entity;
    }
//    &parametr.11323=11323_2
//    &parametr.199=199_2
//    &parametr.13=13_246582

}
