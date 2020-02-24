package com.KOndziu.usercarservice.repos;

import com.KOndziu.usercarservice.modules.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPreferencesRepository extends JpaRepository<UserPreference,Integer> {

}
