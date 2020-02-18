package com.KOndziu.marketservice.modules;

import lombok.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MarketDto {

  private Integer announcementId;
  private String carType;
  private String state;
  private Double price;
  private String endpoint;



}
