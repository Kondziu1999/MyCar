package com.KOndziu.marketservice.modules;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="market_cars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MarketCar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId ;

    @Column(name = "anno_id")
    private Integer annoId ;

    @Column(name = "car_type")
    private String carType ;

    private String state;
    private Double price;
    private String locality ;
    private String province ;

    @Column(name = "prod_date")
    private LocalDate prodDate; //yyyy-MM-dd

    private Integer mileage;

    @Column(name = "eng_size")
    private Double engSize;

    @Column(name = "eng_force")
    private Double engForce;

    private Integer seats;
    private String color;
    private String origin;

    @OneToMany(mappedBy = "marketCar", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    public final Set<MarketCarPic> marketCarPics=new HashSet<>();

    public void addMarketCarPic(MarketCarPic marketCarPic){
        this.marketCarPics.add(marketCarPic);
    }


}
