package com.KOndziu.marketservice.repositories;


import com.KOndziu.marketservice.modules.MarketCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;



@Repository
public interface MarketCarRepo extends JpaRepository<MarketCar,Integer>, JpaSpecificationExecutor<MarketCar> {
}
