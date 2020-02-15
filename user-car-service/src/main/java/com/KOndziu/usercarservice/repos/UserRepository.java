package com.KOndziu.usercarservice.repos;

import com.KOndziu.usercarservice.modules.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByNameAndAndSecondName(String name, String secondName);

}
