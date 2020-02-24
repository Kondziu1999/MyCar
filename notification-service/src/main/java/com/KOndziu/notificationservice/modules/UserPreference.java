package com.KOndziu.notificationservice.modules;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "preference_id")
    private Integer preferenceId;
    @Column(name = "user_id")
    private Integer userId ;
    @Column(name = "car_type")
    private String carType ;
    private String state;

    @Column(name = "price_from")
    private Double priceFrom;
    @Column(name = "price_to")
    private Double priceTo;

    private String locality ;
    private String province ;

    @Column(name = "prod_date_from")
    private LocalDate prodDateFrom; //yyyy-MM-dd
    @Column(name = "prod_date_to")
    private LocalDate prodDateTo; //yyyy-MM-dd

    @Column(name = "mileage_from")
    private Integer mileageFrom;
    @Column(name = "mileage_to")
    private Integer mileageTo;

    @Column(name = "eng_size_from")
    private Double engSizeFrom;
    @Column(name = "eng_size_to")
    private Double engSizeTo;

    @Column(name = "eng_force_from")
    private Double engForceFrom;
    @Column(name = "eng_force_to")
    private Double engForceTo;

    private Integer seats;
    private String color;
    private String origin;
    private String mark;
    private String model;
    @Column(name = "fuel_type")
    private String fuelType;

}
