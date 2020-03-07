package com.KOndziu.researchserver.allegroDTO.Filters;

import com.KOndziu.researchserver.DTO.UserPreferenceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AllegroQueryBuilder {

    //TODO
    // query format -> ?param[.suffix]=value

    @Autowired
    HashFilters hashFilters;
    @Autowired
    ResponseWrapper filterWrapper;
    //filterWrapper contains all filters available
    //categories->subcategories contain mark name and category id which is crucial to build search query



    //TODO change this awful function
    public String compileToSearchString(UserPreferenceDTO userPreferenceDTO,Optional<Integer> modelCategoryId){
        //TODO find particular model id
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("?category.id=");
        //if model is specified there is need to go down the search tree
        if(modelCategoryId.isPresent()) {
            stringBuilder.append(modelCategoryId.get());
        }
        //if model is not specified
        else {
            stringBuilder.append(getIdOfSearchMark(userPreferenceDTO.getMark()));
        }

        List<String> filters= Arrays.asList(
        (userPreferenceDTO.getCarType()!=null)? getGivenParam("nadwozie",userPreferenceDTO.getCarType(),null) :"",
        (userPreferenceDTO.getColor()!=null)? getGivenParam("Kolor",userPreferenceDTO.getColor(),null) : "",
        (userPreferenceDTO.getEngForceFrom()!=null)? getGivenParam("Moc",userPreferenceDTO.getEngForceFrom().toString(),"od") : "",
        (userPreferenceDTO.getEngForceTo()!=null)? getGivenParam("Moc",userPreferenceDTO.getEngForceTo().toString(),"do") : "",
        (userPreferenceDTO.getEngSizeFrom()!=null)? getGivenParam("Pojemność silnika",userPreferenceDTO.getEngSizeFrom().toString(),"od") : "",
        (userPreferenceDTO.getEngSizeTo()!=null)? getGivenParam("Pojemność silnika",userPreferenceDTO.getEngSizeTo().toString(),"do") : "",
        (userPreferenceDTO.getFuelType()!=null)? getGivenParam("Rodzaj paliwa",userPreferenceDTO.getFuelType(),null) : "",
        (userPreferenceDTO.getLocality()!=null)? getGivenParam("miejscowość",userPreferenceDTO.getLocality(),null) : "",
        (userPreferenceDTO.getProvince()!=null)? getGivenParam("województwo",userPreferenceDTO.getProvince(),null) : "",
        (userPreferenceDTO.getMileageFrom()!=null)? getGivenParam("Przebieg",userPreferenceDTO.getMileageFrom().toString(),"od") : "",
        (userPreferenceDTO.getMileageTo()!=null)? getGivenParam("Przebieg",userPreferenceDTO.getMileageTo().toString(),"do") : "",
        (userPreferenceDTO.getOrigin()!=null)? getGivenParam("Kraj pochodzenia",userPreferenceDTO.getOrigin(),null) : "",
        (userPreferenceDTO.getPriceFrom()!=null)? getGivenParam("cena",userPreferenceDTO.getPriceFrom().toString(),"od") : "",
        (userPreferenceDTO.getPriceTo()!=null)? getGivenParam("cena",userPreferenceDTO.getPriceTo().toString(),"do") : "",
        (userPreferenceDTO.getSeats()!=null)? getGivenParam("Liczba miejsc",userPreferenceDTO.getSeats().toString(),"od") : "",
        (userPreferenceDTO.getState()!=null)? getGivenParam("Stan",userPreferenceDTO.getState(),null) : "",
        (userPreferenceDTO.getProdDateFrom()!=null)? getGivenParam("Rok produkcji",String.valueOf(userPreferenceDTO.getProdDateFrom().getYear()),"od") : "",
        (userPreferenceDTO.getProdDateTo()!=null)? getGivenParam("Rok produkcji",String.valueOf(userPreferenceDTO.getProdDateTo().getYear()),"do") : ""
        );

        filters.stream().forEach(filter ->{
            //i filter is present append to query string
            if(!filter.isEmpty()){
                stringBuilder.append(filter);
            }
        });

        return stringBuilder.toString();
    }
    //actual value is here in order to check out if such a value is available
    public String getGivenParam(String param,String actualValue,String suffix){
        //get car filter
        CarFilter carFilter=hashFilters.getHashFilters().get(param.toLowerCase());
        //find filter with given name
        Optional<FilterValue> filterValueOptional=carFilter.getValues().stream().filter(filterValue -> {
            //get get filter value name for example nadwozie -> hatchback
            if(suffix==null) {
                String value = filterValue.getName().toLowerCase();
                return value.equals(actualValue.toLowerCase());
            }
            else {
                String value=filterValue.getName().toLowerCase();
                return value.equals(suffix);
            }
        }).findFirst();

        if(filterValueOptional.isPresent()){
            FilterValue value=filterValueOptional.get();
            //check if given filter have suffix for example price : .from ...  .to
            //no suffix
            if(value.getIdSuffix()==null){
                return "&"+carFilter.getId()+"="+value.getValue();
            }
            //suffix
            //for clarity lets assume that suffix is correct
            //allegro accept integers so there is need to parse some double to int
            else {
                return "&"+carFilter.getId()+value.getIdSuffix()+"="+Math.round(Double.parseDouble(actualValue)); //to get int
            }
        }
        //if filter doesnt exist return empty string
        else return "";
    }

    public Integer getIdOfSearchMark(String mark){
        CarCategory carCategory=filterWrapper.getCategories().getSubcategories().stream()
                .filter(subcategory ->subcategory.getName().toLowerCase().equals(mark.toLowerCase())).findFirst()
                .orElseThrow(()->new RuntimeException("car with given name cannot be found"));

        return carCategory.getId();
    }




}
