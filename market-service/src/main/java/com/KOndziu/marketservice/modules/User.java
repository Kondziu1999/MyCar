package com.KOndziu.marketservice.modules;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
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

    public void addFollowCar(FollowCar followCar){
        followCarList.add(followCar);
    }
    public User(String name, String secondName) {
        this.name = name;
        this.secondName = secondName;
    }
}
