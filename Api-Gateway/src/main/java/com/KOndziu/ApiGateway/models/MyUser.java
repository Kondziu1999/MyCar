package com.KOndziu.ApiGateway.models;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer userId;

    @Column(name = "name")
    private String firstName;
    @Column(name = "second_name")
    private String secondName;
    @Column(name = "registry_email")
    private String registrationEmail;
    //nick
    @Column(name = "username")
    private String username;
    private String password;
    private String authorities;


}
