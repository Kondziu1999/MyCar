package com.KOndziu.notificationservice.dto.ResearchServicePayload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CarsWrapper {
    List<CarInfo> promoted;  //promoted offers
    List<CarInfo> regular;  //regular offers
}
