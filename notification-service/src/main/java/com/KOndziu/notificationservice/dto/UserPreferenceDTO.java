package com.KOndziu.notificationservice.dto;

import com.KOndziu.notificationservice.modules.UserPreference;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferenceDTO {

    private Integer userId ;
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

    public static UserPreferenceDTO convertToDTO(UserPreference userPreference){
        return UserPreferenceDTO.builder()
                .carType(userPreference.getCarType())
                .color(userPreference.getColor())
                .engForceFrom(userPreference.getEngForceFrom())
                .engForceTo(userPreference.getEngForceTo())
                .engSizeFrom(userPreference.getEngSizeFrom())
                .engSizeTo(userPreference.getEngSizeTo())
                .fuelType(userPreference.getFuelType())
                .locality(userPreference.getLocality())
                .mark(userPreference.getMark())
                .mileageFrom(userPreference.getMileageFrom())
                .mileageTo(userPreference.getMileageTo())
                .model(userPreference.getModel())
                .origin(userPreference.getOrigin())
                .priceFrom(userPreference.getPriceFrom())
                .priceTo(userPreference.getPriceTo())
                .prodDateFrom(userPreference.getProdDateFrom())
                .prodDateTo(userPreference.getProdDateTo())
                .province(userPreference.getProvince())
                .seats(userPreference.getSeats())
                .state(userPreference.getState())
                .userId(userPreference.getUserId())
            .build();
    }

}
