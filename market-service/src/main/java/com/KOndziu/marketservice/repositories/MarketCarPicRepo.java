package com.KOndziu.marketservice.repositories;

import com.KOndziu.marketservice.modules.MarketCarPic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketCarPicRepo extends JpaRepository<MarketCarPic, Integer> {

    List<MarketCarPic> findAllByAnnouncementId(Integer announcementId);
}
