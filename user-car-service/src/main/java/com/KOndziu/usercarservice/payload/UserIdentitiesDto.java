package com.KOndziu.usercarservice.payload;

import com.KOndziu.usercarservice.modules.UserIdentities;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserIdentitiesDto {

    private Integer userId;
    private String postalCode;
    private String locality;
    private Integer houseNr;
    private String email;

    public static  UserIdentitiesDto convertToDTO(UserIdentities user){
        return new UserIdentitiesDto(user.getId(),user.getPostalCode(),user.getLocality(),user.getHouseNr(),user.getEmail());
    }

}
