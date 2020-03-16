package com.KOndziu.ApiGateway.repos;

import com.KOndziu.ApiGateway.models.MyUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<MyUser, Integer> {

    //find by nick
    @Query(value = "Select * from users u where u.username = ?1",nativeQuery = true)
    Optional<MyUser> findByUsername(String username);

    //find by nick and email in order to check if given user is already in db
    Optional<MyUser> findByRegistrationEmail(String registrationEmail);
}
