package com.KOndziu.usercarservice.repos;


import com.KOndziu.usercarservice.modules.UserTrackingOffers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTrackingOffersRepository extends JpaRepository<UserTrackingOffers,Integer> {
}
