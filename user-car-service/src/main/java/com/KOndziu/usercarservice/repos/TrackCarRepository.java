package com.KOndziu.usercarservice.repos;

import com.KOndziu.usercarservice.modules.TrackCar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackCarRepository extends JpaRepository<TrackCar,Integer> {
}
