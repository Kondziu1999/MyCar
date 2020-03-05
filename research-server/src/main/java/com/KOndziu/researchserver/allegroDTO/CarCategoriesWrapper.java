package com.KOndziu.researchserver.allegroDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarCategoriesWrapper {
    //according to allegro api nomenclature
    List<CarCategory> subcategories;
}
