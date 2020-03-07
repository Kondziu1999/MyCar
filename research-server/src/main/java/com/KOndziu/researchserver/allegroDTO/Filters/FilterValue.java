package com.KOndziu.researchserver.allegroDTO.Filters;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilterValue {

    private String idSuffix; //optional param
    private String value;
    private String name;
    private Integer count;
    private boolean selected;

}
