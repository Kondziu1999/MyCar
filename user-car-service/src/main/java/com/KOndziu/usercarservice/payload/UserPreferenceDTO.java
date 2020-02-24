package com.KOndziu.usercarservice.payload;

import com.KOndziu.usercarservice.modules.User;
import com.KOndziu.usercarservice.modules.UserPreference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPreferenceDTO {

    //userId is here in order to send whole preference in one json
    private Integer userId;
    private String carType ;
    private String state;
    private Double priceFrom;
    private Double priceTo;
    private String locality ;
    private String province ;
    private LocalDate prodDateFrom; //yyyy-MM-dd
    private LocalDate prodDateTo; //yyyy-MM-dd
    private Integer mileageFrom;
    private Integer mileageTo;
    private Double engSizeFrom;
    private Double engSizeTo;
    private Double engForceFrom;
    private Double engForceTo;
    private Integer seats;
    private String color;
    private String origin;
    private String mark;
    private String model;
    private String fuelType;


    public static UserPreference convertFromDTO(UserPreferenceDTO userPreferenceDTO, User user){
        return  UserPreference.builder()
                .carType(userPreferenceDTO.getCarType())
                .color(userPreferenceDTO.getColor())
                .engForceFrom(userPreferenceDTO.getEngForceFrom())
                .engForceTo(userPreferenceDTO.getEngForceTo())
                .engSizeFrom(userPreferenceDTO.getEngSizeFrom())
                .engSizeTo(userPreferenceDTO.getEngSizeTo())
                .fuelType(userPreferenceDTO.getFuelType())
                .locality(userPreferenceDTO.getLocality())
                .mark(userPreferenceDTO.getMark())
                .mileageFrom(userPreferenceDTO.getMileageFrom())
                .mileageTo(userPreferenceDTO.getMileageTo())
                .model(userPreferenceDTO.getModel())
                .origin(userPreferenceDTO.getOrigin())
                .priceFrom(userPreferenceDTO.getPriceFrom())
                .priceTo(userPreferenceDTO.getPriceTo())
                .prodDateFrom(userPreferenceDTO.getProdDateFrom())
                .prodDateTo(userPreferenceDTO.getProdDateTo())
                .province(userPreferenceDTO.getProvince())
                .seats(userPreferenceDTO.getSeats())
                .state(userPreferenceDTO.getState())
                .user(user)
                .build();
    }

}
