package com.KOndziu.researchserver.DTO;

import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
}
