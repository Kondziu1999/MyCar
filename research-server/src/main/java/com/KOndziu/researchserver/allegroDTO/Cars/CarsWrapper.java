package com.KOndziu.researchserver.allegroDTO.Cars;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CarsWrapper {
    List<CarInfo> promoted;  //promoted offers
    List<CarInfo> regular;  //regular offers
}
