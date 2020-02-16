package com.KOndziu.usercarservice.modules;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowCarDto {
    private Integer carId;
    private Integer userId;
    private String mark;
    private String carType;
    private String color;
    private String imageURI;

}
