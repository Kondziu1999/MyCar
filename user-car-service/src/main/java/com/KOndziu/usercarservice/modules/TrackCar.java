package com.KOndziu.usercarservice.modules;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tracked_cars")
@Getter
@Setter
@NoArgsConstructor
public class TrackCar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_id")
    private Integer trackId;

    @Column(name = "announcement_id")
    private Integer announcementId;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    public TrackCar(Integer announcementId) {
        this.announcementId = announcementId;
    }
}
