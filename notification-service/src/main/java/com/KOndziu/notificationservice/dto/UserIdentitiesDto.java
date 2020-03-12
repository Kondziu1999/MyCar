package com.KOndziu.notificationservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserIdentitiesDto {

    private Integer userId;
    private String postalCode;
    private String locality;
    private Integer houseNr;
    private String email;

}
