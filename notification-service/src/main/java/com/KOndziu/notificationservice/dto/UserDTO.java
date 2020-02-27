package com.KOndziu.notificationservice.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Integer id;
    private String name;
    private String secondName;

    //to sending email only name secondName and email is necessary
}
