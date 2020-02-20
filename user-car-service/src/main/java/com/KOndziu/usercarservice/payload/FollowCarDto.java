package com.KOndziu.usercarservice.payload;


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

}
