package com.KOndziu.usercarservice.modules;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "follow_cars")
@Getter
@Setter
@NoArgsConstructor
@Builder
public class FollowCar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private Integer carId;

    @Column(name = "id")
    private Integer userId;

    @Column(name = "mark")
    private String mark;

    @Column(name = "car_type")
    private String carType;

    @Column(name = "color")
    private String color;

    @Column(name = "image")
    @Lob
    private byte[] image;


}
