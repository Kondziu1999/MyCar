package com.KOndziu.marketservice.modules;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_identities")
@Getter
@Setter
@NoArgsConstructor
public class UserIdentities {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "postal_code")
    private String postalCode;
    @Column(name = "loacality")
    private String locality;
    @Column(name = "house_nr")
    private Integer houseNr;
    @Column(name = "email")
    private String email;

    @OneToOne(cascade = CascadeType.PERSIST)
    @MapsId
    //@Getter(value = AccessLevel.PRIVATE)//in order to avoid infinite looping
    private User user;

    public UserIdentities(String postalCode, String locality, Integer houseNr, String email, User user) {
        this.postalCode = postalCode;
        this.locality = locality;
        this.houseNr = houseNr;
        this.email = email;
        this.user = user;
    }
}
