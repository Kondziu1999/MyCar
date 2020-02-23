package com.KOndziu.usercarservice.payload;


import com.KOndziu.usercarservice.modules.FollowCar;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowCarDto {
    private Integer carId;
   // private Integer userId;
    private String mark;
    private String carType;
    private String color;
    private String imageURI;

    public static FollowCarDto getDTO(FollowCar followCar,String downloadUrl){
        return FollowCarDto.builder()
                .carId(followCar.getCarId())
                .carType(followCar.getCarType())
                .color(followCar.getColor())
                .mark(followCar.getMark())
                .imageURI(downloadUrl)
                .build();
    }
}
