package com.KOndziu.researchserver.allegroDTO.Filters;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
public class HashFilters {
    HashMap<String,CarFilter> hashFilters=new HashMap<>();
}
