package com.KOndziu.usercarservice.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Integer id;
    private String name;
    private String secondName;
    private String userIdentitiesURL;
    private String userMarketCarsURL;
    private String userTrackCarsURL;

}
