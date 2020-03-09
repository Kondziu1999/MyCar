package com.KOndziu.notificationservice.dao;

import com.KOndziu.notificationservice.modules.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferenceJpaRepository extends JpaRepository<UserPreference,Integer> {

}
