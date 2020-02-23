package com.KOndziu.usercarservice.payload;

import com.KOndziu.usercarservice.modules.User;
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

    public static UserDTO getDTO(User user,String marketServicePort){
        return new UserDTO(user.getId(), user.getName(), user.getSecondName(),
                "http://localhost:8080/users/identities" + user.getId(),
                "http://localhost:8080/cars/" + user.getId(),
                "http://localhost:" + marketServicePort + "/market/" + user.getId());
    }
}
