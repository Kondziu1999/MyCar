package com.KOndziu.usercarservice.modules;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "second_name")
    private String secondName;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private UserIdentities userIdentities;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<FollowCar> followCarList=new HashSet<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<MarketCar> marketCars=new HashSet<>();

    @OneToMany(mappedBy = "user",fetch =FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<TrackCar> trackCars=new HashSet<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<UserPreference> userPreferences=new HashSet<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<UserTrackingOffers> userTrackingOffers=new LinkedList<>();


    public void  addTrackCar(TrackCar trackCar){trackCars.add(trackCar);}
    public void addFollowCar(FollowCar followCar){
        followCarList.add(followCar);
    }
    public User(String name, String secondName) {
        this.name = name;
        this.secondName = secondName;
    }
}
