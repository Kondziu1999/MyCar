package com.KOndziu.marketservice.modules;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "marketcar_pic")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketCarPic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Integer photoId;

    @Column(name = "announcement_id")
    private Integer announcementId;

    @Column(name = "photo")
    private byte[] photo;

    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JoinColumn(name = "anno_id",nullable = false)
    @Getter(AccessLevel.PRIVATE) //in order to avoid infinite looping
    private MarketCar marketCar;
}
