package com.KOndziu.notificationservice.dto;


import com.KOndziu.notificationservice.modules.UserTrackingOffers;
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
    public List<UserTrackingOffers> userTrackingOffers;
}
