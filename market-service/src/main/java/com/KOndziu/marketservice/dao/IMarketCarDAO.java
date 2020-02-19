package com.KOndziu.marketservice.dao;

import com.KOndziu.marketservice.modules.MarketCar;
import com.KOndziu.marketservice.modules.User;
import com.KOndziu.marketservice.specifications.SearchCriteria;

import java.util.List;

public interface IMarketCarDAO {

     List<MarketCar> searchMarketCar(List<SearchCriteria> params);
     void save(MarketCar entity);
}
