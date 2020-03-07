package com.KOndziu.researchserver.allegroDTO.Filters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWrapper {

    private List<CarFilter> filters;
    private CarCategoriesWrapper categories;
}
