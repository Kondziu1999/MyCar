package com.KOndziu.researchserver.allegroDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class CarFilter {

    private String id;
    private String type;
    private String name;
    private List<FilterValue> values;
    private Integer minValue; //optional for numeric
    private Integer maxValue;   //optional for numeric
}
