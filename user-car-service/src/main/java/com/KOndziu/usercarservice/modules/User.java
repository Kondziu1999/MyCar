package com.KOndziu.usercarservice.modules;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private UserIdentities userIdentities;

    public User(String name, String secondName) {
        this.name = name;
        this.secondName = secondName;
    }
}
