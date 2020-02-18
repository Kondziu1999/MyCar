package com.KOndziu.marketservice.repositories;


import com.KOndziu.marketservice.modules.MarketCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketCarRepo extends JpaRepository<MarketCar,Integer> {

    List<MarketCar> findAllBy
}
