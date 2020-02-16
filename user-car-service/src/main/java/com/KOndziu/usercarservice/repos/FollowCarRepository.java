package com.KOndziu.usercarservice.repos;

import com.KOndziu.usercarservice.modules.FollowCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowCarRepository extends JpaRepository<FollowCar,Integer> {
}
