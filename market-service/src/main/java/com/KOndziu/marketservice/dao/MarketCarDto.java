package com.KOndziu.marketservice.dao;
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







}
