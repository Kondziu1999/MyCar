package com.KOndziu.usercarservice.payload;


import com.KOndziu.usercarservice.modules.UserTrackingOffers;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTrackingOfferDTO {


    private Integer trackId;

    private String OfferId;

    private String name;

    private Integer userId;


    public static UserTrackingOfferDTO convertToDTO(UserTrackingOffers userTrackingOffers){
        return new UserTrackingOfferDTO(userTrackingOffers.getTrackId(),userTrackingOffers.getOfferId(),
                userTrackingOffers.getName(),userTrackingOffers.getUser().getId());

    }
}
