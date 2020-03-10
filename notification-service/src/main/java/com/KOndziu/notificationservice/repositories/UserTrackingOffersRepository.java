package com.KOndziu.notificationservice.repositories;

import com.KOndziu.notificationservice.modules.UserTrackingOffers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTrackingOffersRepository extends JpaRepository<UserTrackingOffers,Integer> {
}
