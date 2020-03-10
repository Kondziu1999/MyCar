package com.KOndziu.notificationservice.modules;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_tracking_offers")
@Getter
@Setter
@NoArgsConstructor
public class UserTrackingOffers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_id")
    private Integer trackId;

    @Column(name = "offer_id")
    private String OfferId;

    @Column(name = "name")
    private String name;

    public UserTrackingOffers(String offerId, String name) {
        OfferId = offerId;
        this.name = name;
    }
}
