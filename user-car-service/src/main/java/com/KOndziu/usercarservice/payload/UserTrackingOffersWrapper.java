package com.KOndziu.usercarservice.payload;

import com.KOndziu.usercarservice.modules.UserTrackingOffers;
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
    private List<UserTrackingOffers> userTrackingOffers;
}
