package com.KOndziu.notificationservice.services;

import com.KOndziu.notificationservice.dao.UserPreferenceDAO;
import com.KOndziu.notificationservice.dto.MarketCarDto;
import com.KOndziu.notificationservice.modules.UserPreference;
import com.KOndziu.notificationservice.specifications.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserPreferenceService {

    @Autowired
    private  UserPreferenceDAO userPreferenceDAO;

//    @Autowired
//    public UserPreferenceService(UserPreferenceDAO userPreferenceDAO) {
//        this.userPreferenceDAO = userPreferenceDAO;
//    }


    //call user preference dao to get appropriate UserPreferences
    public List<UserPreference> findPreferences(String search) {
        List<SearchCriteria> params = new ArrayList<>();
        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            //split search params into key operation value
            while (matcher.find()) {
                params.add(new SearchCriteria(matcher.group(1)
                        , matcher.group(2), matcher.group(3)));
            }
        }

        return userPreferenceDAO.searchUserPreference(params);
    }

    //create search string by given marketCar (it was added in market controller)
    public String compileSearchString(MarketCarDto marketCarDto){
       List<String> params= Arrays.asList(
               (!marketCarDto.getCarType().isEmpty()) ? "carType:"+ marketCarDto.getCarType() : "",
               (marketCarDto.getColor()!=null) ? "color:"+marketCarDto.getColor() : "",
               (marketCarDto.getFuelType()!=null) ? "fuelType:"+marketCarDto.getFuelType() : "",
               (marketCarDto.getLocality()!=null) ? "locality:"+marketCarDto.getLocality() : "",
               (marketCarDto.getMark()!=null) ? "mark:"+marketCarDto.getMark() : "",
               (marketCarDto.getModel()!=null) ? "model:"+marketCarDto.getModel() : "",
               (marketCarDto.getOrigin()!=null) ? "origin:"+marketCarDto.getOrigin() : "",
               (marketCarDto.getProvince()!=null) ? "province:"+marketCarDto.getProvince() : "",
               (marketCarDto.getState()!=null) ? "state:"+marketCarDto.getState() : "",
               (marketCarDto.getSeats()!=null) ? "seats:"+marketCarDto.getSeats().toString() : "",
               (marketCarDto.getEngForce()!=null) ? "engForce:"+marketCarDto.getEngForce().toString() : "",
               (marketCarDto.getPrice()!=null) ? "priceTo<"+marketCarDto.getPrice().toString() : "",
               (marketCarDto.getMileage()!=null) ? "mileageTo<"+marketCarDto.getMileage().toString() : "",
               (marketCarDto.getEngSize()!=null) ? "engSize<"+marketCarDto.getEngSize().toString() : ""
               );

       List<String> pureParams=params.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
       String paramString=pureParams.stream().reduce("",(subtotal,element)->subtotal+element+",");
       //in order to delete last comma
       String finalParams=paramString.substring(0,paramString.length()-1);

       return "search="+finalParams;
    }

}
