package com.KOndziu.marketservice.dao;
import com.KOndziu.marketservice.modules.MarketCar;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketCarDto {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //int order to hide it in serialization
    private Integer userId ;
    private Integer annoId ;
    private String carType ;
    private String state;
    private Double price;
    private String locality ;
    private String province ;
    private LocalDate prodDate; //yyyy-MM-dd
    private Integer mileage;
    private Double engSize;
    private Double engForce;
    private Integer seats;
    private String color;
    private String origin;
    private Set<String> picURLs;
    private String mark;
    private String model;
    private String fuelType;

    public static MarketCar getMarket(MarketCarDto marketCarDto){
        return MarketCar.builder()
                .annoId(marketCarDto.getAnnoId())
                .carType(marketCarDto.getCarType())
                //.userId(marketCarDto.getUserId())
                .color(marketCarDto.getColor())
                .engForce(marketCarDto.getEngForce())
                .engSize(marketCarDto.getEngSize())
                .locality(marketCarDto.getLocality())
                .mileage(marketCarDto.getMileage())
                .origin(marketCarDto.getOrigin())
                .price(marketCarDto.getPrice())
                .prodDate(marketCarDto.getProdDate())
                .province(marketCarDto.getProvince())
                .seats(marketCarDto.getSeats())
                .state(marketCarDto.getState())
                .fuelType(marketCarDto.getFuelType())
                .mark(marketCarDto.getMark())
                .model(marketCarDto.getModel())
                // here could be pics
                .build();
    }
    public static MarketCarDto getDTO(MarketCar marketCar,Set<String> marketCarPicsURLs){
        return  MarketCarDto.builder()
                .annoId(marketCar.getAnnoId())
                .carType(marketCar.getCarType())
                //.userId(marketCar.getUserId())
                .color(marketCar.getColor())
                .engForce(marketCar.getEngForce())
                .engSize(marketCar.getEngSize())
                .locality(marketCar.getLocality())
                .mileage(marketCar.getMileage())
                .picURLs(marketCarPicsURLs)
                .origin(marketCar.getOrigin())
                .price(marketCar.getPrice())
                .prodDate(marketCar.getProdDate())
                .province(marketCar.getProvince())
                .seats(marketCar.getSeats())
                .state(marketCar.getState())
                .fuelType(marketCar.getFuelType())
                .mark(marketCar.getMark())
                .model(marketCar.getModel())
                .build();
    }

}
