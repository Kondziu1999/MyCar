package com.KOndziu.usercarservice.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTrackingOffersWrapper {
    private List<UserTrackingOfferDTO> userTrackingOffers;
}
