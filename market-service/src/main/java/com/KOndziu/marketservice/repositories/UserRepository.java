package com.KOndziu.marketservice.repositories;

import com.KOndziu.marketservice.modules.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {



}
